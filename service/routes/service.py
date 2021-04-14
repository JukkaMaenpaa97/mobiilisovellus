# kaunista

from flask_restful import Resource
from flask import request
from datamodels.servicemodel import ServiceModel
from datamodels.categorymodel import CategoryModel
from security.auth import Auth
from validate.postvalidator import PostValidator

class Service(Resource):
    def get(self, id):
        servicemodel = ServiceModel()

        if servicemodel.load(id):
            return {"data": [servicemodel.serialize()]}, 200
        else:
            return {"message": "Palvelua ei löytynyt"}, 404

    def put(self, id):
        current_user = Auth.checkApiKey()
        if not current_user:
            return Auth.unauthorizedResponse()

        validator = PostValidator()
        validator.postData(request.json)

        validator.addField("service_category", validate=['not_empty'])
        validator.addField("service_type", type="integer", validate=['not_empty'], allowed=[1,2])
        validator.addField("service_title", validate=['not_empty'])
        validator.addField("service_description", validate=['not_empty'])
        validator.addField("service_price_type", type="integer", validate=['not_empty'], allowed=[1,2])
        validator.addField("service_price", type="double", validate=['not_empty'])
        validator.addField("service_availability", validate=['not_empty'])

        # If validator doens't pass, return an error message
        if not validator.validate():
            return {
                "error": "300",
                "message": "Palvelun päivitys epäonnistui.",
                "invalid_fields": validator.getInvalidFields(),
                "messages": validator.getInvalidMessages()
            }, 400

        # Before creation check if the category exists
        category = CategoryModel()
        if not category.load(validator.get("service_category")):
            return {
                "error": "300",
                "message": "Palvelun kategoria virheellinen.",
                "invalid_fields": ['service_category']
            }

        # Trying to load the service
        service = ServiceModel()
        if service.load(id):
            if service.get("service_provider_id") == current_user.get("user_id") or current_user.get("user_type") == "3":
                service.set("service_category", validator.get("service_category"))
                service.set("service_type", validator.get("service_type"))
                service.set("service_title", validator.get("service_title"))
                service.set("service_description", validator.get("service_description"))
                service.set("service_price_type", validator.get("service_price_type"))
                service.set("service_price", validator.get("service_price"))
                service.set("service_availability", validator.get("service_availability"))

                if service.update():
                    return {
                        "message": "Palvelu päivitetty onnistuneesti"
                    }, 200
                else:
                    return {
                        "message": "Palvelua ei päivitetty tai annetut tiedot olivat samat."
                    }, 200
            else:
                return Auth.unauthorizedResponse()

        else:
            return {
                "message": "Palvelua ei löytynyt"
            }, 404

    def delete(self, id):
        current_user = Auth.checkApiKey()
        if not current_user:
            return Auth.unauthorizedResponse()

        # Trying to load the service
        service = ServiceModel()
        if service.load(id):
            if service.get("service_provider_id") == current_user.get("user_id") or current_user.get("user_type") == "3":
                if service.delete():
                    return {
                        "message": "Palvelu poistettu onnistuneesti"
                    }, 200
                else:
                    return {
                        "message": "Tapahtui odottamaton virhe"
                    }, 500
            else:
                return Auth.unauthorizedResponse()

        else:
            return {
                "message": "Palvelua ei löytynyt"
            }, 404
