# class for handling multiple instances of datacomponent
# components use the same model passed to constructor

from database.database import mysql, query

class DataModelList:
    model = None
    data  = None

    # just adding the model to list class
    def __init__(self, model):
        self.model = model
        self.data = {}

    # loads a list of datamodels based on query
    def load(self, sql, args = {}):
        result = query(sql, args)

        if result == None:
            return False

        for instance in result:
            model_instance = self.model()
            if model_instance.populate(instance):
                self.data[model_instance.id] = model_instance
            else:
                return False

        return True

    def computeAll(self):
        for key, model_instance in self.data.items():
            model_instance.computeValues()

    def count(self):
        return len(self.data)

    # returns the serialized data for api
    def serialize(self):
        sdata = []

        for model_id, model_instance  in self.data.items():
            sdata.append(model_instance.serialize())

        return sdata
