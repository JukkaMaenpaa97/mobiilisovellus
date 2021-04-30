import requests
import copy
# commands
# login -> username, password
# me -> own info
# logout -> logs out
# services -> returns a list of services
# service -> index
# categories
# add_category -> adds a new category
# providers -> loads list of providers
# orders -> loads list of your orders

print("Jobster administrator controller - Type 'help' to see commands")
api = "http://mobiilisovellus.therozor.com:5000/"
command = ""
headers = {
    "Content-Type": "application/json",
    "apikey": ""
}

# GLOBAL LISTS
services = {}
categories = {}
user = {}

def login():
    global headers
    username = input(">> Username: ")
    password = input(">> Password: ")
    post_data = {
        "user_email": username,
        "user_password": password
    }
    print("Logging in...")
    r = requests.post(api+"login", json=post_data)
    if r.status_code == 200:
        headers['apikey'] = r.json()['apikey']
        print(str(r.status_code)+" | Login succesfull!")
    else:
        print("Login failed, response:")
        print(r.status_code)
        print(r.text)



def me(print_data = True):
    global headers
    global user
    print("Loading own information...")
    r = requests.get(api+"user/me", headers=headers)
    if r.status_code == 200:
        if print_data:
            print(r.text)
        user = copy.deepcopy(r.json()['data'])
    else:
        print("Request failed:")
        print(r.status_code)
        print(r.text)

def update_me():
    me()
    euser = {}
    euser['user_name'] = input(">> Name: ")
    euser['user_phone'] = input(">> Phone: ")
    euser['user_address'] = input(">> Address: ")
    euser['user_postalcode'] = input(">> Postalcode: ")
    euser['user_city'] = input(">> City: ")
    euser['user_password'] = input(">> Password: ")
    euser['user_password_again'] = input(">> Password again: ")
    euser['user_company_name'] = input(">> Company name: ")
    euser['user_company_id'] = input(">> Company id: ")

    r = requests.put(api+"user/me", json=euser, headers=headers)
    if r.status_code == 200:
        print("User updated!")
    else:
        print("User update request failed:")
        print(r.status_code)
        print(r.text)

def logout():
    global headers
    print("Logging out...")
    if headers['apikey'] != "":
        r = requests.delete(api+"login", headers=headers)
        if r.status_code == 200:
            print(str(r.status_code)+" | Logout succesfull!")
            headers['apikey'] = ""
        else:
            print("Login failed, response:")
            print(r.status_code)
            print(r.text)
    else:
        print("Not logged in!")

def services_list():
    global services
    global headers
    print("Loading service list...")
    r = requests.get(api+"services", headers=headers)
    if r.status_code == 200:
        print(str(r.status_code)+" | Service list: ")
        count = 0
        services = copy.deepcopy(r.json()['data'])

        for service in r.json()['data']:
            print("["+str(count)+"] "+str(service['service_provider_name']) +": "+service['service_title'])
            count = count + 1
    else:
        print("Unable to load services!")

def service():
    global headers
    number = input(">> Service number: ")
    print("Loading service...")
    service_id = services[int(number)]['service_id']
    r = requests.get(api+"service/"+service_id, headers=headers)
    if r.status_code == 200:
        print(str(r.status_code)+" | Service info: ")
        service = r.json()['data'][0]
        print("Name: "+str(service['service_title']))
        print("Provider: "+str(service['service_provider_name']))
        print("Availability: "+str(service['service_availability']))
        print("Category: "+str(service['service_category_name']))
        print("Price: "+str(service['service_price'])+"€")

    else:
        print("Unable to load service!")

def add_service():
    global headers
    global categories
    categories_list()
    print("Add a new service")
    service = {}

    category_number = input(">> Category number: ")
    service['service_category'] = categories[int(category_number)]['category_id']
    print("Category id("+str(service['service_category'])+") set!")

    service['service_type'] = input(">> Type(1,2): ")
    service['service_title'] = input(">> Title: ")
    service['service_description'] = input(">> Description: ")
    service['service_price_type'] = input(">> Price type(1,2): ")
    service['service_price'] = input(">> Price: ")
    service['service_availability'] = input(">> Avalability: ")

    print("Creating service...")
    r = requests.post(api+"services", json=service, headers=headers)
    if r.status_code == 200:
        print("Service created!")
    else:
        print("Failed to create service, response:")
        print(r.status_code)
        print(r.text)

