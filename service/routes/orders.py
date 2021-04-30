from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.ordermodel import OrderModel
from datamodels.servicemodel import ServiceModel
from flask import request
from security.auth import Auth
from validate.postvalidator import PostValidator
import time





class Orders(Resource):
        
        def post(self):
            current_user = Auth.checkApiKey()
            if not current_user:
                    return Auth.unauthorizedResponse()
            ordermodel = OrderModel()
            servicemodel = ServiceModel()
            data = request.json
            validator = PostValidator()
            validator.postData(data)
            validator.addField("order_comments", validate=['not_empty'])
            validator.addField("order_service_id", validate=['not_empty'])
            order = Orders()
            order_created = order.timeStamp()
            if validator.validate():
                servicemodel.load(validator.get("order_service_id"))
                ordermodel.set("order_service_id", validator.get("order_service_id"))
                ordermodel.set("order_sender_id",current_user.get("user_id"))
                ordermodel.set("order_status", 1)
                ordermodel.set("order_comments",validator.get("order_comments"))
                ordermodel.set("order_service_provider_id", servicemodel.get("service_provider_id"))
                ordermodel.set("order_created", order_created)
                if ordermodel.create():
                    return{"message": "Tilauksen luominen onnistui"}, 200
                else:
                    return{"message": "tilauksen luominen epäonnistui"},400
            else:
                    return{"message": "Virheelliset tiedot.",
                        "invalid_fields": validator.getInvalidFields(),
                        "messages": validator.getInvalidMessages() },400
                

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
                #user_type = current_user.get("user_type")
                if(orderlist.load("SELECT " + ordermodel.getFields()+ " FROM " + ordermodel.getTable()+" WHERE order_sender_id=%(user_id)s",{"user_id": user_id})):
                        orderlist.computeAll()
                        return{"message": "Tilausten listaus",
                                   "count": orderlist.count(),
                                   "data": orderlist.serialize()},200
                else:
                        return{"message": "Tilausten listaus epäonnistui"}, 404


