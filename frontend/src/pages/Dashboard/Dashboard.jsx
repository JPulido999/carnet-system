import "./Dashboard.css";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "../../context/AuthContext";
import { obtenerCarnetMe } from "../../services/usuarioService";
import html2canvas from "html2canvas";
import jsPDF from "jspdf";

import MisAccesosTabla from "./MisAccesosTabla";

export default function Dashboard() {
    const { logout, token, usuario } = useContext(AuthContext);
    const [datosUsuario, setDatosUsuario] = useState(null);
    const [error, setError] = useState(false);

    useEffect(() => {
        if (token) {
            obtenerCarnetMe()
                .then(res => setDatosUsuario(res))
                .catch(err => {
                    console.error(err);
                    setError(true);
                });
        }
    }, [token]);


    // 🔒 BLOQUEAR CLIC DERECHO + F12 + CTRL+U + CTRL+SHIFT+I
    useEffect(() => {
        const disableContextMenu = (e) => e.preventDefault();
        document.addEventListener("contextmenu", disableContextMenu);

        const blockKeys = (e) => {
            if (
                e.key === "F12" ||
                (e.ctrlKey && e.shiftKey && e.key === "I") ||
                (e.ctrlKey && e.shiftKey && e.key === "J") ||
                (e.ctrlKey && e.key === "U")
            ) {
                e.preventDefault();
                return false;
            }
        };
        document.addEventListener("keydown", blockKeys);

        return () => {
            document.removeEventListener("contextmenu", disableContextMenu);
            document.removeEventListener("keydown", blockKeys);
        };
    }, []);


    if (error) return <p>Error al cargar los datos.</p>;
    if (!datosUsuario) return <p>Cargando...</p>;

    
    /** JPG */
    const descargarJPG = () => {
        const carnet = document.getElementById("carnet");

        html2canvas(carnet, {
            scale: 3,
            useCORS: true,
            logging: false,
            allowTaint: false,
        }).then(canvas => {
            const link = document.createElement("a");
            link.download = `${datosUsuario.codigoEstudiante}_carnet.jpg`;
            link.href = canvas.toDataURL("image/jpeg", 1.0);
            link.click();
        });
    };

    /** PDF */
    const descargarPDF = () => {
        const carnet = document.getElementById("carnet");

        html2canvas(carnet, {
            scale: 3,
            useCORS: true,
            logging: false,
        }).then(canvas => {
            const imgData = canvas.toDataURL("image/png");

            // Hoja A4 en puntos
            const pdf = new jsPDF({
                orientation: "portrait",
                unit: "pt",
                format: "a4"
            });

            // Tamaño real del carnet
            const carnetWidth = 153.07;  // 5.4cm
            const carnetHeight = 241.94; // 8.5cm

            // Centramos en A4
            const x = (595 - carnetWidth) / 2;
            const y = (842 - carnetHeight) / 2;

            pdf.addImage(imgData, "PNG", x, y, carnetWidth, carnetHeight);
            const nombreArchivo = datosUsuario.codigoEstudiante || datosUsuario.dni;
            pdf.save(`${nombreArchivo}_carnet.pdf`);
        });
    };


    return (
        <div className="dashboard-container">

            {/* --- BOTÓN SUPERIOR DERECHA --- */}
            <button className="btn-logout" onClick={logout}>⎋ Cerrar sesión</button>

            {/* --- CONTENIDO CENTRAL --- */}
            <div className="main-content">

                <div className="actions">
                    <button onClick={descargarJPG}>🖼 Descargar JPG</button>
                    <button onClick={descargarPDF}>📄 Descargar PDF</button>
                </div>

                {/* --- CARNET --- */}
                <div id="carnet" className="carnet">

                    {/* HEADER BARRA */}
                    <div className="barra-superior">
                        <img
                            src="/logounsch1.png"
                            alt="Logo UNSCH"
                            className="logo-unsch"
                            crossOrigin="anonymous"
                        />

                        {/* QR */}
                        <img
                            src={`data:image/png;base64,${datosUsuario.qrBase64}`}
                            className="qr-img"
                            crossOrigin="anonymous"
                        />
                    </div>

                    {/* FOTO */}
                    <div className="marco-foto">
                        <img
                            src={`data:image/jpeg;base64,${datosUsuario.fotoBase64}`}
                            alt="Foto"
                            className="foto-usuario"
                            crossOrigin="anonymous"
                        />
                    </div>

                    {/* DATOS */}
                    <div className="info">
                        <p><strong>Nombres:</strong> {datosUsuario.nombres}</p>
                        <p><strong>Apellidos:</strong> {datosUsuario.apellidos}</p>
                        <p><strong>Código:</strong> {datosUsuario.codigoEstudiante}</p>
                        <p><strong>DNI:</strong> {datosUsuario.dni}</p>
                        <p><strong>Escuela:</strong> {datosUsuario.escuela}</p>
                    </div>

                    {/* CODIGO DE BARRAS */}
                    <img
                        src={`data:image/png;base64,${datosUsuario.barcodeBase64}`}
                        className="codigo-barra-img"
                        crossOrigin="anonymous"
                    />

                    <p className="nota-pie">* El uso de este documento es personal e instansferible</p>

                </div>
            </div>

            <MisAccesosTabla dni={datosUsuario.dni} />

        {/* --- OPCIONES SOLO PARA VIGILANTE --- */}
        {(usuario?.rol === "VIGILANTE" || usuario?.rol === "ADMIN_SISTEMA") && (
            <div className="verificacion-panel">
                <h3>Verificación de identidad</h3>

                <div className="verificacion-actions">
                    <button
                        onClick={() => window.location.href = "/verificacion/scan"}
                    >
                        📷 Escanear QR
                    </button>

                    <button
                        onClick={() => window.location.href = "/verificacion/manual"}
                    >
                        ✍ Verificación manual
                    </button>
                </div>
            </div>
        )}

        {/* --- OPCIÓN SOLO ADMIN SISTEMA --- */}
        {usuario?.rol === "ADMIN_SISTEMA" && (
        <div className="verificacion-panel">
            <h3>Administración del sistema</h3>

            <div className="verificacion-actions">
            <button
                onClick={() => window.location.href = "/admin"}
            >
                📊 Ver Eventos de Acceso
            </button>
            </div>
        </div>
        )}
        </div>
    );
    }