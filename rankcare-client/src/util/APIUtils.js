import { API_BASE_URL, ACCESS_TOKEN } from '../constants';

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    })
    
    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
    .then(response => 
        response.json().then(json => {
            if(!response.ok) {
                return Promise.reject(json);
            }
            return json;
        })
    );
};

export function login(loginRequest) {
    return request({
        url: API_BASE_URL + "/auth/signin",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return request({
        url: API_BASE_URL + "/auth/signup",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}

export function checkUsernameAvailability(username) {
    return request({
        url: API_BASE_URL + "/user/checkUsernameAvailability?username=" + username,
        method: 'GET'
    });
}

export function checkEmailAvailability(email) {
    return request({
        url: API_BASE_URL + "/user/checkEmailAvailability?email=" + email,
        method: 'GET'
    });
}


export function getCurrentUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/user/me",
        method: 'GET'
    });
}

export function getUserProfile(username) {
    return request({
        url: API_BASE_URL + "/users/" + username,
        method: 'GET'
    });
}

export function getAllUsers() {
    return request({
        url: API_BASE_URL + "/users",
        method: 'GET'
    });
}

export function deleteUser(id) {
    return request({
        url: API_BASE_URL + "/users/" + id,
        method: 'DELETE'
    });
}

export function udpateUser(signupRequest) {
    return request({
        url: API_BASE_URL + "/user/update",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}

export function getToxicityData(page, count) {
    return request({
        url: API_BASE_URL + "/toxicity?page=" + page + "&count=" + count,
        method: 'GET'
    });
}

export function deleteToxicity(id) {
    return request({
        url: API_BASE_URL + "/toxicity/" + id,
        method: 'DELETE'
    });
}

export function createToxicity(toxicityRequest) {
    return request({
        url: API_BASE_URL + "/toxicity/insert",
        method: 'POST',
        body: JSON.stringify(toxicityRequest)
    });
}

export function updateToxicity(toxicityRequest) {
    return request({
        url: API_BASE_URL + "/toxicity/update",
        method: 'POST',
        body: JSON.stringify(toxicityRequest)
    });
}

export function getConsumptionData(page, count) {
    return request({
        url: API_BASE_URL + "/consumption?page=" + page + "&count=" + count,
        method: 'GET'
    });
}

export function deleteConsumption(id) {
    return request({
        url: API_BASE_URL + "/consumption/" + id,
        method: 'DELETE'
    });
}

export function createConsumption(consumptionRequest) {
    return request({
        url: API_BASE_URL + "/consumption/insert",
        method: 'POST',
        body: JSON.stringify(consumptionRequest)
    });
}

export function updateConsumtion(consumptionRequest) {
    return request({
        url: API_BASE_URL + "/consumption/update",
        method: 'POST',
        body: JSON.stringify(consumptionRequest)
    });
}

export function getAllChemicals() {
    return request({
        url: API_BASE_URL + "/chemicals",
        method: 'GET'
    });
}

export function getSites(page, count) {
    return request({
        url: API_BASE_URL + "/sites?page=" + page + "&count=" + count,
        method: 'GET'
    });
}