import axios from "axios";

const api_url = "http://localhost:5021"

const post_options = {
    headers: {
        'Content-Type': 'application/json'
    }
}

const get_options = {
    headers: {

    }
}

function login(user_email, user_password, callback = null, failure_callback = null){
    axios
        .post(api_url+"/login",{
            user_email: user_email,
            user_password: user_password
        }, post_options)
        .then(res => callback(res))
        .catch(err => failure_callback(err));
}

function register(user_email, user_name, user_password, user_password_again, callback = null, failure_callback = null){
    axios
        .post(api_url+"/register",{
            user_type: 1,
            user_email: user_email,
            user_name: user_name,
            user_password: user_password,
            user_password_again: user_password_again
        }, post_options)
        .then(res => callback(res)).
        catch(err => failure_callback(err));
}

function getServiceList(callback = null, failure_callback = null){
    axios
        .get(api_url+"/services")
        .then(res => callback(res))
        .catch(err => failure_callback(err));
}

const Api = {
    login: login,
    register: register,
    services: getServiceList
};

export default Api;
