from datamodels.datamodel import DataModel

class OrderModel(DataModel):
    def __init__(self):
        self.primary_column = "order_id"
        self.table = "orders"

        self.fields ={
            "order_id": self.field(),
            "order_sender_id": self.field(),
            "order_service_id": self.field(),
            "order_status": self.field(),
            "order_comments": self.field(),
            "order_created": self.field(),
            "order_finished": self.field(),
            "order_cancelled": self.field()
        }

        self.computed_fields = {
            "order_service_info": self.computedField(parent="order_service_id", handler=self.getService),
            "order_sender_info": self.computedField(parent="order_sender_id", handler=self.getSender)
        }

    def getService(self, value):
        service = ServiceModel()
        if service.load(value):
            return service.serialize()
        else:
            return None

    def getSender(self,value):
        sender = UserModel()
        if sender.load(value):
            return sender.serialize(include_private=["user_email", "user_phone", "user_address", "user_postalcode", "user_city"])
        else:
            return None

from datamodels.servicemodel import ServiceModel
from datamodels.usermodel import UserModel
