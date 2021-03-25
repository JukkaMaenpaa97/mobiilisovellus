from flask_restful import Resource
from database.database import mysql, query
from security.auth import Auth
from datamodels.usermodel import UserModel

class User(Resource):
    def get(self, id):
        usermodel = UserModel()
        if usermodel.load(id):
            return {"data": usermodel.serialize()}, 200
        else:
            return {"message": "Käyttäjää ei löytynyt"}, 404
