from datamodels.datamodel import DataModel

class UserModel(DataModel):
    def __init__(self):
        self.primary_column = "user_id"
        self.table = "users"
        self.fields = {
            "user_id":          self.field(),
            "user_type":        self.field(value = "1"),
            "user_email":       self.field(),
            "user_password":    self.field(public = False),
            "user_name":        self.field(),
            "user_phone":       self.field(),
            "user_address":     self.field(),
            "user_postalcode":  self.field(),
            "user_city":        self.field(),
            "user_company_name":self.field(),
            "user_company_id":  self.field()
        }

        self.computed_fields = {
            "user_type_string": self.computedField(parent="user_type", handler=self.computeUserType)
        }

    def formatEmail(self, value):
        return str(value)

    def computeUserType(self, value):
        if value == 1:
            return "Asiakas"
        elif value == 2:
            return "Yrityskäyttäjä"
        elif value == 3:
            return "Ylläpitäjä"
