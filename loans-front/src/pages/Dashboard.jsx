import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Dashboard() {

    const navigate = useNavigate();
    const { email, logout } = useAuth();

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    return (
        <div className="page-container">
            <div className="dashboard-header">
                <div>
                    <h1>Banco Loans</h1>
                    <p className="subtitle">{email}</p>
                </div>
                <button className="btn-logout" onClick={handleLogout}>
                    Cerrar sesión
                </button>
            </div>

            <h3 className="section-title">¿Qué deseas hacer?</h3>

            <div className="dashboard-grid">
                <div className="dashboard-card" onClick={() => navigate("/request-loan")}>
                    <div className="dashboard-card-icon">💰</div>
                    <h4>Solicitar Préstamo</h4>
                    <p>Envía una nueva solicitud de préstamo indicando el monto y el plazo.</p>
                </div>

                <div className="dashboard-card" onClick={() => navigate("/my-loans")}>
                    <div className="dashboard-card-icon">📋</div>
                    <h4>Mis Préstamos</h4>
                    <p>Consulta el estado de tus préstamos aprobados, rechazados o pendientes.</p>
                </div>

                <div className="dashboard-card admin-card" onClick={() => navigate("/admin")}>
                    <div className="dashboard-card-icon">🔧</div>
                    <h4>Panel Administrador</h4>
                    <p>Aprueba o rechaza solicitudes de préstamo. Solo disponible para administradores.</p>
                </div>
            </div>
        </div>
    );
}
