from datamodels.datamodel import DataModel

class UserModel(DataModel):
    def __init__(self):
        self.id = None
        self.primary_column = "user_id"
        self.table = "users"
        self.fields = {
            "user_id":      self.field(),
            "user_type":    self.field(value = "1"),
            "user_email":   self.field(),
            "user_password": self.field(public = False),
            "user_name":    self.field(),
            "user_phone":   self.field(),
            "user_address": self.field(),
            "user_postalcode": self.field(),
            "user_city":    self.field(),
            "user_company_name": self.field(),
            "user_company_id": self.field()
        }
