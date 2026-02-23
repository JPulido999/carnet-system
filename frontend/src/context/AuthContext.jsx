import { createContext, useState, useEffect } from "react";
import { jwtDecode } from "jwt-decode";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [token, setToken] = useState(null);
    const [usuario, setUsuario] = useState(null);
    const [loading, setLoading] = useState(true); // 👈 NUEVO

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const tokenParam = urlParams.get("token");

        if (tokenParam) {
            localStorage.setItem("token", tokenParam);
            setToken(tokenParam);

            const payload = jwtDecode(tokenParam);
            setUsuario({ id: payload.sub, rol: payload.rol });

            window.history.replaceState({}, document.title, "/");
        } else {
            const storedToken = localStorage.getItem("token");
            if (storedToken) {
                setToken(storedToken);
                const payload = jwtDecode(storedToken);
                setUsuario({ id: payload.sub, rol: payload.rol });
            }
        }

        setLoading(false); // 👈 MUY IMPORTANTE
    }, []);

    const login = (newToken) => {
        localStorage.setItem("token", newToken);
        setToken(newToken);

        const payload = jwtDecode(newToken);
        setUsuario({ id: payload.sub, rol: payload.rol });
    };

    const logout = () => {
        localStorage.removeItem("token");
        setToken(null);
        setUsuario(null);
        window.location.href = "/";
    };

    return (
        <AuthContext.Provider value={{ token, usuario, loading, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};
