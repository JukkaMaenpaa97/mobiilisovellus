from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.categorymodel import CategoryModel
from flask import request
from validate.postvalidator import PostValidator



class Categories(Resource):
                                                         
        def post(self):
                categorymodel = CategoryModel()
                data = request.form
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
                        new_category = CategoryModel()
                        new_category.set("category_name", validator.get("category_name"))
                        new_category.set("category_description", validator.get("category_description"))
                        new_category.create()
                        return{"message": "Kategorian luonti onnistui"}, 200
                else:
                        return{"message": "Kategorian luonti epäonnistui"}, 400

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



            
