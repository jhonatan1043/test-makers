import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/axiosConfig";
import { useAuth } from "../context/AuthContext";

export default function Register() {

    const navigate = useNavigate();
    const { saveUserId } = useAuth();

    const [form, setForm] = useState({
        name: "",
        email: "",
        password: "",
        role: "USER"
    });

    const [error, setError] = useState("");

    const change = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const submit = async (e) => {
        e.preventDefault();
        setError("");
        try {
            const response = await api.post("/auth/register", form);
            saveUserId(response.data.id);
            alert("Registro exitoso. Ahora inicia sesión.");
            navigate("/");
        } catch (err) {
            setError(err.response?.data?.message || "Error al registrarse");
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h2>Crear Cuenta</h2>
                <p className="subtitle">Sistema de Préstamos Bancarios</p>

                {error && <div className="error-box">{error}</div>}

                <form onSubmit={submit}>
                    <div className="field">
                        <label>Nombre completo</label>
                        <input
                            name="name"
                            placeholder="Juan Pérez"
                            onChange={change}
                            required
                        />
                    </div>

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

                    <div className="field">
                        <label>Tipo de cuenta</label>
                        <select name="role" onChange={change} value={form.role}>
                            <option value="USER">Usuario</option>
                            <option value="ADMIN">Administrador</option>
                        </select>
                    </div>

                    <button type="submit" className="btn-primary">
                        Registrarse
                    </button>
                </form>

                <p className="auth-link">
                    ¿Ya tienes cuenta? <Link to="/">Inicia sesión</Link>
                </p>
            </div>
        </div>
    );
}
