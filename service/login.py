from flask_restful import Resource
from database.database import mysql, query
from security.auth import Auth

class Login(Resource):
    def get(self):
        return {"message": "login_success",
        "apikey": Auth.createApiKey(1)}
