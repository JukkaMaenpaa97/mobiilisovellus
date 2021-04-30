from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.messagemodel import MessageModel
from flask import request
from validate.postvalidator import PostValidator
from security.auth import Auth
import time



class Message(Resource):
    def timeStamp(self):
        message_created=""
        message_created = int(time.time())
        return message_created

    def post(self,id):

        current_user = Auth.checkApiKey()
        if not current_user:
            return Auth.unauthorizedResponse()
        else:
            message = MessageModel()
            data = request.json
            validator = PostValidator()
            validator.postData(data)
            validator.addField("message_type", type = "integer", validate=['not empty'])
            validator.addField("message_content", validate=['not_empty'])
            timestamp = Message()
            message_created = timestamp.timeStamp()
        if validator.validate():
    

            if validator.get("message_type") == 1:
                message.set("message_sender_id", current_user.get("user_id"))
                message.set("message_service_provider_id", id)
                message.set("message_content", validator.get("message_content"))
                message.set("message_datetime", message_created)
                message.set("message_type", validator.get("message_type"))
                if message.create():
                    return{"message": "Viestin lähetys onnistui"},200
                else:
                    return{"message": "Viestin lähetys epäonnistui"},400

            else:
                message.set("message_sender_id", current_user.get("user_id"))
                message.set("message_service_provider_id", id)
                message.set("message_content", validator.get("message_content"))
                message.set("message_datetime", message_created)
                message.set("message_type", validator.get("message_type"))
                if message.create():
                    return{"message": "Puhelu kirjattu lokiin"},200
                else:
                    return{"message": "Tapahtui virhe"},400
                
                
                
            
