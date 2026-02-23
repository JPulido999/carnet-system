import { useContext, useState } from "react";
import { AuthContext } from "../../context/AuthContext";
import { obtenerVerificacionManual } from "../../services/verificacionService";
import "./VerificacionManual.css";
import { useNavigate } from "react-router-dom";


export default function VerificacionManual() {
    const { token } = useContext(AuthContext);

    const navigate = useNavigate();
    const [dni, setDni] = useState("");
    const [codigo, setCodigo] = useState("");
    const [resultado, setResultado] = useState(null);
    const [error, setError] = useState(null);

    const buscar = async () => {
        setError(null);
        setResultado(null);

        if (!dni.trim() && !codigo.trim()) {
            setError("Ingrese DNI o código");
            return;
        }

        try {
            const data = await obtenerVerificacionManual({
                dni: dni.trim(),
                codigo: codigo.trim()
            }, token);

            setResultado(data);
        } catch {
            setError("No se encontró al usuario");
        }
    };

    return (
        <div className="verificacion-manual-container">

            <div className="verificacion-manual-card">

                <h2>Verificación manual</h2>

                <div className="campos-manual">
                    <input
                        placeholder="DNI"
                        value={dni}
                        onChange={e => setDni(e.target.value)}
                    />

                    <input
                        placeholder="Código de estudiante"
                        value={codigo}
                        onChange={e => setCodigo(e.target.value)}
                    />

                    <button className="btn-verificar" onClick={buscar}>
                        Verificar
                    </button>
                </div>

                {resultado && (
                    <div className="resultado-verificacion">
                        <img
                            src={`data:image/jpeg;base64,${resultado.fotoBase64}`}
                            alt="Foto carnet"
                        />
                        <p><b>{resultado.nombres} {resultado.apellidos}</b></p>
                        <p>DNI: {resultado.dni}</p>
                        <p>Escuela: {resultado.escuela}</p>

                        <div className="estado-ok">
                            ✔ IDENTIDAD VÁLIDA
                        </div>
                    </div>
                )}

                {error && (
                    <div className="estado-error">
                        ✖ {error}
                    </div>
                )}
            </div>
        </div>
    );
}
