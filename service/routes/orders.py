from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.ordermodel import OrderModel
from flask import request
from security.auth import Auth




class Orders(Resource):
    
        
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
            

