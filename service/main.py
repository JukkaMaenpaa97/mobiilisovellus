# core modules
from flask import Flask
from flask_restful import Api, Resource
from flask_mysqldb import MySQL
from config import api_config
from database.database import mysql

# initializing app
app = Flask(__name__)

app.config['MYSQL_HOST'] = api_config['DB_HOST']
app.config['MYSQL_USER'] = api_config['DB_USER']
app.config['MYSQL_PASSWORD'] = api_config['DB_PASSWORD']
app.config['MYSQL_DB'] = api_config['DB_DB']
app.config['MYSQL_CURSORCLASS'] = 'DictCursor'

api = Api(app)
mysql.init_app(app)

# api modules
from routes.user import User
from login import Login
from routes.register import Register
from routes.services import Services

# default route index
class Index(Resource):
    def get(self):
        return {"message": "Jobster API 0.1 beta"}

api.add_resource(Index, "/")
api.add_resource(User, "/user/<id>")
api.add_resource(Login, "/login")
api.add_resource(Register, "/register")

api.add_resource(Services, "/services/<id>")

def main():
    app.run(debug=True)

if __name__ == '__main__':
    main()
