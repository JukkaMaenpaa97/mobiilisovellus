from datamodels.datamodel import DataModel
from database.database import mysql, query

class UserModel(DataModel):
    def __init__(self):
        self.primary_column = "user_id"
        self.table = "users"
        self.fields = {
            "user_id":          self.field(),
            "user_type":        self.field(value = "1"),
            "user_email":       self.field(public = False),
            "user_password":    self.field(public = False),
            "user_name":        self.field(),
            "user_phone":       self.field(public = False),
            "user_address":     self.field(public = False),
            "user_postalcode":  self.field(public = False),
            "user_city":        self.field(public = False),
            "user_company_name":self.field(),
            "user_company_id":  self.field()
        }

        self.computed_fields = {
            "user_type_string": self.computedField(parent="user_type", handler=self.computeUserType),
            "user_total_service_count": self.computedField(parent="user_id", handler=self.getServiceCount)
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

    def getServiceCount(self, value):
        service_count = query("SELECT COUNT(service_id) FROM services WHERE service_provider_id=%(user_id)s", {"user_id": value}, True)
        return service_count['COUNT(service_id)']
