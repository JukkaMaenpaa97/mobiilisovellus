from flask_restful import Resource
from datamodels.ratingmodel import RatingModel
from flask import request
from validate.postvalidator import PostValidator
from security.auth import Auth
from routes.order import Order
import time


class Rating(Resource):

    def post(self,id):

        current_user = Auth.checkApiKey()
        if not current_user:
            return Auth.unauthorizedResponse()
        else:
            rating = RatingModel()
            validator = PostValidator()
            order = Order()
            validator.postData(request.json)
            data = request.json
            validator.addField("rating_grade", type="integer", validate=['not empty'], allowed=[1,2,3,4,5])
            validator.addField("rating_message", validate=['not empty'])
        if validator.validate():

            date = order.timeStamp()
            rating.set("rating_grade", validator.get("rating_grade"))
            rating.set("rating_sender_id", current_user.get("user_id"))
            rating.set("rating_service_provider_id", id)
            rating.set("rating_message", validator.get("rating_message"))
            rating.set("rating_datetime", date)

            if rating.create():
                return{"message": "Arvostelun jättäminen onnistui"},200
            else:
                return{"message": "Arvosten jättäminen epäonnistui"},400
        else:
            return{"message": "Virheelliset tiedot"}, 400
