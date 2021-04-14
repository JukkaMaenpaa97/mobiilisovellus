from flask_restful import Resource
from flask import request
from database.database import mysql,query
from security.auth import Auth
import hashlib
from datamodels.datamodel import DataModel
import time


class Api(DataModel):
      def __init__(self): 

        self.primary_column = "session_id" 

        self.table = "sessions" 

        self.fields = {
            "session_id":     self.field(),
            "session_apikey":   self.field(),
            "session_user_id": self.field(),
            "session_created": self.field(),
            "session_expires": self.field()
            } 

     

class Login(Resource):
      def post(self):
            getapi = Api()
            time_created=""
            time_expires=""
            data = request.json
            credentials = {"user_email": data.get('user_email'), "user_password": hashlib.sha256(data.get('user_password').encode('utf-8')).hexdigest() }
            crendentialcheck = query("SELECT user_email, user_password FROM users WHERE user_email = %(user_email)s AND user_password = %(user_password)s", credentials)
            getid = query("SELECT user_id FROM users WHERE user_email = %(user_email)s AND user_password = %(user_password)s", credentials,True)
            try:
                  apikey = Auth.createApiKey()
                  #unix integer timestamps for now and expiring date
                  time_created = int(time.time())
                  time_expires = time_created + 172800
                  getapi.set("session_user_id", getid["user_id"])
                  getapi.set("session_apikey", apikey)
                  getapi.set("session_created",time_created)
                  getapi.set("session_expires",time_expires)
                  getapi.create()
                  return {"message": "login_success","apikey": apikey}

            except Exception:
                  return {"error": "100","message": "invalid credentials"}
                     
            
      

      
        
    

        
                        
        
        


         
        
