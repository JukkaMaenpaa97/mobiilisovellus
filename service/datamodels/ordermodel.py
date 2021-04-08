from datamodels.datamodel import DataModel

class OrderModel(DataModel):
    def __init__(self):


        self.primary_column = "order_id"

        self.table = "orders"

        self.fields ={
            "order_id": self.field(),
            "order_sender_id": self.field(),
            "order_receiver_id": self.field(),
            "order_status": self.field(),
            "order_comments": self.field(),
            "order_created": self.field(),
            "order_finished": self.field(),
            "order_cancelled": self.field()



            }
