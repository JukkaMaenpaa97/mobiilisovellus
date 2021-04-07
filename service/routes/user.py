from flask_restful import Resource
from database.database import mysql, query
from security.auth import Auth
from datamodels.usermodel import UserModel

class User(Resource):
    def get(self, id):
        if id == "me":
            current_user = Auth.checkApiKey()
            if not current_user:
                return Auth.unauthorizedResponse()
            return {
                "message": "Omat tiedot",
                "data": current_user.serialize(include_private=["user_email", "user_phone", "user_address", "user_postalcode", "user_city"])
            }, 200

        usermodel = UserModel()

        if usermodel.load(id):
            return {
                "message": "Toisen käyttäjän tiedot",
                "data": usermodel.serialize()
            }, 200
        else:
            return {"message": "Käyttäjää ei löytynyt"}, 404
