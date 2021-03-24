from flask_restful import Resource
from flask import request
from database.database import mysql,query
from security.auth import Auth
import hashlib

class Login(Resource):
      def post(email,password):
      #def post(self):   #password to hash
        data = request.form
        testdictionary = {"user_email": data.get('user_email'), "user_password": hashlib.sha256(data.get('user_password'))}
        credentials = query("SELECT (user_email, user_password) FROM Users WHERE user_email = %(user_email)s, AND  user_password = %(user_password)s", users)
        #check if query returned rows
        if(credentials is None):
            return {"message": "invalid_credentials"}
        else:
            return {"message": "login_success",
            "apikey": Auth.createApiKey(1)}
        
    

        
                        
        
        


         
        
