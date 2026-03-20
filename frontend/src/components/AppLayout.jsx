import { useNavigate, useLocation } from "react-router-dom";
import Sidebar from "./Sidebar";
import { useState } from "react";
import "./AppLayout.css";

export default function AppLayout({ children }) {
    const navigate = useNavigate();
    const location = useLocation();

    const [collapsed, setCollapsed] = useState(false);

    // Ocultar botón en páginas donde no debe aparecer
    const hideBack =
        location.pathname === "/login" ||
        location.pathname === "/";

    // Ocultar sidebar en login
    const hideSidebar =
        location.pathname === "/login" ||
        location.pathname === "/";

    return (
        <div style={{ display: "flex" }}>

            {/* SIDEBAR */}
            {!hideSidebar && (
                <Sidebar collapsed={collapsed} toggleCollapsed={() => setCollapsed(!collapsed)} />
            )}

            {/* CONTENIDO */}
            <div
                className="app-layout"
                style={{
                    marginLeft: hideSidebar ? 0 : collapsed ? 70 : 240,
                    width: "100%",
                    transition: "margin-left 0.3s"
                }}
            >

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
        </div>
    );
}