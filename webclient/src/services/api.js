import axios from "axios";

const api_url = "http://localhost:5021"

const post_options = {
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
    }
}

const get_options = {
    headers: {

    }
}

function login(username, password, callback = null, failure_callback = null){
    axios
        .post(api_url+"/login",{
            user_email: username,
            user_password: password
        }, post_options)
        .then(res => callback(res))
        .catch(err => failure_callback(err));
}

function getServiceList(callback = null, failure_callback = null){
    axios
        .get(api_url+"/services")
        .then(res => callback(res))
        .catch(err => failure_callback(err));
}

const Api = {
    login: login,
    services: getServiceList
};

export default Api;
