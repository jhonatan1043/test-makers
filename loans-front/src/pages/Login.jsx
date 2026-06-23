import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/axiosConfig";
import { useAuth } from "../context/AuthContext";

export default function Login() {

    const navigate = useNavigate();
    const { login, saveUserData } = useAuth();

    const [form, setForm] = useState({ email: "", password: "" });
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const change = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const submit = async (e) => {
        e.preventDefault();
        setError("");
        setLoading(true);
        try {
            const loginRes = await api.post("/auth/login", form);
            const token = loginRes.data.token;

            login(token);

            const meRes = await api.get("/auth/me", {
                headers: { Authorization: `Bearer ${token}` }
            });

            saveUserData(meRes.data.id, meRes.data.role);

            navigate("/dashboard");
        } catch (err) {
            setError(err.response?.data?.message || "Credenciales incorrectas");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h2>Banco Loans</h2>
                <p className="subtitle">Sistema de Préstamos Bancarios</p>

                {error && <div className="error-box">{error}</div>}

                <form onSubmit={submit}>
                    <div className="field">
                        <label>Correo electrónico</label>
                        <input
                            name="email"
                            type="email"
                            placeholder="correo@ejemplo.com"
                            onChange={change}
                            required
                        />
                    </div>

                    <div className="field">
                        <label>Contraseña</label>
                        <input
                            name="password"
                            type="password"
                            placeholder="••••••••"
                            onChange={change}
                            required
                        />
                    </div>

                    <button type="submit" className="btn-primary" disabled={loading}>
                        {loading ? "Ingresando..." : "Ingresar"}
                    </button>
                </form>

                <p className="auth-link">
                    ¿No tienes cuenta? <Link to="/register">Regístrate</Link>
                </p>
            </div>
        </div>
    );
}
