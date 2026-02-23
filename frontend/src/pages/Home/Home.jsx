import "./Home.css";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function Home() {
    const { token, login } = useContext(AuthContext);
    const navigate = useNavigate();

    const [mostrarVigilante, setMostrarVigilante] = useState(false);
    const [usuario, setUsuario] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null);

    useEffect(() => {
        if (token) {
            navigate("/dashboard");
        }
    }, [token, navigate]);

    const iniciarConGoogle = () => {
        window.location.href =
            "http://localhost:9000/oauth2/authorization/google";
    };

    const loginVigilante = async () => {
        setError(null);

        try {
            const res = await fetch("http://localhost:9000/auth/local/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    username: usuario,
                    password: password
                })
            });

            if (!res.ok) throw new Error();

            const data = await res.json();
            login(data.token); // 🔑 reutilizas AuthContext
        } catch {
            setError("Credenciales incorrectas");
        }
    };

    return (
        <div className="home-container">

            {/* ICONO DISCRETO */}
            <div
                className="icono-vigilante"
                onClick={() => setMostrarVigilante(!mostrarVigilante)}
                title="Acceso vigilante"
            >
                👮
            </div>

            <img
                className="logo-unsch-principal"
                src="/logounsch1.png"
                alt=""
            />

            <h1>Carnet Digital UNSCH</h1>

            {/* LOGIN GOOGLE */}
            <button className="btn-google" onClick={iniciarConGoogle}>
                <img
                    src="/google-icon.png"
                    alt="Google"
                />
                Iniciar sesión con Google
            </button>

            <p>Accede a tu carnet estudiantil con tu correo institucional</p>

            {/* LOGIN VIGILANTE OCULTO */}
            {mostrarVigilante && (
                <div className="login-vigilante">
                    <h3>Acceso Vigilante</h3>

                    <input
                        placeholder="Usuario"
                        value={usuario}
                        onChange={e => setUsuario(e.target.value)}
                    />

                    <input
                        type="password"
                        placeholder="Contraseña"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                    />

                    <button onClick={loginVigilante}>
                        Ingresar
                    </button>

                    {error && <span className="error-text">{error}</span>}
                </div>
            )}
        </div>
    );
}
