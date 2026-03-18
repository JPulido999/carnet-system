import "./CarnetBase.css";

export default function CarnetVigilante({ datos }) {
    return (
        <div id="carnet" className="carnet carnet-vigilante">

            <div className="barra-superior vigilante-color">
                <img src="http://localhost:9000/uploads/logounsch1.png" className="logo-unsch" />
                <img src={`data:image/png;base64,${datos.qrBase64}`} className="qr-img" />
            </div>

            <div className="marco-foto">
                <img
                    src={`http://localhost:9000/uploads/${datos.fotoCarnetUrl}`}
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
