import "./CarnetBase.css";

export default function CarnetAdministrativo({ datos }) {
    return (
        <div id="carnet" className="carnet carnet-admin">

            <div className="barra-superior admin-color">
                <img src="http://localhost:9000/control_ph/logounsch1.png" className="logo-unsch" />
                <img src={`data:image/png;base64,${datos.qrBase64}`} className="qr-img" />
            </div>

            <div className="marco-foto">
                <img
                    src={`http://localhost:9000/control_ph/${datos.fotoCarnetUrl}`}
                    className="foto-usuario"
                />
            </div>

            <div className="info">
                <p><strong>Nombres:</strong> {datos.nombres}</p>
                <p><strong>Área:</strong> {datos.area || "Administración General"}</p>
                <p><strong>DNI:</strong> {datos.dni}</p>
            </div>

        </div>
    );
}
