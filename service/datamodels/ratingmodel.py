from datamodels.datamodel import DataModel

class RatingModel(DataModel):
    def __init__(self):


        self.primary_column = "rating_id"

        self.table = "ratings"

        self.fields ={
            "rating_id": self.field(),
            "rating_grade": self.field(),
            "rating_sender_id": self.field(),
            "rating_service_provider_id": self.field(),
            "rating_message": self.field(),
            "rating_datetime": self.field()
            }
