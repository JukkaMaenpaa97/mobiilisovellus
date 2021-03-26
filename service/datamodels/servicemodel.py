from datamodels.datamodel import DataModel

class ServiceModel(DataModel):
    def __init__(self):
        self.primary_column = "service_id"
        self.table = "services"
        self.fields = {
            "service_id": self.field(),
            "service_provider_id": self.field(),
            "service_category": self.field(),
            "service_type": self.field(),
            "service_title": self.field(),
            "service_description": self.field(),
            "service_price_type": self.field(),
            "service_price": self.field(),
            "service_avalability": self.field()
        }
