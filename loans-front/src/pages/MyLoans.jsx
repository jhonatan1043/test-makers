import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";
import { useAuth } from "../context/AuthContext";

const STATUS_LABEL = {
    PENDING: "Pendiente",
    APPROVED: "Aprobado",
    REJECTED: "Rechazado"
};

const STATUS_CLASS = {
    PENDING: "badge-pending",
    APPROVED: "badge-approved",
    REJECTED: "badge-rejected"
};

export default function MyLoans() {

    const navigate = useNavigate();
    const { userId } = useAuth();

    const [loans, setLoans] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        if (!userId) {
            setError("No se encontró tu ID de usuario.");
            setLoading(false);
            return;
        }
        fetchLoans();
    }, [userId]);

    const fetchLoans = async () => {
        try {
            const response = await api.get(`/loans/user/${userId}`);
            setLoans(response.data);
        } catch (err) {
            setError("Error al cargar los préstamos");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="page-container">
            <div className="page-header">
                <button className="btn-back" onClick={() => navigate("/dashboard")}>
                    ← Volver
                </button>
                <h2>Mis Préstamos</h2>
            </div>

            {loading && <p className="loading">Cargando...</p>}
            {error && <div className="error-box">{error}</div>}

            {!loading && loans.length === 0 && !error && (
                <div className="empty-state">
                    <p>No tienes préstamos registrados.</p>
                    <button className="btn-primary" onClick={() => navigate("/request-loan")}>
                        Solicitar Préstamo
                    </button>
                </div>
            )}

            <div className="loans-grid">
                {loans.map((loan) => (
                    <div key={loan.id} className="loan-card">
                        <div className="loan-card-header">
                            <span className="loan-id">Préstamo #{loan.id}</span>
                            <span className={`badge ${STATUS_CLASS[loan.status]}`}>
                                {STATUS_LABEL[loan.status]}
                            </span>
                        </div>
                        <div className="loan-card-body">
                            <div className="loan-info">
                                <span>Monto</span>
                                <strong>${Number(loan.amount).toLocaleString()}</strong>
                            </div>
                            <div className="loan-info">
                                <span>Plazo</span>
                                <strong>{loan.termMonths} meses</strong>
                            </div>
                            {loan.createdAt && (
                                <div className="loan-info">
                                    <span>Fecha</span>
                                    <strong>{new Date(loan.createdAt).toLocaleDateString()}</strong>
                                </div>
                            )}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
