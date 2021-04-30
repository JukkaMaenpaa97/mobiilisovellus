from flask import request
from flask_restful import Resource
from database.database import mysql, query
from validate.postvalidator import PostValidator
from datamodels.usermodel import UserModel
import hashlib

class Register(Resource):
    def post(self):
        # collecting post data to dict
        data = request.json

        validator = PostValidator()
        validator.postData(data)

        validator.addField("user_type", type="integer", validate=["not_empty"], allowed=[1,2])
        validator.addField("user_name", max_len=50, validate=["not_empty"])
        validator.addField("user_email", max_len=50, validate=["not_empty"])
        validator.addField("user_password", validate=["not_empty"])
        validator.addField("user_password_again", validate=["not_empty"])

        if validator.validate():
            if validator.get("user_password") == validator.get("user_password_again"):
                user_email_check = query("SELECT user_email FROM users WHERE user_email=%(email)s", {"email": validator.get("user_email")})
                if user_email_check != None:
                    return {
                        "error": "111",
                        "message": "Käyttäjätunnus on jo olemassa",
                        "invalid_fields": ["user_email"]
                    },400
                # validation passed, create a new user
                new_user = UserModel()
                new_user.set("user_type", validator.get("user_type"))
                new_user.set("user_name", validator.get("user_name"))
                new_user.set("user_email", validator.get("user_email"))

                user_password = hashlib.sha256(validator.get("user_password").encode("utf-8")).hexdigest()
                new_user.set("user_password", user_password)

                if new_user.create():
                    return {"message": "Rekisteröinti onnistui"}, 200
                else:
                    return {"message": "Virhe"}, 500
            else:
                return {
                    "error": "110",
                    "message": "Rekisteröinti epäonnistui. Salasanat eivät täsmää",
                    "invalid_fields": ["user_password", "user_password_again"]
                }, 400
        else:
            return {
                "error": "110",
                "message": "Rekisteröinti epäonnistui.",
                "invalid_fields": validator.getInvalidFields(),
                "messages": validator.getInvalidMessages()
            }, 400
