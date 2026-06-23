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

    const [role, setRole] = useState(
        localStorage.getItem("role")
    );

    const email = token ? decodeToken(token)?.sub : null;

    const isAdmin = role === "ADMIN";

    const login = (jwt) => {
        localStorage.setItem("token", jwt);
        setToken(jwt);
    };

    const saveUserData = (id, userRole) => {
        localStorage.setItem("userId", String(id));
        localStorage.setItem("role", userRole);
        setUserId(String(id));
        setRole(userRole);
    };

    const logout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("userId");
        localStorage.removeItem("role");
        setToken(null);
        setUserId(null);
        setRole(null);
    };

    return (
        <AuthContext.Provider value={{ token, userId, role, email, isAdmin, login, saveUserData, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) throw new Error("AuthProvider requerido");
    return context;
}
