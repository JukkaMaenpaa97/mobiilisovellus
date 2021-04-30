from datamodels.datamodel import DataModel 

class SessionModel(DataModel):
      def __init__(self):

        self.primary_column = "session_id"

        self.table = "sessions"

        self.fields = {
            "session_id":     self.field(),
            "session_apikey":   self.field(),
            "session_user_id": self.field(),
            "session_created": self.field(),
            "session_expires": self.field()
        }
