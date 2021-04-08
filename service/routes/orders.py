from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.ordermodel import OrderModel
from flask import request
from validate.postvalidator import PostValidator




class Orders(Resource):


    def get(self):
        ordermodel = OrderModel()
        orderlist = DataModelList(OrderModel)

        if(orderlist.load("SELECT "+ordermodel.getFields()+" FROM "+ordermodel.getTable())):
            orderlist.computeAll()
            return{ "message": "Tilausten listaus",
                    "count": orderlist.count(),
                    "data": orderlist.serialize()},200

        else:
            return{"message": "Tilauksia ei löytynyt"},404
        
