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
            "session_id":          self.field(),
            "session_apikey":   self.field(),
            "session_user_id": self.field(),
            "session_created": self.field(),
            "session_expires": self.field()
            } 

     

class Login(Resource):
      def post(self):
        testset = Api()
        time_created=""
        time_expires=""
        data = request.form
        credentials = {"user_email": data.get('user_email'), "user_password": hashlib.sha256(data.get('user_password').encode('utf-8')).hexdigest() }
        #tosend{"user_id":, "session_apikey":}
        crendentialcheck = query("SELECT user_email, user_password FROM users WHERE user_email = %(user_email)s AND user_password = %(user_password)s", credentials)
        getid = query("SELECT user_id FROM users WHERE user_email = %(user_email)s AND user_password = %(user_password)s", credentials,True)
        print(getid)
        #check if query returned rows
        if(credentials is None):
            return {"message": "invalid_credentials"}
        else:
            apikey = Auth.createApiKey()
            #unix integer timestamps for now and expiring date
            time_created = int(time.time())
            time_expires = time_created + 172800
            testset.set("session_user_id", getid["user_id"])
            testset.set("session_apikey", apikey)
            testset.set("session_created",time_created)
            testset.set("session_expires",time_expires)
            testset.create()
            return {"message": "login_success"}
      

      
        
    

        
                        
        
        


         
        