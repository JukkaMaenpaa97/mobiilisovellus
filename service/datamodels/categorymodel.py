from datamodels.datamodel import DataModel
from datamodels.datamodellist import DataModelList

class CategoryModel(DataModel):

    def __init__(self):
        self.primary_column = "category_id"
        self.table = "categories"
        self.fields = {
            "category_id": self.field(),
            "category_name": self.field(),
            "category_description": self.field()
        }

        self.computed_fields = {
            "category_service_count": self.computedField(parent="category_id", handler=self.getServiceCount),
            "category_images": self.computedField(parent="category_id", handler=self.getCategoryImage)
        }

    # has to be rewritten to pure sql
    def getServiceCount(self, value):
        serviceModel = ServiceModel()
        serviceList = DataModelList(ServiceModel)

        if serviceList.load("SELECT "+serviceModel.getFields()+" FROM "+serviceModel.getTable()+" WHERE service_category = %(id)s", {"id": value}):
            return serviceList.count()
        else:
            return 0

    def getCategoryImage(self, value):
        imagemodel = ImageModel()
        imagelist = DataModelList(ImageModel)

        if imagelist.load("SELECT "+imagemodel.getFields()+" FROM "+imagemodel.getTable()+" WHERE image_owner_id = %(id)s", {"id": value}):
            return imagelist.serialize()
        else:
            return None

from datamodels.servicemodel import ServiceModel
from datamodels.imagemodel import ImageModel
