from database.database import mysql, query
from datamodels.usermodel import UserModel
from datamodels.sessionmodel import SessionModel

from flask import request
import random, string

class Auth():

    # checks the key from apikey header,
    # returns the id of the key owner or false on failure
    @staticmethod
    def checkApiKey():

        # checking the request headers for apikey
        apikey = request.headers.get('apikey')
        if apikey == None:
            return False

        # creating models
        user = UserModel()
        session = SessionModel()

        # checking the apikey and getting the corresponding user from database
        session_data = query("SELECT "+session.getFields()+" FROM "+session.getTable()+" WHERE session_apikey = %(apikey)s", {"apikey": apikey}, True)
        if session_data == None:
            return False
        session.populate(session_data)

        # trying to load the users data
        # if succesfull, then return the current user
        if user.load(session.get("session_user_id")):
            return user
        else:
            return False


    # creates a new apikey in succesfull authentication
    # with username and password, returns the new apikey as string
    @staticmethod
    def createApiKey():
        key_length = 32
        characters = string.ascii_letters + string.digits
        apilist = []
        apikey = ""
        for x in range(key_length):
            apilist.append(random.choice(characters))
            apikey =''.join(apilist)

        return apikey

    @staticmethod
    def unauthorizedResponse():
        return {"message": "Ei oikeuksia sisältöön"}, 401

    @staticmethod
    def getApiKey():
        apikey = request.headers.get('apikey')
        if apikey == None:
            return False
        return apikey
