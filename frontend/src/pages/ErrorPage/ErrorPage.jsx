import "./ErrorPage.css";
import { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";

export default function ErrorPage() {
    const { logout } = useContext(AuthContext);
    return (
        <div className="error-container">
            <h1>No tiene un carnet válido</h1>
            <p>Acérquese a la garita de control para registrar su acceso.</p>
            <button onClick={logout}>Volver</button>
        </div>
    );
}
