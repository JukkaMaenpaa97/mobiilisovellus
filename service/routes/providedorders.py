from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.ordermodel import OrderModel
from flask import request
from security.auth import Auth
from validate.postvalidator import PostValidator
import time



class ProvidedOrders(Resource):
        
      
        def timeStamp(self):
                order_created =""
                order_created = int(time.time())
                return order_created

               
        
        def get(self):
                ordermodel = OrderModel()
                orderlist = DataModelList(OrderModel)
                current_user = Auth.checkApiKey()
                if not current_user:
                    return Auth.unauthorizedResponse()
                user_id = current_user.get("user_id")
                if(orderlist.load("SELECT " + ordermodel.getFields()+ " FROM " + ordermodel.getTable()+" WHERE order_service_provider_id=%(user_id)s",{"user_id": user_id})):
                   if orderlist.count() > 0:
                       orderlist.computeAll()
                       return{"message": "Tilausten listaus",
                                   "count": orderlist.count(),
                                   "data": orderlist.serialize()},200
                   else:
                        return{"message": "Tilausten listaus epäonnistui"}, 404
                        
