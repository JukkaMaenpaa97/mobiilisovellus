from flask_restful import Resource
from database import mysql, query
from auth import Auth
from hashlib import sha256

class Login(Resource):
    def get(email,password):
        #password to hash
        hashedpassword = hashlib.sha256(password.encode())
        credentials = query("SELECT user_email, user_password FROM Users WHERE user_email = %(user_email)s,
        {"user_email": email} AND  password = %(hashedpassword)s, {"user_password": hashedpassword}, False)

        #check if query returned rows
        if(credentials is None):
            return {"message": "invalid_credentials"}
        else:
            return {"message": "login_success",
            "apikey": Auth.createApiKey(1)}
        
    

        
                        
        
        


         
        
