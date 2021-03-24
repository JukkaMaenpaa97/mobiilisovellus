# Abstaction for database objects
# Automatically constructs queries for single database entries

from database.database import mysql, query
import uuid

class DataModel:
    table = None
    primary_column = None
    id = None

    fields = {}

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
            query(sql, update_values)
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
            query(sql, insert_values)

            return True
        else:
            return False

    # delete the object from the database
    def delete(self):
        if self.id != None:
            sql = "DELETE FROM "+self.table+" WHERE "+self.primary_column+"=%("+self.primary_column+")s"
            args = {self.primary_column: self.id}
            query(sql, args)
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

    # populate object from dictionary containing required data
    def populate(self):
        print("Populate")

    # returns a dictionary of all public fields
    def serialize(self):
        sdict = {}
        for key, value in self.fields.items():
            if value['public'] == True:
                sdict[key] = value['value']
        return {self.id: sdict}

    # set a fields value
    # *** sanity checker not implemented yet ***
    def set(self, key, value):
        if key in self.fields and self.fields[key]['public'] == True:
            self.fields[key]['value'] = value

    # get a fields value
    # *** formatter not implemented yet ***
    def get(self, key):
        if key in self.fields and self.fields[key]['public'] == True:
            return self.fields[key]['value']
        else:
            return None

    # constructor for a field
    def field(self, value=None, required=False, public=True, formatter=None, sanity=None):
        return {
            "value": value,
            "required": required,
            "public": public,
            "formatter": formatter ,
            "sanity": sanity
        }

    def generateId(self):
        return str(uuid.uuid4())
