from flask_restful import Resource
from flask import request

from datamodels.usermodel import UserModel
from datamodels.sessionmodel import SessionModel
from validate.postvalidator import PostValidator
from database.database import mysql, query

from security.auth import Auth
import hashlib
import time

class Login(Resource):

    # Login
    def post(self):
        validator = PostValidator()
        validator.postData(request.json)

        validator.addField("user_email", validate=['not_empty'])
        validator.addField("user_password", validate=['not_empty'])

        if validator.validate():
            #creating an instance of user so we can use it later
            usermodel = UserModel()

            #hashing the password #both to vars for consistency
            user_email = validator.get("user_email")
            user_password = hashlib.sha256(validator.get('user_password').encode('utf-8')).hexdigest()

            login_query = query("SELECT "+usermodel.getFields()+" FROM "+usermodel.getTable()+" WHERE user_email=%(user_email)s AND user_password=%(user_password)s",
                {
                    "user_email": user_email,
                    "user_password": user_password
                }, True
            )

            # if no match was found, return invalid message
            if login_query == None:
                return {
                    "error": "100",
                    "message": "Virheelliset kirjautumistiedot."
                }, 401
            else:
                apikey = Auth.createApiKey()
                time_created = int(time.time())
                time_expires = time_created + 172800

                usermodel.populate(login_query)

                sessionmodel = SessionModel()
                sessionmodel.set("session_apikey", apikey)
                sessionmodel.set("session_user_id", usermodel.get("user_id"))
                sessionmodel.set("session_created", time_created)
                sessionmodel.set("session_expires", time_expires)

                if sessionmodel.create():
                    return {
                        "message": "Kirjautuminen onnistui",
                        "apikey": apikey
                    }, 200

                else:
                    return {
                        "message": "Tapahtui odottamaton virhe. Yritä myöhemmin uudelleen."
                    }, 500


        else:
            return {
                "error": "300",
                "message": "Kirjautuminen epäonnistui.",
                "invalid_fields": validator.getInvalidFields(),
                "messages": validator.getInvalidMessages()
            }, 400

    # Logout
    def delete(self):
        sessionmodel = SessionModel()
        current_user = Auth.checkApiKey()
        if not current_user:
            return Auth.unauthorizedResponse()

        session_delete_query = query("DELETE FROM "+sessionmodel.getTable()+" WHERE session_user_id=%(user_id)s", {"user_id": current_user.get("user_id")})
        if session_delete_query != None:
            return {"message": "Uloskirjautuminen onnistui."}, 200
        else:
            return Auth.unauthorizedResponse()
