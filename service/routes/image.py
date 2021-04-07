from flask_restful import Resource
from flask import send_file
from datamodels.imagemodel import ImageModel

class Image(Resource):
    def get(self, id):
        image = ImageModel()
        if image.load(id):
            return send_file("static/"+image.get("image_url"), mimetype="image/jpg")
        else:
            return {"message": "Kuvaa ei löytynyt"}, 404
