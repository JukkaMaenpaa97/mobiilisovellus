from flask_restful import Resource
from flask import request
from datamodels.datamodellist import DataModelList
from datamodels.categorymodel import CategoryModel
from datamodels.usermodel import UserModel
from security.auth import Auth
from validate.postvalidator import PostValidator

class Categories(Resource):
    def post(self):
        # checking if user is logged in
        current_user = Auth.checkApiKey()
        if not current_user:
            return Auth.unauthorizedResponse()

        # if user is not an admin, don't allow modification
        if current_user.get("user_type") != 3:
            return Auth.unauthorizedResponse()

        validator = PostValidator()
        validator.postData(request.json)
        validator.addField("category_name", validate = ["not_empty"])
        validator.addField("category_description", validate = ["not_empty"])

        if validator.validate():
            new_category = CategoryModel()
            new_category.set("category_name", validator.get("category_name"))
            new_category.set("category_description", validator.get("category_description"))

            if new_category.create():
                return {
                    "message": "Kategorian luonti onnistui"
                }, 200
            else:
                return {
                    "message": "Tuntematon virhe"
                }, 500
        else:
            return {
                "error": "300",
                "message": "Kategorian luonti epäonnistui.",
                "invalid_fields": validator.getInvalidFields(),
                "messages": validator.getInvalidMessages()
            }, 400

    def get(self):
        categorymodel = CategoryModel()
        categorylist = DataModelList(CategoryModel)

        if(categorylist.load("SELECT "+categorymodel.getFields()+" FROM "+categorymodel.getTable())):
            categorylist.computeAll()
            return {
                "message": "Kategorioiden listaus",
                "count": categorylist.count(),
                "data": categorylist.serialize()
            }, 200
        else:
            return {"message": "Yhtään kategoriaa ei löytynyt"}, 404
