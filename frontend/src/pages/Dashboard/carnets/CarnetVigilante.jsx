import "./CarnetBase.css";
import { BACKEND_URL } from "../../../config/api";

export default function CarnetVigilante({ datos }) {
    return (
        <div id="carnet" className="carnet carnet-vigilante">

            <div className="barra-superior vigilante-color">
                <img src={`${BACKEND_URL}/uploads/logounsch1.png`} className="logo-unsch" />
                <img src={`data:image/png;base64,${datos.qrBase64}`} className="qr-img" />
            </div>

            <div className="marco-foto">
                <img
                    src={`${BACKEND_URL}/uploads/${datos.fotoCarnetUrl}`}
                    className="foto-usuario"
                />
            </div>

            <div className="info">
                <p><strong>VIGILANTE AUTORIZADO</strong></p>
                <p><strong>Nombres:</strong> {datos.nombres}</p>
                <p><strong>DNI:</strong> {datos.dni}</p>
            </div>
        </div>
    );
}
