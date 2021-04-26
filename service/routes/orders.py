from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.ordermodel import OrderModel
from flask import request
from security.auth import Auth
from validate.postvalidator import PostValidator
import time





class Orders(Resource):

        def timeStamp(self):
            order_created =""
            order_created = int(time.time())
            return order_created
        

        def post(self):
            current_user = Auth.checkApiKey()
            if not current_user:
                return Auth.unauthorizedResponse()
            else:
                ordermodel = OrderModel()
                data = request.json
                validator = PostValidator()
                validator.postData(data)
                validator.addField("order_comments", validate=['not empty'])
                validator.addField("order_service_id", validate=['not empty'])
                order = Orders()
                order_created = order.timeStamp()
            if validator.validate():
                ordermodel.set("order_service_id", validator.get("order_service_id"))
                ordermodel.set("order_sender_id",current_user.get("user_id"))
                ordermodel.set("order_status", 1)
                ordermodel.set("order_comments",validator.get("order_comments"))
                ordermodel.set("order_created", order_created)
                if ordermodel.create():
                    return{"message": "Tilauksen luominen onnistui"}, 200
                else:
                    return{"message": "tilauksen luominen epäonnistui"},400
            else:
                    return{"message": "Virheelliset tiedot." },400

        
        
            
        
        def get(self):
            ordermodel = OrderModel()
            orderlist = DataModelList(OrderModel)
            current_user = Auth.checkApiKey()
            if not current_user:
                return Auth.unauthorizedResponse()
            user_id = current_user.get("user_id")
            user_type = current_user.get("user_type")

            if user_type == 2:
                
                if(orderlist.load("SELECT " + ordermodel.getFields()+ " FROM " + ordermodel.getTable()+" WHERE order_service_provider_id=%(user_id)s",{"user_id": user_id})):
                   if orderlist.count() > 0:
                       orderlist.computeAll()
                       return{"message": "Tilausten listaus",
                                   "count": orderlist.count(),
                                   "data": orderlist.serialize()},200
                   else:
                        return{"message": "Tilausten listaus epäonnistui"}, 404
            elif user_type == 1:
                if(orderlist.load("SELECT " + ordermodel.getFields()+ " FROM " + ordermodel.getTable()+" WHERE order_sender_id=%(user_id)s",{"user_id": user_id})):
                   if orderlist.count() > 0:
                       orderlist.computeAll()
                       return{"message": "Tilausten listaus",
                                   "count": orderlist.count(),
                                   "data": orderlist.serialize()},200
                   else:
                        return{"message": "Tilausten listaus epäonnistui"}, 404
            

