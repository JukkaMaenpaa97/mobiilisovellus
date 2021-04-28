from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.ordermodel import OrderModel
from flask import request
from validate.postvalidator import PostValidator
from security.auth import Auth
import time



class Order(Resource):
    
    def get (self,id):
            current_user = Auth.checkApiKey()
            if not current_user:
                return Auth.unauthorizedResponse()
            order = OrderModel()
            if order.load(id):
                if current_user.get("user_id") == order.get("order_sender_id"):
                    return {"data": [order.serialize()]}, 200
                else:
                    return{"message": "ei oikeuksia tähän tilaukseen"}
            else:
                return {"message": "Tilausta ei löytynyt"}, 404
            
            
    
    def put(self,id):
        current_user = Auth.checkApiKey()
        if not current_user:
            return Auth.unauthorizedResponse()
        ordermodel = OrderModel()
        order = Order()
        ordermodel.load(id)
        validator = PostValidator()
        validator.postData(request.json)
        validator.addField("order_status", validate=['not_empty'])
        validator.addField("order_comments", validate=['not_empty'])

        if validator.validate():
            if validator.get("order_status") == 2:
                ordermodel.set("order_status", validator.get("order_status"))
                ordermodel.set("order_comments", validator.get("order_comments"))

            elif validator.get("order_status") == 3:
                ordermodel.set("order_status", validator.get("order_status"))
                order_finished = order.timeStamp()
                ordermodel.set("order_finished", order_finished)
                ordermodel.set("order_comments", validator.get("order_comments"))


            elif validator.get("order_status") == 4:
                ordermodel.set("order_status", validator.get("order_status"))
                order_cancelled = order.timeStamp()
                ordermodel.set("order_cancelled", order_cancelled)
                ordermodel.set("order_comments", validator.get("order_comments"))


        if ordermodel.update():
            return{"message": "Tilauksen päivitys onnistui."},200
        else:
            return{"message": "Tilausta ei muutettu. Tapahtui virhe tai tiedot ovat jo ajan tasalla."},400
                
            
                
                
                
                
                
                
                
                
            
        
    
