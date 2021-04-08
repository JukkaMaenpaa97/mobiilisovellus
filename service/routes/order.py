from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.ordermodel import OrderModel
from flask import request
from validate.postvalidator import PostValidator
from security.auth import Auth



class Order(Resource):

    def post(self):
            current_user = Auth.checkApiKey()
            if not current_user:
                return Auth.unauthourizedResponse()
            else:
                #toteutus
                
                
            
        
    
