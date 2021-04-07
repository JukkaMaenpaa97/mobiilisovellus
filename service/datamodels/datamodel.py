# Abstaction for database objects
# Automatically constructs queries for single database entries

from database.database import mysql, query
import uuid

class DataModel:
    table = None
    primary_column = None
    id = None

    fields = {}
    computed_fields = {}

    # update the current object
    def update(self):
        if self.id != None:
            sql = "UPDATE "+self.table+" SET "
            update_fields = []
            update_values = {}
            for key, value in self.fields.items():
                update_fields.append(key+"=%("+key+")s")
                update_values[key] = value['value']

            sql = sql + ", ".join(update_fields)
            sql = sql + " WHERE "+self.primary_column+"=%(unique_identifier)s"
            update_values["unique_identifier"] = self.id
            if query(sql, update_values) == None:
                return False

            self.onUpdate()
            return True
        else:
            return False

    # create a new object
    def create(self):

        # if id is set, do not allow duplicate creation
        if self.id == None:
            # generate new id
            self.id = self.generateId()
            self.set(self.primary_column, self.id)

            # generate sql
            sql = "INSERT INTO "+self.table
            insert_fields = []
            insert_placeholders = []
            insert_values = {}
            for key, value in self.fields.items():
                insert_fields.append(key)
                insert_placeholders.append("%("+key+")s")
                insert_values[key] = value['value']

            sql = sql + "("+",".join(insert_fields)+")"
            sql = sql + " VALUES("+",".join(insert_placeholders)+")"
            if query(sql, insert_values) == None:
                return False

            self.onCreate()
            return True
        else:
            return False

    # delete the object from the database
    def delete(self):
        if self.id != None:
            sql = "DELETE FROM "+self.table+" WHERE "+self.primary_column+"=%("+self.primary_column+")s"
            args = {self.primary_column: self.id}

            if query(sql, args) == None:
                return False

            self.onDelete()
            self.id == None
            return True
        else:
            return False

    # load the data from db with id
    def load(self, id):
        #constructing raw sql query
        sql = "SELECT "
        query_fields = []
        for f in self.fields:
            query_fields.append(f)

        sql = sql + ", ".join(query_fields)
        sql = sql + " FROM "+self.table+" WHERE "+self.primary_column+" = %(id)s"

        result = query(sql, {"id": id}, True)
        if result == None:
            return False
        else:
            for key, value in result.items():
                self.set(key, value)
            self.id = id
            self.onLoad()
            return True

    # populate object from dictionary containing required data
    # Note: private fields cannot be populated from outside
    # Note 2: private field CAN now be populated
    def populate(self, data):
        self.id = data[self.primary_column]

        for key, value in data.items():
            self.set(key, value, False)

        self.onPopulate()
        return True

    # overridable methods
    # these are always run after the corresponding operation
    # ONLY if the operation was succesfull
    def onUpdate(self):
        pass

    def onCreate(self):
        pass

    def onLoad(self):
        pass

    def onDelete(self):
        pass

    def onPopulate(self):
        pass



    # returns a dictionary of all public fields
    # include_private list can be used to include private fields explicitly
    # for example in a situation where user want their own information etc.
    def serialize(self, include_private = []):
        sdict = {}

        # return all normal fields
        for key, value in self.fields.items():
            if value['public'] == True or key in include_private:
                if value['formatter'] != None and callable(value['formatter']):
                    sdict[key] = value['formatter'](value['value'])
                else:
                    sdict[key] = value['value']

        # add computed fields to dict
        for key, item in self.computed_fields.items():
            sdict[key] = item['value']

        return sdict

    # set a fields value
    def set(self, key, value, compute=True):
        if key in self.fields:
            if self.fields[key]['sanity'] != None and callable(self.fields[key]['sanity']):
                self.fields[key]['value'] = self.fields[key]['sanity'](value)
            else:
                self.fields[key]['value'] = value

            if compute:
                # checking if field has any computable fields
                # looping through computable fields and checking if any have current field as parent
                for ckey, item in self.computed_fields.items():

                    # if parent is set to current field, run the handler function for the value
                    if item['parent'] == key:
                        self.computed_fields[ckey]['value'] = self.computed_fields[ckey]['handler'](value)

    # get a fields value
    def get(self, key):
        if key in self.fields:
            if self.fields[key]['formatter'] != None and callable(self.fields[key]['formatter']):
                return self.fields[key]['formatter'](self.fields[key]['value'])
            else:
                return self.fields[key]['value']
        else:
            return None

    # computes all computed values in object
    # single instance's values are automatically computed, but
    # populate function does not compute any, because of recursion danger.
    # After populating, values must be computed explicitly
    def computeValues(self):
        # checking if field has any computable fields
        # looping through computable fields and checking if any have current field as parent
        for ckey, item in self.computed_fields.items():
            self.computed_fields[ckey]['value'] = self.computed_fields[ckey]['handler'](self.fields[self.computed_fields[ckey]['parent']]['value'])

    # get a computed field, direct setting is not permitted
    def getComputed(self, key):
        if key in self.computed_fields:
            return self.computed_fields[key]['value']
        else:
            return None

    # setting a private field
    # DEPRECATED
    def setPrivate(self, key, value):
        if self.fields[key]['sanity'] != None and callable(self.fields[key]['sanity']):
            self.fields[key]['value'] = self.fields[key]['sanity'](value)
        else:
            self.fields[key]['value'] = value

    # get a private field
    # DEPRECATED
    def getPrivate(self, key):
        if self.fields[key]['formatter'] != None and callable(self.fields[key]['formatter']):
            return self.fields[key]['formatter'](self.fields[key]['value'])
        else:
            return self.fields[key]['value']

    # getting all the fields for queries etc.
    def getFields(self, type = "sql_string"):
        keylist = []
        for key, field in self.fields.items():
            keylist.append(key)

        if type == "sql_string":
            return ", ".join(keylist)
        elif type == "list":
            return keylist

    def getTable(self):
        return self.table

    # constructor for a field
    def field(self, value=None, required=False, public=True, formatter=None, sanity=None):
        return {
            "value": value,
            "required": required,
            "public": public,
            "formatter": formatter ,
            "sanity": sanity
        }

    def computedField(self, value=None, parent=None, handler=None):
        return {
            "value": value,
            "parent": parent,
            "handler": handler
        }

    def generateId(self):
        return str(uuid.uuid4())
