import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import {PrivateRoute} from "./components/start/Authorization/PrivateRoute.jsx";
import {LoginPage} from "./components/start/login/LoginPage.jsx";
import {AuthProvider} from "./components/start/Authorization/AuthContext.jsx";
import {MainPage} from "./components/main/MainPage/MainPage.jsx";
import "./App.css"

export default function App() {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="*" element={<LoginPage/>}/>
                    <Route path="/main" element={
                        <PrivateRoute>
                            <MainPage/>
                        </PrivateRoute>
                    }
                    />
                </Routes>
            </Router>
        </AuthProvider>
    );
}