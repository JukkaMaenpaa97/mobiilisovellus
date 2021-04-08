from flask import request
from flask_restful import Resource
from datamodels.usermodel import UserModel
from datamodels.datamodellist import DataModelList
from database.database import mysql, query

class Providers(Resource):
    def get(self):
        usermodel = UserModel()
        userlist = DataModelList(UserModel)
        category_id = request.args.get('category_id')

        if category_id == None:
            userlist.load(
                "SELECT "+usermodel.getFields()+" FROM "+usermodel.getTable()+" WHERE user_type=%(usertype)s",
                {"usertype": 2}
            )
        else:
            userlist.load(
                "SELECT "+usermodel.getFields()+" FROM "+usermodel.getTable()+" INNER JOIN services ON services.service_provider_id=users.user_id AND services.service_category=%(category_id)s",
                {"category_id": category_id}
            )

            category_name = query("SELECT category_name FROM categories WHERE category_id=%(category_id)s", {"category_id": category_id})[0]['category_name']


        if userlist.count() > 0:
            userlist.computeAll()
            return {
                "message": "Palveluntarjoajien listaus",
                "category": category_id,
                "category_name": category_name,
                "count": userlist.count(),
                "data": userlist.serialize()
            }, 200
        else:
            return {"message": "Yhtään palveluntarjoajaa ei löytynyt"}, 404
