from datamodels.datamodel import DataModel
from datamodels.usermodel import UserModel
from datamodels.categorymodel import CategoryModel

class ServiceModel(DataModel):
    def __init__(self):
        self.primary_column = "service_id"
        self.table = "services"
        self.fields = {
            "service_id":           self.field(),
            "service_provider_id":  self.field(),
            "service_category":     self.field(),
            "service_type":         self.field(),
            "service_title":        self.field(),
            "service_description":  self.field(),
            "service_price_type":   self.field(),
            "service_price":        self.field(),
            "service_availability":  self.field()
        }

        self.computed_fields = {
            "service_provider_name": self.computedField(parent="service_provider_id", handler=self.getServiceProviderName),
            "service_category_name": self.computedField(parent="service_category", handler=self.getServiceCategoryName)
        }


    def getServiceProviderName(self, value):
        provider = UserModel()
        if provider.load(value):
            return provider.get("user_name")
        else:
            return None

    def getServiceCategoryName(self, value):
        category = CategoryModel()
        if category.load(value):
            return category.get("category_name")
        else:
            return None
