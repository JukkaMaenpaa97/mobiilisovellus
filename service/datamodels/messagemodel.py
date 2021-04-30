from datamodels.datamodel import DataModel

class MessageModel(DataModel):
    def __init__(self):


        self.primary_column = "message_id"

        self.table = "messages"

        self.fields ={
            "message_id": self.field(),
            "message_sender_id": self.field(),
            "message_service_provider_id": self.field(),
            "message_content": self.field(),
            "message_datetime": self.field(),
            "message_type": self.field()


            }
