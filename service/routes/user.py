from flask_restful import Resource
from database.database import mysql, query
from security.auth import Auth

class User(Resource):
    def get(self, id):
        result = query("SELECT * FROM users WHERE user_id = %(user_id)s",
        {"user_id": id}, True)
        return {'data': result},200