import { useContext, useState } from "react";
import { AuthContext } from "../context/AuthContext";
import { Link, useLocation } from "react-router-dom";

import {
    LayoutDashboard,
    QrCode,
    ClipboardCheck,
    BarChart3,
    Users
} from "lucide-react";

import "./Sidebar.css";

export default function Sidebar() {
    const { usuario } = useContext(AuthContext);
    const location = useLocation();

    const [collapsed, setCollapsed] = useState(false);
    const [mobileOpen, setMobileOpen] = useState(false);

    const isActive = (path) => location.pathname === path;

    const Item = ({ to, icon: Icon, label }) => (
        <Link
            to={to}
            className={`sidebar-item ${isActive(to) ? "active" : ""}`}
            onClick={() => setMobileOpen(false)}
        >
            <Icon size={20} className="icon" />
            {!collapsed && <span>{label}</span>}
        </Link>
    );

    return (
        <>
            {/* MOBILE BUTTON */}
            <button
                className="menu-toggle"
                onClick={() => setMobileOpen(!mobileOpen)}
            >
                ☰
            </button>

            <div className={`sidebar ${collapsed ? "collapsed" : ""} ${mobileOpen ? "open" : ""}`}>

                {/* HEADER */}
                <div className="sidebar-header">
                    {!collapsed && <h2>UNSCH</h2>}

                    <button
                        className="collapse-btn"
                        onClick={() => setCollapsed(!collapsed)}
                    >
                        {collapsed ? "➡" : "⬅"}
                    </button>
                </div>

                {/* NAV */}
                <nav>
                    <Item to="/dashboard" icon={LayoutDashboard} label="Mi Carnet" />

                    {(usuario?.rol === "VIGILANTE" || usuario?.rol === "ADMIN_SISTEMA") && (
                        <>
                            <Item to="/verificacion/scan" icon={QrCode} label="Escanear QR" />
                            <Item to="/verificacion/manual" icon={ClipboardCheck} label="Verificación manual" />
                        </>
                    )}

                    {usuario?.rol === "ADMIN_SISTEMA" && (
                        <>
                            <Item to="/admin" icon={BarChart3} label="Eventos de accesos" />
                            <Item to="/usuarios" icon={Users} label="Gestionar usuarios" />
                        </>
                    )}
                </nav>
            </div>
        </>
    );
}