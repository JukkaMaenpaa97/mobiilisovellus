from flask_restful import Resource
from database import mysql, query
from auth import Auth

class Register(Resource):
    def put(self):
        return {"message": "register_success"}
