import requests

api_point = 'http://127.0.0.1:5000/register'
post_data = {
    'user_type': '1',
    'user_name': 'Testimies',
    'user_password': '1234',
    'user_email': 'testi@testi.com',
    'user_phone': '0451234567',
    'user_company_id': '',
    'user_company_name': ''
}

r = requests.post(api_point, data = post_data)

print(r.text)
