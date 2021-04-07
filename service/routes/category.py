from flask_restful import Resource
from flask import request
from database.database import mysql,query
from security.auth import Auth
from datamodels.categorymodel import CategoryModel



        

class Category(Resource):  
    def get(self,id):
        categorymodel = CategoryModel()
        if categorymodel.load(id):
            return{"data": [categorymodel.serialize()]}, 200
        else:
            return {"message": "Kategoriaa ei löytynyt"}, 404
        
        
        
        
        
        
