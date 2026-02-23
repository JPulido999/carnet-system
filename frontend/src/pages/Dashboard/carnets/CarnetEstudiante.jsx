import "./CarnetBase.css";

export default function CarnetEstudiante({ datos }) {
    return (
        <div className="carnet">

            <div className="barra-superior estudiante-color">
                <img
                    src="http://localhost:9000/control_ph/logounsch1.png"
                    className="logo-unsch"
                />

                <img
                    src={`data:image/png;base64,${datos.qrBase64}`}
                    className="qr-img"
                />
            </div>

            <div className="marco-foto">
                <img
                    src={`http://localhost:9000/control_ph/${datos.fotoCarnetUrl}`}
                    className="foto-usuario"
                />
            </div>

            <div className="info">
                <p><strong>Nombres:</strong> {datos.nombres}</p>
                <p><strong>Apellidos:</strong> {datos.apellidos}</p>
                <p><strong>Código:</strong> {datos.codigoEstudiante}</p>
                <p><strong>DNI:</strong> {datos.dni}</p>
                <p><strong>Escuela:</strong> {datos.escuela}</p>
            </div>

            <img
                src={`data:image/png;base64,${datos.barcodeBase64}`}
                className="codigo-barra-img"
            />

            <p className="nota-pie">* Este documento es personal e intransferible.</p>
        </div>
    );
}
