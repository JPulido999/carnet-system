import { useContext } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

export default function RutaProtegida({ children, roles }) {
    const { token, usuario, loading } = useContext(AuthContext);

    // ⏳ Esperar a que cargue el token
    if (loading) {
        return <p>Cargando sesión...</p>;
    }

    if (!token) {
        return <Navigate to="/" replace />;
    }

    if (roles && usuario && !roles.includes(usuario.rol)) {
    return <Navigate to="/error" replace />;
    }

    return children;
}
