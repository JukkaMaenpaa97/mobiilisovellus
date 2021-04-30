from datamodels.datamodel import DataModel

class ImageModel(DataModel):
    def __init__(self):
        self.primary_column = "image_id"
        self.table = "image_resources"
        self.fields = {
            "image_id": self.field(),
            "image_owner_id": self.field(public = False),
            "image_url": self.field(public = False)
        }
