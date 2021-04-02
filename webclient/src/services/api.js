import axios from "axios";

const api_url = "http://localhost:5021"

function login(username, password){
    axios
        .post(api_url+"/login",{
            username: username,
            password: password
        })
        .then(res => console.log(res))
        .catch(err => console.log(err));
}

const Api = {
    login: login
};

export default Api;
