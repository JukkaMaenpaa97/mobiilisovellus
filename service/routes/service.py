# kaunista

from flask_restful import Resource
from datamodels.servicemodel import ServiceModel

class Service(Resource):
    def get(self, id):
        servicemodel = ServiceModel()

        if servicemodel.load(id):
            return {"data": servicemodel.serialize()}, 200
        else:
            return {"message": "Palvelua ei löytynyt"}, 404
