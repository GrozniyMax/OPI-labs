import React, { createContext, useState, useContext } from "react";
import { login, register } from "../../../data/Repository";
import { OK, PASSWORDS_DIFFERENT_ERROR } from "../../../data/Strings.js";
import SHA256 from "crypto-js/sha256";

const AuthContext = createContext();

const SALT_ROUNDS = 10;

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [userPassword, setUserPassword] = useState(null);

    const loginUser = async (username, password) => {
        const hashedPassword = SHA256(password).toString();
        const response = await login(username, hashedPassword);
        console.log(username, hashedPassword)
        if (response === OK) {
            setUser(username);
            setUserPassword(hashedPassword);
        }
        return response;
    };

    const registerUser = async (username, password, passwordAgain) => {
        if (password !== passwordAgain) {
            return PASSWORDS_DIFFERENT_ERROR;
        } else {
            // Хэшируем пароль перед отправкой на сервер
            const hashedPassword = SHA256(password).toString();
            const response = await register(username, hashedPassword);
            console.log(username, hashedPassword)
            if (response === OK) {
                setUser(username);
                setUserPassword(hashedPassword);
            }
            return response;
        }
    };

    const logout = () => setUser(null);

    return (
        <AuthContext.Provider value={{ user, password: userPassword, login: loginUser, logout, register: registerUser }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}
