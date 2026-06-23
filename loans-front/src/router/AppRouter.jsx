import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

import Login from "../pages/Login";
import Register from "../pages/Register";
import Dashboard from "../pages/Dashboard";
import RequestLoan from "../pages/RequestLoan";
import MyLoans from "../pages/MyLoans";
import AdminLoans from "../pages/AdminLoans";
import Unauthorized from "../pages/Unauthorized";

// Solo accesible si NO hay sesión activa
function PublicRoute({ children }) {
    const { token } = useAuth();
    return !token ? children : <Navigate to="/dashboard" replace />;
}

// Requiere sesión activa
function PrivateRoute({ children }) {
    const { token } = useAuth();
    return token ? children : <Navigate to="/" replace />;
}

// Requiere sesión activa + rol ADMIN
function AdminRoute({ children }) {
    const { token, isAdmin } = useAuth();
    if (!token) return <Navigate to="/" replace />;
    if (!isAdmin) return <Navigate to="/unauthorized" replace />;
    return children;
}

export default function AppRouter() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={
                    <PublicRoute><Login /></PublicRoute>
                } />

                <Route path="/register" element={
                    <PublicRoute><Register /></PublicRoute>
                } />

                <Route path="/dashboard" element={
                    <PrivateRoute><Dashboard /></PrivateRoute>
                } />

                <Route path="/request-loan" element={
                    <PrivateRoute><RequestLoan /></PrivateRoute>
                } />

                <Route path="/my-loans" element={
                    <PrivateRoute><MyLoans /></PrivateRoute>
                } />

                <Route path="/admin" element={
                    <AdminRoute><AdminLoans /></AdminRoute>
                } />

                <Route path="/unauthorized" element={
                    <PrivateRoute><Unauthorized /></PrivateRoute>
                } />

                <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
        </BrowserRouter>
    );
}
