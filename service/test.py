import requests

# LOGIN  TEST
api_point = 'http://mobiilisovellus.therozor.com:5000/login'

post_data = {
    "user_email": "juho@jtaskila.fi",
    "user_password": "1234"
}

headers = {
    "Content-Type": "application/json",
    "apikey": ""
}

r1 = requests.post(api_point, json=post_data, headers=headers)
print("===============================================================================")
print("LOGIN")
print(r1.status_code)
print(r1.text)
headers['apikey'] = r1.json()['apikey']

# / LOGIN TEST

# OWN INFO TEST
r3 = requests.get("http://mobiilisovellus.therozor.com:5000/user/me", headers=headers)
print("===============================================================================")
print("GET OWN INFO")
print(r3.status_code)
print(r3.text)
# / OWN INFO TEST


# UPDATE INFO TEST
data = r3.json()['data'][0]
user = {
    "user_name":  data['user_name'],
    "user_phone": data['user_phone'],
    "user_address": data['user_address'],
    "user_postalcode": data['user_postalcode'],
    "user_city": data['user_city'],
    "user_password": "",
    "user_password_again": "",
    "user_company_name": data['user_company_name'],
    "user_company_id": data['user_company_id']
}

r2 = requests.put("http://mobiilisovellus.therozor.com:5000/user/me", json=user, headers=headers)
print("===============================================================================")
print("UPDATE OWN INFO")
print(r2.status_code)
print(r2.text)

# / UPDATE INFO TEST

# GET SERVICES
r = requests.get("http://mobiilisovellus.therozor.com:5000/services", headers=headers)
print("===============================================================================")
print("SERVICES")
print(r.status_code)
print(r.text)

# ORDERS TEST
r = requests.get("http://mobiilisovellus.therozor.com:5000/orders", headers=headers)
print("===============================================================================")
print("GET OWN ORDERS")
print(r.status_code)
print(r.text)
# /ORDERS

# GET ONE ORDER
order = r.json()['data'][0]['order_id']
r = requests.get("http://mobiilisovellus.therozor.com:5000/order/"+order, headers=headers)
print("===============================================================================")
print("GET ONE ORDER")
print(r.status_code)
print(r.text)

# LOGOUT TEST
r4 = requests.delete("http://mobiilisovellus.therozor.com:5000/login", headers=headers)
print("===============================================================================")
print("LOGOUT")
print(r4.status_code)
print(r4.text)
# /LOGOUT TEST
