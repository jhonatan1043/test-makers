import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";
import { useAuth } from "../context/AuthContext";

export default function RequestLoan() {

    const navigate = useNavigate();
    const { userId } = useAuth();

    const [form, setForm] = useState({ amount: "", termMonths: "" });
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);

    const change = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const submit = async (e) => {
        e.preventDefault();
        setError("");
        setSuccess("");

        if (!userId) {
            setError("No se encontró tu ID de usuario. Por favor regístrate nuevamente.");
            return;
        }

        setLoading(true);
        try {
            await api.post(`/loans/${userId}`, {
                amount: parseFloat(form.amount),
                termMonths: parseInt(form.termMonths)
            });
            setSuccess("Préstamo solicitado correctamente. Estado: PENDIENTE");
            setForm({ amount: "", termMonths: "" });
        } catch (err) {
            setError(err.response?.data?.message || "Error al solicitar el préstamo");
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
                <h2>Solicitar Préstamo</h2>
            </div>

            <div className="card">
                {error && <div className="error-box">{error}</div>}
                {success && <div className="success-box">{success}</div>}

                <form onSubmit={submit}>
                    <div className="field">
                        <label>Monto del préstamo ($)</label>
                        <input
                            name="amount"
                            type="number"
                            min="1"
                            step="0.01"
                            placeholder="Ej: 5000000"
                            value={form.amount}
                            onChange={change}
                            required
                        />
                    </div>

                    <div className="field">
                        <label>Plazo (meses)</label>
                        <input
                            name="termMonths"
                            type="number"
                            min="1"
                            placeholder="Ej: 24"
                            value={form.termMonths}
                            onChange={change}
                            required
                        />
                    </div>

                    <button type="submit" className="btn-primary" disabled={loading}>
                        {loading ? "Enviando..." : "Solicitar Préstamo"}
                    </button>
                </form>
            </div>
        </div>
    );
}
