from database.database import mysql
import random, string

key_length = 32

class Auth():
    

    # checks the key from apikey header,
    # returns the id of the key owner or false on failure
    @staticmethod
    def checkApiKey(key):
        return False

    # creates a new apikey in succesfull authentication
    # with username and password, returns the new apikey as string
    @staticmethod
    def createApiKey():
        characters = string.ascii_letters + string.digits
        apilist = []
        apikey = ""
        for x in range(key_length):
            apilist.append(random.choice(characters))
            apikey =''.join(apilist)
        
        
        return apikey