def categories_list():
    global categories
    global headers
    print("Loading categories...")
    r = requests.get(api+"categories", headers=headers)
    if r.status_code == 200:
        print(str(r.status_code)+" | Category list: ")
        categories = copy.deepcopy(r.json()['data'])
        count = 0
        for category in r.json()['data']:
            print("["+str(count)+"] "+str(category['category_name']))
            count = count + 1
    else:
        print(r.status_code)
        print(r.text)

def add_category():
    global headers
    print("Create new category")
    category_name = input(">> Category name: ")
    category_desc = input(">> Category description: ")

    r = requests.post(api+"categories", json={"category_name": category_name, "category_description": category_desc}, headers=headers)
    if r.status_code == 200:
        print("Category created!")
    else:
        print("Failed to create category, response:")
        print(r.status_code)
        print(r.text)

def add_user():
    global headers
    print("Create a new user: ")
    user = {}
    user['user_type'] = input(">> Type(1,2): ")
    user['user_name'] = input(">> Name: ")
    user['user_email'] = input(">> Email: ")
    user['user_password'] = input(">> Password: ")
    user['user_password_again'] = input(">> Password again: ")

    print("Adding user...")
    r = requests.post(api+"register", json=user)
    if r.status_code == 200:
        print("User created!")
    else:
        print("Failed to create user, response:")
        print(r.status_code)
        print(r.text)

def users():
    global headers
    print("List of users")
    print("ACTION NOT SUPPORTED")

def providers_list():
    global headers
    print("Loading providers...")
    r = requests.get(api+"providers", headers=headers)
    if r.status_code == 200:
        print(str(r.status_code)+" | Providers list: ")
        count = 0
        for provider in r.json()['data']:
            print("["+str(count)+"] "+str(provider['user_company_name'])+", services: "+str(provider['user_total_service_count']))
            count = count + 1
    else:
        print(r.status_code)
        print(r.text)

def orders():
    global headers
    print("Loading your orders...")
    r = requests.get(api+"orders", headers=headers)
    if r.status_code == 200 and r.text != "null":
        print(str(r.status_code)+" | Your orders: ")
        count = 0
        for order in r.json()['data']:
            print("["+str(count)+"] "+order['order_service_info']['service_title']+", "+order['order_comments'])
            count = count + 1
    else:
        print(r.status_code)
        print(r.text)

def add_order():
    global headers
    print("Add a new order")
    services_list()
    service_number = input(">> Service number:")
    order = {}
    order['order_service_id'] = services[int(service_number)]['service_id']
    print("Service id("+str(order['order_service_id'])+") set!")
    order['order_comments'] = input(">> Comments to order: ")

    r = requests.post(api+"orders", headers=headers, json=order)
    if r.status_code == 200:
        print("Order created!")
    else:
        print("Failed to create order, response:")
        print(r.status_code)
        print(r.text)

def help():
    print("Available commands:")
    print("login")
    print("add_user")
    print("me")
    print("update_me")
    print("services")
    print("add_service")
    print("categories")
    print("add_category")
    print("users")
    print("add_user")
    print("providers")
    print("orders")
    print("add_order")
    print("help")

while(command != "exit"):
    command = input(">>")

    if command == "login":
        login()
    elif command == "me":
        me()
    elif command == "update_me":
        update_me()
    elif command == "logout" or command == "exit":
        logout()
    elif command == "services":
        services_list()
    elif command == "service":
        service()
    elif command == "add_service":
        add_service()
    elif command == "categories":
        categories_list()
    elif command == "add_category":
        add_category()
    elif command == "users":
        users()
    elif command == "add_user":
        add_user()
    elif command == "providers":
        providers_list()
    elif command == "orders":
        orders()
    elif command == "add_order":
        add_order()
    elif command == "help":
        help()
    else:
        print("Command not found")
