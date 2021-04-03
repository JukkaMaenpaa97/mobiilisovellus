from flask import request
from flask_restful import Resource
from database.database import mysql, query
from security.auth import Auth
import uuid
import hashlib

class Register(Resource):
    def post(self):
        # collecting post data to dict
        data = request.form
        if int(data.get('user_type')) == 1:
            user = {
                "user_id":          str(uuid.uuid4()),
                "user_type":        int(data.get('user_type')),
                "user_name":        data.get('user_name'),
                "user_password":    hashlib.sha256(data.get('user_password').encode('utf-8')).hexdigest(),
                "user_email":       data.get('user_email'),
                "user_phone":       data.get('user_phone'),
            }
        elif int(data.get('user_type')) == 2:
            user = {
                "user_id":          str(uuid.uuid4()),
                "user_type":        int(data.get('user_type')),
                "user_name":        data.get('user_name'),
                "user_password":    hashlib.sha256(data.get('user_password').encode('utf-8')).hexdigest(),
                "user_email":       data.get('user_email'),
                "user_phone":       data.get('user_phone'),
                "user_company_id":  data.get('user_company_id'),
                "user_company_name":data.get('user_company_name')
            }

        # first checking if usertype is not set
        if user['user_type'] == '' or user['user_type'] == None:
            return self.failure({"user_type": "invalid"})

        # checking if user type is valid
        if user['user_type'] == 1 or user['user_type'] == 2:
            failure_flag = False
            invalid_fields = {}

            # looping all fields, push invalid to dict if no value is set
            for key, value in user.items():
                if value == None or value == '':
                    invalid_fields[key] = "invalid"
                    failure_flag = True

            if failure_flag:
                return self.failure(invalid_fields)
            else:
                # everything was cool, let's add a new user to database
                if user['user_type'] == 1:
                    query("INSERT INTO users (user_id, user_type, user_email, user_password, user_name, user_phone) VALUES(%(user_id)s, %(user_type)s, %(user_email)s, %(user_password)s, %(user_name)s, %(user_phone)s)", user)
                elif user['user_type'] == 2:
                    query("INSERT INTO users (user_id, user_type, user_email, user_password, user_name, user_phone, user_company_name, user_company_id) VALUES(%(user_id)s, %(user_type)s, %(user_email)s, %(user_password)s, %(user_name)s, %(user_phone)s, %(user_company_name)s, %(user_company_id)s)", user)
                return self.success()

        else:
            return self.failure({"user_type": "invalid"})

    def success(self):
        return {"message": "Rekisteröinti onnistui."}, 200

    def failure(self, invalid_fields):
        return {
            "error": "110",
            "message": "Rekisteröinti epäonnistui.",
            "invalid_fields": invalid_fields
        }, 400
