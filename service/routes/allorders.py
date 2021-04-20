from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.ordermodel import OrderModel
from flask import request
from validate.postvalidator import PostValidator
from security.auth import Auth





class AllOrders(Resource):


    def get(self):
        current_user = Auth.checkApiKey()
        if not current_user:
            return Auth.unauthorizedResponse
        ordermodel = OrderModel()
        orderlist = DataModelList(OrderModel)

        if(orderlist.load("SELECT "+ordermodel.getFields()+" FROM "+ordermodel.getTable())) and orderlist.count() > 0:

        #elif orderlist.count() > 0:

            orderlist.computeAll()
            return{ "message": "Tilausten listaus",
                    "count": orderlist.count(),
                    "data": orderlist.serialize()},200

        else:
            return{"message": "Tilauksia ei löytynyt"},404
        
