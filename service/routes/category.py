from flask_restful import Resource
from flask import request
from database.database import mysql,query
from security.auth import Auth
from datamodels.categorymodel import CategoryModel
from datamodels.usermodel import UserModel
from validate.postvalidator import PostValidator






class Category(Resource):

    def get(self,id):
        categorymodel = CategoryModel()
        if categorymodel.load(id):
            return {
                "message": "Kategorian tiedot",
                "data": [categorymodel.serialize()]
            }, 200
        else:
            return {"message": "Kategoriaa ei löytynyt"}, 404




    def put(self,id):

        # checking if user is logged in
        current_user = Auth.checkApiKey()
        if not current_user:
            return Auth.unauthorizedResponse()

        # if user is not an admin, don't allow modification
        if current_user.get("user_type") != 3:
            return Auth.unauthorizedResponse()

        categoryupdate = CategoryModel()
        if categoryupdate.load(id):
                data = request.json
                validator = PostValidator()
                validator.postData(data)
                validator.addField(
                "category_name",
                validate = ["not_empty"]
                )

                validator.addField(
                "category_description",
                validate = ["not_empty"])

                validation_result = validator.validate()
                if validation_result:
                        categoryupdate.set("category_name", validator.get("category_name"))
                        categoryupdate.set("category_description", validator.get("category_description"))
                        categoryupdate.update()
                        return{"message": "Kategorian päivitys onnistui"}, 200
                else:
                        return{"message": "Virheelliset tiedot, päivitys epäonnistui"}, 400

        else:
                return{"message": "Kategorian päivitys epäonnistui"}, 400
