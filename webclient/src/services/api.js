import axios from "axios";

const api_url = "http://localhost:5021"
const post_options = {
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
    }
}


function login(username, password, callback = null){
    axios
        .post(api_url+"/login",{
            user_email: username,
            user_password: password
        }, post_options)
        .then(res => console.log(res))
        .catch(err => console.log(err));
}

function getServiceList(callback = null){
    axios
        .get(api_url+"/services")
        .then(res => callback(res))
        .catch(erro => console.log(err));
}

const Api = {
    login: login,
    services: getServiceList
};

export default Api;
