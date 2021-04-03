from flask_restful import Resource
from datamodels.datamodellist import DataModelList
from datamodels.categorymodel import CategoryModel

class Categories(Resource):
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
