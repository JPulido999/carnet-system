import { useNavigate, useLocation } from "react-router-dom";
import "./AppLayout.css";

export default function AppLayout({ children }) {
    const navigate = useNavigate();
    const location = useLocation();

    // Ocultar botón en páginas donde no debe aparecer
    const hideBack =
        location.pathname === "/login" ||
        location.pathname === "/";

    return (
        <div className="app-layout">

            {!hideBack && (
                <button
                    className="btn-back-global"
                    onClick={() => navigate(-1)}
                >
                    ⬅ Atrás
                </button>
            )}

            {children}
        </div>
    );
}
