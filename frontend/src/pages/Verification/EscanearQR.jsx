import "./EscanearQR.css";
import { useEffect } from "react";
import { Html5QrcodeScanner } from "html5-qrcode";
import { useNavigate } from "react-router-dom";

export default function EscanearQR() {
    const navigate = useNavigate();

    useEffect(() => {
        const scanner = new Html5QrcodeScanner(
            "qr-reader",
            { fps: 10, qrbox: { width: 250, height: 250 } },
            false
        );

        scanner.render(
            (decodedText) => {
                try {
                    const url = new URL(decodedText);
                    const uuid = url.pathname.split("/").pop();
                    scanner.clear();
                    navigate(`/verificacion/${uuid}`);
                } catch {
                    alert("QR inválido");
                }
            },
            () => {}
        );

        return () => scanner.clear().catch(() => {});
    }, [navigate]);

    return (
        <div className="escanear-qr-container">
            <div className="escanear-qr-card">
                <h2>Escanear QR</h2>
                <p>Apunte la cámara al código QR del carnet</p>

                <div className="qr-reader-box" style={{ position: "relative" }}>
                    <div id="qr-reader"></div>
                    <div className="qr-overlay"></div>
                </div>

                <div className="estado-escaner">
                    Cámara activa – esperando código QR
                </div>
            </div>
        </div>
    );
}
