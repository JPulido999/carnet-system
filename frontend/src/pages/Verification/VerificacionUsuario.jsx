import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState, useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
import { obtenerVerificacionPorUuid } from "../../services/verificacionService";

import "./VerificacionUsuario.css";

export default function VerificacionUsuario() {
  const { uuid } = useParams();
  const navigate = useNavigate();
  const { token } = useContext(AuthContext);

  const [info, setInfo] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!uuid || !token) return;

    obtenerVerificacionPorUuid(uuid, token)
      .then(setInfo)
      .catch(() =>
        setError("No se pudo verificar la identidad mediante QR")
      );
  }, [uuid, token]);

  return (
    <div className="verificacion-container">
      <div className="verificacion-card">

        <h2>Verificación de Identidad</h2>

        {/* ✅ RESULTADO QR */}
        {info && (
          <>
            <div className="foto-container">
              <img
                src={`data:image/jpeg;base64,${info.fotoBase64}`}
              />
            </div>

            <div className="datos-verificacion">
              <p><strong>{info.nombres} {info.apellidos}</strong></p>
              <p><strong>DNI:</strong> {info.dni}</p>
              <p><strong>Escuela:</strong> {info.escuela}</p>
            </div>

            <div className="estado-verificado">
              ✔ IDENTIDAD VÁLIDA
            </div>
          </>
        )}

        {/* ❌ ERROR QR */}
        {error && (
          <>
            <p className="error-text">{error}</p>

            <button
              className="btn-manual"
              onClick={() => navigate("/verificacion/manual")}
            >
              Verificación manual
            </button>
          </>
        )}
      </div>
    </div>
  );
}
