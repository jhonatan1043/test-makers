import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";

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

export default function AdminLoans() {

    const navigate = useNavigate();

    const [loans, setLoans] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [actionLoading, setActionLoading] = useState(null);
    const [filter, setFilter] = useState("ALL");

    useEffect(() => {
        fetchAll();
    }, []);

    const fetchAll = async () => {
        setLoading(true);
        setError("");
        try {
            const response = await api.get("/loans/all");
            setLoans(response.data);
        } catch (err) {
            if (err.response?.status === 403) {
                setError("Acceso denegado. Solo administradores pueden ver esta sección.");
            } else {
                setError("Error al cargar los préstamos.");
            }
        } finally {
            setLoading(false);
        }
    };

    const approve = async (id) => {
        setActionLoading(id + "-approve");
        try {
            const res = await api.patch(`/loans/${id}/approve`);
            setLoans(loans.map(l => l.id === id ? res.data : l));
        } catch (err) {
            alert(err.response?.data?.message || "Error al aprobar");
        } finally {
            setActionLoading(null);
        }
    };

    const reject = async (id) => {
        setActionLoading(id + "-reject");
        try {
            const res = await api.patch(`/loans/${id}/reject`);
            setLoans(loans.map(l => l.id === id ? res.data : l));
        } catch (err) {
            alert(err.response?.data?.message || "Error al rechazar");
        } finally {
            setActionLoading(null);
        }
    };

    const filtered = filter === "ALL"
        ? loans
        : loans.filter(l => l.status === filter);

    return (
        <div className="page-container">
            <div className="page-header">
                <button className="btn-back" onClick={() => navigate("/dashboard")}>
                    ← Volver
                </button>
                <h2>Panel de Administración</h2>
                <button className="btn-secondary" onClick={fetchAll}>
                    Actualizar
                </button>
            </div>

            <div className="filter-bar">
                {["ALL", "PENDING", "APPROVED", "REJECTED"].map(s => (
                    <button
                        key={s}
                        className={`filter-btn ${filter === s ? "active" : ""}`}
                        onClick={() => setFilter(s)}
                    >
                        {s === "ALL" ? "Todos" : STATUS_LABEL[s]}
                    </button>
                ))}
            </div>

            {loading && <p className="loading">Cargando préstamos...</p>}
            {error && <div className="error-box">{error}</div>}

            {!loading && !error && filtered.length === 0 && (
                <p className="empty-state">No hay préstamos en esta categoría.</p>
            )}

            {filtered.length > 0 && (
                <div className="table-container">
                    <table className="loans-table">
                        <thead>
                            <tr>
                                <th>#ID</th>
                                <th>Monto</th>
                                <th>Plazo</th>
                                <th>Estado</th>
                                <th>Fecha</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filtered.map((loan) => (
                                <tr key={loan.id}>
                                    <td>#{loan.id}</td>
                                    <td>${Number(loan.amount).toLocaleString()}</td>
                                    <td>{loan.termMonths} meses</td>
                                    <td>
                                        <span className={`badge ${STATUS_CLASS[loan.status]}`}>
                                            {STATUS_LABEL[loan.status]}
                                        </span>
                                    </td>
                                    <td>
                                        {loan.createdAt
                                            ? new Date(loan.createdAt).toLocaleDateString()
                                            : "—"}
                                    </td>
                                    <td className="actions-cell">
                                        {loan.status === "PENDING" ? (
                                            <>
                                                <button
                                                    className="btn-approve"
                                                    disabled={actionLoading !== null}
                                                    onClick={() => approve(loan.id)}
                                                >
                                                    {actionLoading === loan.id + "-approve" ? "..." : "Aprobar"}
                                                </button>
                                                <button
                                                    className="btn-reject"
                                                    disabled={actionLoading !== null}
                                                    onClick={() => reject(loan.id)}
                                                >
                                                    {actionLoading === loan.id + "-reject" ? "..." : "Rechazar"}
                                                </button>
                                            </>
                                        ) : (
                                            <span className="no-action">—</span>
                                        )}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}
