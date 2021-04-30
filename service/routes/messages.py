from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.messagemodel import MessageModel
from flask import request
from security.auth import Auth




class Messages(Resource):
    
        
    def get(self):
        messagemodel = MessageModel()
        messagelist = DataModelList(MessageModel)
        current_user = Auth.checkApiKey()
        if not current_user:
            return Auth.unauthorizedResponse()
        user_id = current_user.get("user_id")
        user_type = current_user.get("user_type")

        if user_type == 2:
            
            if(messagelist.load("SELECT " + messagemodel.getFields()+ " FROM " + messagemodel.getTable()+" WHERE message_service_provider_id=%(user_id)s",{"user_id": user_id})):
               if messagelist.count() > 0:
                   messagelist.computeAll()
                   return{"message": "Tilausten listaus",
                               "count": messagelist.count(),
                               "data": messagelist.serialize()},200
               else:
                    return{"message": "Viestien listaus epäonnistui"}, 404
        elif user_type == 1:
            if(messagelist.load("SELECT " + messagemodel.getFields()+ " FROM " + messagemodel.getTable()+" WHERE message_sender_id=%(user_id)s",{"user_id": user_id})):
               if messagelist.count() > 0:
                   messagelist.computeAll()
                   return{"message": "Viestien listaus",
                               "count": messagelist.count(),
                               "data": messagelist.serialize()},200
               else:
                    return{"message": "Viestien listaus epäonnistui"}, 404
            

