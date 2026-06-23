import { createContext, useContext, useState } from "react";

const AuthContext = createContext(null);

function decodeToken(token) {
    try {
        const payload = token.split(".")[1];
        return JSON.parse(atob(payload));
    } catch {
        return null;
    }
}

export function AuthProvider({ children }) {

    const [token, setToken] = useState(
        localStorage.getItem("token")
    );

    const [userId, setUserId] = useState(
        localStorage.getItem("userId")
    );

    const email = token ? decodeToken(token)?.sub : null;

    const login = (jwt) => {
        localStorage.setItem("token", jwt);
        setToken(jwt);
    };

    const saveUserId = (id) => {
        localStorage.setItem("userId", String(id));
        setUserId(String(id));
    };

    const logout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("userId");
        setToken(null);
        setUserId(null);
    };

    return (
        <AuthContext.Provider value={{ token, userId, email, login, saveUserId, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) throw new Error("AuthProvider requerido");
    return context;
}
