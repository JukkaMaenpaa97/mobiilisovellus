from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.servicemodel import ServiceModel

class Services(Resource):
    def get(self):
        servicemodel = ServiceModel()
        servicelist = DataModelList(ServiceModel)

        if servicelist.load("SELECT "+servicemodel.getFields()+" FROM "+servicemodel.getTable()):
            servicelist.computeAll()
            return {"data": servicelist.serialize()}, 200
        else:
            return {"message": "Yhtään palvelua ei löytynyt"}, 404
