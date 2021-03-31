from flask import request
from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.servicemodel import ServiceModel

class Services(Resource):
    def get(self):
        servicemodel = ServiceModel()
        servicelist = DataModelList(ServiceModel)

        user_id = request.args.get('user_id')
        category_id = request.args.get('category_id')        

        if user_id == None and category_id == None:
            servicelist.load("SELECT "+servicemodel.getFields()+" FROM "+servicemodel.getTable())

        elif user_id != None and category_id != None:
            servicelist.load("SELECT "+servicemodel.getFields()+" FROM "+servicemodel.getTable()+" WHERE service_provider_id=%(user_id)s AND service_category=%(category_id)s",
                {
                    "user_id": user_id,
                    "category_id": category_id
                }
            )

        elif user_id != None:
            servicelist.load("SELECT "+servicemodel.getFields()+" FROM "+servicemodel.getTable()+" WHERE service_provider_id=%(user_id)s", {"user_id": user_id})

        elif category_id != None:
            servicelist.load("SELECT "+servicemodel.getFields()+" FROM "+servicemodel.getTable()+" WHERE service_category=%(category_id)s", {"category_id": category_id})

        if servicelist.count() > 0:
            servicelist.computeAll()
            return {
                "message": "Palveluiden listaus",
                "count": servicelist.count(),
                "data": servicelist.serialize()
            }, 200
        else:
            return {"message": "Yhtään palvelua ei löytynyt"}, 404
