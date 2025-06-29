import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../Authorization/AuthContext.jsx";
import "./LoginPage.css"
import {Button} from "../../core/Button/Button.jsx";
import Header from "../Header/Header.jsx";
import {OK} from "../../../data/Strings.js";

export function LoginPage() {
    const [type, setType] = useState("register")
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [passwordAgain, setPasswordAgain] = useState("");
    const [error, setError] = useState("");
    const auth = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        setType("login")
    }, [])

    async function handleLogin(e) {
        e.preventDefault();
        if (type === "login") {
            const response = await auth.login(username, password);
            if (response === OK) {
                navigate("/main");
            } else {
                setError(response);
            }
        } else {
            const registerRes =  await auth.register(username, password, passwordAgain)
            if (registerRes === OK) {
                navigate("/main")
            } else {
                setError(registerRes)
            }
        }
    };

    function changeType() {
        if (type === "login") {
            setType("register")
        } else {
            setType("login")
        }
        setError("")
    }

    return (
        <div>
            <Header/>
            <h1>{type === "login" ? "Вход" : "Регистрация"}</h1>
            <form style={{marginBottom: "1rem"}} onSubmit={handleLogin}>
                <div>
                    <label className={"label"}>Логин</label>
                    <input
                        id="login"
                        className={"input"}
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>
                <div>
                    <label className={"label"}>Пароль</label>
                    <input
                        id="password"
                        className={"input"}
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                {type !== "login" &&
                    <div>
                        <label className={"label"}>Повторите пароль</label>
                        <input
                            id="repeatedPassword"
                            className={"input"}
                            type="password"
                            value={passwordAgain}
                            onChange={(e) => setPasswordAgain(e.target.value)}
                        />
                    </div>}
                {error && <p className={"error"}>{error}</p>}
                <Button type="submit">{type === "login" ? "Войти" : "Зарегистрироваться"}</Button>
            </form>
            <Button onClick={changeType}
                    transparent={true}>{type === "login" ? "Еще не зарегистрирован" : "Уже есть аккаунт"}</Button>
        </div>
    );
}
