from flask_restful import Resource
from flask import request

from security.auth import Auth
from datamodels.usermodel import UserModel

from validate.postvalidator import PostValidator
import hashlib
class User(Resource):

    # GET
    def get(self, id):
        if id == "me":
            current_user = Auth.checkApiKey()
            if not current_user:
                return Auth.unauthorizedResponse()
            return {
                "message": "Omat tiedot",
                "data": [current_user.serialize(include_private=["user_email", "user_phone", "user_address", "user_postalcode", "user_city"])]
            }, 200

        usermodel = UserModel()

        if usermodel.load(id):
            return {
                "message": "Toisen käyttäjän tiedot",
                "data": [usermodel.serialize(include_private=["user_email", "user_phone", "user_address", "user_postalcode", "user_city"])]
            }, 200
        else:
            return {"message": "Käyttäjää ei löytynyt"}, 404

    # PUT (update)
    def put(self, id):
        if id == "me":
            # checking and loading the current user.
            # NOTE: checkApiKey returns a full UserModel
            current_user = Auth.checkApiKey()
            if not current_user:
                return Auth.unauthorizedResponse()

            validator = PostValidator()
            validator.postData(request.json)

            validator.addField("user_name", validate=['not_empty'])
            validator.addField("user_phone", validate=['not_empty'])
            validator.addField("user_address", validate=['not_empty'])
            validator.addField("user_postalcode", validate=['not_empty'])
            validator.addField("user_city", validate=['not_empty'])
            validator.addField("user_password")
            validator.addField("user_password_again")
            validator.addField("user_company_name")
            validator.addField("user_company_id")

            if validator.validate():
                current_user.set("user_name", validator.get("user_name"))
                current_user.set("user_phone", validator.get("user_phone"))
                current_user.set("user_address", validator.get("user_address"))
                current_user.set("user_postalcode", validator.get("user_postalcode"))
                current_user.set("user_city", validator.get("user_city"))
                current_user.set("user_company_name", validator.get("user_company_name"))
                current_user.set("user_company_id", validator.get("user_company_id"))

                if validator.get("user_password") != "":
                    if validator.get("user_password") == validator.get("user_password_again"):
                        current_user.set("user_password", hashlib.sha256(validator.get("user_password").encode("utf-8")).hexdigest())
                    else:
                        return {
                            "error": "300",
                            "message": "Tietojen päivitys epäonnistui. Salasanat eivät täsmää.",
                            "invalid_fields": ["user_password", "user_password_again"]
                        }, 400

                if current_user.update():
                    return {
                        "message": "Käyttäjän päivitys onnistui."
                    }, 200
                else:
                    return {
                        "message": "Tietoja ei muutettu. Tapahtui virhe tai tiedot eivät vaatineet päivitystä."
                    }, 304
            else:
                return {
                    "error": "300",
                    "message": "Käyttäjän päivitys epäonnistui.",
                    "invalid_fields": validator.getInvalidFields(),
                    "messages": validator.getInvalidMessages()
                }, 400
        else:
            return Auth.unauthorizedResponse();
