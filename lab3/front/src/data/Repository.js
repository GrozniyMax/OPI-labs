import {AUTH_ERROR, ERROR, OK, USER_EXIST_ERROR} from "./Strings.js";
import {transformPoints} from "./Util.js";

export const login = async (username, password) => {
    try {
        const response = await fetch('http://localhost:8080/users/loginUser', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': ' Basic YXJla2Fsb3Y6cXdlcnR5MTIzNA=='
            },
            body: JSON.stringify({ login:username, password:password }),
        });
        if (response.ok) {
            return OK;
        }
        return AUTH_ERROR;
    } catch (error) {
        return `${AUTH_ERROR}:, ${error}`
    }
};

export const register = async (username, password) => {
    const existUser = await login(username, password)
    if (existUser === OK) {
        return USER_EXIST_ERROR
    }
    try {
        const response = await fetch('http://localhost:8080/users/signUpUser', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': ' Basic YXJla2Fsb3Y6cXdlcnR5MTIzNA=='
            },
            body: JSON.stringify({ login:username, password:password }),
        });
        if (response.ok) {
            return OK;
        }
        return ERROR;
    } catch (error) {
        return ERROR
    }
}


export const getPoints = async (username, password) => {
    try {
        const response = await fetch('http://localhost:8080/points/getAll', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': ' Basic YXJla2Fsb3Y6cXdlcnR5MTIzNA=='
            },
            body: JSON.stringify({ login:username, password:password }),
        });
        if (response.ok) {
            const data = await response.json();
            return transformPoints(data);
        }
        return ERROR;
    } catch (error) {
        return ERROR
    }
}

export const clearPoints = async (username, password) => {
    try {
        const response = await fetch('http://localhost:8080/points/clearAll', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': ' Basic YXJla2Fsb3Y6cXdlcnR5MTIzNA=='
            },
            body: JSON.stringify({ login:username, password:password }),
        });
        if (response.ok) {
            const data = await response.json();
            return transformPoints(data);
        }
        return ERROR;
    } catch (error) {
        return ERROR
    }
}

export const addPoint = async (username, password, x, y, r, time, result) => {
    try {
        const response = await fetch('http://localhost:8080/points/addPoint', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': ' Basic YXJla2Fsb3Y6cXdlcnR5MTIzNA=='
            },
            body: JSON.stringify({ login:username, password:password, x: x, y: y, r: r, time: time, result: result }),
        });
        if (response.ok) {
            const data = await response.json();
            return transformPoints(data);
        }
        return ERROR;
    } catch (error) {
        return ERROR
    }
}