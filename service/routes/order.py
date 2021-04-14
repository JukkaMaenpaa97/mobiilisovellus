from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.ordermodel import OrderModel
from flask import request
from validate.postvalidator import PostValidator
from security.auth import Auth
import time



class Order(Resource):

    def timeStamp(self):
        order_created =""
        order_created = int(time.time())
        return order_created
        

    def post(self,id):
        
            current_user = Auth.checkApiKey()
            if not current_user:
                return Auth.unauthorizedResponse()
            else:
                ordermodel = OrderModel()
                data = request.json
                #validator = PostValidator()
                #validator.postData(request.json)
                #validator.addField("order_comments", validate=['not_empty'])
                order = Order()
                order_created = order.timeStamp()
                ordermodel.set("order_service_provider_id", id)
                ordermodel.set("order_sender_id",current_user.get("user_id"))
                ordermodel.set("order_status", 1)
                ordermodel.set("order_comments",data.get("order_comments"))
                ordermodel.set("order_created", order_created)
                ordermodel.create()
                return{"message": "onnistui"}
                
                
                
                

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
            #print(validator.get("order_status"))

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
                print("täällä")
                return{"message": "Tilauksen päivitys onnistui."}
            
            else:
                return{"message": "Tilausta ei muutettu. Tapahtui virhe tai tiedot ovat jo ajan tasalla."}
                    
                
                
                
                
                
                
                
                
                
            
        
    
