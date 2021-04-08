class PostValidator:
    post_data = None
    fields = None
    invalid_fields = None
    invalid_messages = None
    valid_flag = None

    def __init__(self):
        self.fields = {}
        self.invalid_fields = []
        self.invalid_messages = []
        self.valid_flag = True

    def postData(self, post_data):
        # checking if post data is actually set
        # just a safeguard to avoid errors
        if type(post_data) is dict:
            self.post_data = post_data
        else:
            self.post_data = {}

    def addField(self, name, type = "string", min_len = None, max_len = None, validate = None, allowed = None, optional = False):
        self.fields[name] = {
            "name": name,
            "type": type,
            "min_len": min_len,
            "max_len": max_len,
            "validate": validate,
            "allowed": allowed,
            "optional": optional,
            "value": None
        }

    def validate(self):
        for key, field in self.fields.items():
            item_valid_flag = True

            if key not in self.post_data:
                if field['optional'] == False:
                    item_valid_flag = False
                    self.invalid_messages.append(key+" is missing from request")
                    self.invalid(key)
            else:
                if field['validate'] != None:
                    for validator in field['validate']:
                        if validator == "not_empty":
                            item_valid_flag = self.not_empty(key, self.post_data[key])


                if field['min_len'] != None:
                    if len(self.post_data[key]) < field['min_len']:
                        item_valid_flag = False
                        self.invalid_messages.append(key+" is too short, minimum expected "+str(field['min_len']))
                        self.invalid(key)

                if field['max_len'] != None:
                    if len(self.post_data[key]) > field['max_len']:
                        item_valid_flag = False
                        self.invalid_messages.append(key+" is too long, maximum expected "+str(field['max_len']))
                        self.invalid(key)

                if field['allowed'] != None:
                    allowed_string = []
                    for i in field['allowed']:
                        allowed_string.append(str(i))

                    if field['type'] == "string":
                        if not str(self.post_data[key]) in field['allowed']:
                            item_valid_flag = False
                            self.invalid_messages.append(key+" value is not allowed, expecting ("+", ".join(allowed_string)+")")
                            self.invalid(key)
                    elif field['type'] == "integer":
                        if not int(self.post_data[key]) in field['allowed']:
                            item_valid_flag = False
                            self.invalid_messages.append(key+" value is not allowed, expecting ("+", ".join(allowed_string)+")")
                            self.invalid(key)
                    elif field['type'] == "double":
                        if not double(self.post_data[key]) in field['allowed']:
                            item_valid_flag = False
                            self.invalid_messages.append(key+" value is not allowed, expecting ("+", ".join(allowed_string)+")")
                            self.invalid(key)

                # if the value was valid, update it to field
                if item_valid_flag:
                    if field['type'] == "string":
                        self.fields[key]['value'] = self.post_data[key]
                    elif field['type'] == "integer":
                        self.fields[key]['value'] = int(self.post_data[key])
                    elif field['type'] == "double":
                        self.fields[key]['value'] = double(self.post_data[key])
            #endif
        return self.valid_flag

    def invalid(self, field):
        self.valid_flag = False
        self.invalid_fields.append(field)

    def getInvalidFields(self):
        return self.invalid_fields

    def getInvalidMessages(self):
        return self.invalid_messages

    def get(self, key):
        if key in self.fields:
            return self.fields[key]['value']
        else:
            return None

    # validators
    def not_empty(self, key, value):
        if value == "" or value == None:
            self.invalid_messages.append(key+" has an empty value")
            self.invalid(key)
            return False
        else:
            return True
