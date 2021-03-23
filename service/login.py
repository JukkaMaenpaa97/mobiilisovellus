from flask_restful import Resource
from database import mysql, query
from auth import Auth

class Login(Resource):
    def get(self):
        return {"message": "login_success",
        "apikey": Auth.createApiKey(1)}
