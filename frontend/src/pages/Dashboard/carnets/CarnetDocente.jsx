import "./CarnetBase.css";

export default function CarnetDocente({ datos }) {
    return (
        <div id="carnet" className="carnet carnet-docente">

            <div className="barra-superior docente-color">
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
                <p><strong>Nombres:</strong> {datos.nombres}</p>
                <p><strong>Departamento:</strong> {datos.departamento || "Docencia"}</p>
                <p><strong>DNI:</strong> {datos.dni}</p>
            </div>

        </div>
    );
}
