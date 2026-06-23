import { useNavigate } from "react-router-dom";

export default function Unauthorized() {
    const navigate = useNavigate();

    return (
        <div className="auth-container">
            <div className="auth-card" style={{ textAlign: "center" }}>
                <div style={{ fontSize: "48px", marginBottom: "16px" }}>🚫</div>
                <h2>Acceso Denegado</h2>
                <p className="subtitle">
                    No tienes permisos para acceder a esta sección.
                    Solo los administradores pueden ingresar aquí.
                </p>
                <button
                    className="btn-primary"
                    style={{ marginTop: "24px" }}
                    onClick={() => navigate("/dashboard")}
                >
                    Volver al inicio
                </button>
            </div>
        </div>
    );
}
