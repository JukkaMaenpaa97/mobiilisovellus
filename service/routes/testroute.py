from flask_restful import Resource
from flask import request
from database.database import mysql,query
from security.auth import Auth

class TestRoute(Resource):
    def get(self):
        pass

    def post(self):
        pass

    def put(self):
        pass

    def delete(self):
        pass 
