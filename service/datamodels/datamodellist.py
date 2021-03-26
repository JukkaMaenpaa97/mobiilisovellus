# class for handling multiple instances of datacomponent
# components use the same model passed to constructor

from database.database import query

class DataModelList:
    model = None
    data  = {}

    def __init__(self, model):
        self.model = model

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

    def serialize(self):
        sdata = {}

        for model_id, model_instance  in self.data.items():
            sdata[model_id] = model_instance.serialize()

        return sdata
