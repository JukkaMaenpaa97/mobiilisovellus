# Abstaction for database objects
# Automatically constructs queries for single database entries

from database.database import mysql, query

class DataModel:
    table = None
    primary_column = None
    id = None

    fields = {}

    def update(self):
        print("Update")

    def create(self):
        print("Create")

    def delete(self):
        print("Delete")

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

    def populate(self):
        print("Populate")

    def serialize(self):
        sdict = {}
        for key, value in self.fields.items():
            if value['public'] == True:
                sdict[key] = value['value']
        return {self.id: sdict}

    def set(self, key, value):
        if key in self.fields and self.fields[key]['public'] == True:
            self.fields[key]['value'] = value

    def get(self, key):
        if key in self.fields and self.fields[key]['public'] == True:
            return self.fields[key]['value']
        else:
            return None

    def field(self, value=None, required=False, public=True, formatter=None, sanity=None):
        return {
            "value": value,
            "required": required,
            "public": public,
            "formatter": formatter ,
            "sanity": sanity
        }
