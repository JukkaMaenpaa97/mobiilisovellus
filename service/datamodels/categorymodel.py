from datamodels.datamodel import DataModel

class CategoryModel(DataModel):

    def __init__(self):
        self.primary_column = "category_id"
        self.table = "categories"
        self.fields = {
            "category_id": self.field(),
            "category_name": self.field(),
            "category_description": self.field()
        }
