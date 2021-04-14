from flask import request
from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.servicemodel import ServiceModel
from datamodels.categorymodel import CategoryModel
from validate.postvalidator import PostValidator
from security.auth import Auth

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

    def post(self):
        current_user = Auth.checkApiKey()
        if not current_user:
            return Auth.unauthorizedResponse()

        validator = PostValidator()
        validator.postData(request.json)

        validator.addField("service_category", validate=['not_empty'])
        validator.addField("service_type", type="integer", validate=['not_empty'], allowed=[1,2])
        validator.addField("service_title", validate=['not_empty'])
        validator.addField("service_description", validate=['not_empty'])
        validator.addField("service_price_type", type="integer", validate=['not_empty'], allowed=[1, 2])
        validator.addField("service_price", type="double", validate=['not_empty'])
        validator.addField("service_availability", validate=['not_empty'])

        if validator.validate():

            # ennen luontia tarkistetaan onko kategoria olemassa
            category = CategoryModel()
            if category.load(validator.get("service_category")) == False:
                return {
                    "error": "300",
                    "message": "Palvelun kategoria virheellinen.",
                    "invalid_fields": ['service_category']
                }

            # luodaan uusi palvelu
            service = ServiceModel()
            service.set("service_category", validator.get("service_category"))
            service.set("service_provider_id", current_user.get("user_id"))
            service.set("service_type", validator.get("service_type"))
            service.set("service_title", validator.get("service_title"))
            service.set("service_description", validator.get("service_description"))
            service.set("service_price_type", validator.get("service_price_type"))
            service.set("service_price", validator.get("service_price"))
            service.set("service_availability", validator.get("service_availability"))

            if service.create():
                return {
                    "message": "Uuden palvelun luonti onnistui.",
                    "service_id": service.get("service_id")
                }, 200
            else:
                return {
                    "message": "Tapahtui odottamaton virhe.",
                }, 500
        else:
            return {
                "error": "300",
                "message": "Palvelun lisäys epäonnistui.",
                "invalid_fields": validator.getInvalidFields(),
                "messages": validator.getInvalidMessages()
            }, 400
