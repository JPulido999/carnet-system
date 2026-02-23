import { useEffect, useState } from "react";
import {
  obtenerTotalAccesos,
  obtenerAccesosPorDispositivo,
  obtenerTotalFiltrado
} from "../../services/zktecoService";


export default function Estadisticas({ filtro }) {
  const [total, setTotal] = useState(0);
  const [porDispositivo, setPorDispositivo] = useState([]);
  const [totalFiltrado, setTotalFiltrado] = useState(0);

  useEffect(() => {
    if (!filtro.startDate || !filtro.endDate) return;

    obtenerTotalFiltrado(filtro).then(setTotalFiltrado);
  }, [filtro]);

  return (
    <div className="admin-stats-grid">
      <div className="stat-card">
        <span>Registros encontrados</span>
        <strong>{totalFiltrado}</strong>
      </div>


      <div className="stat-card">
        <span>Por dispositivo</span>
        {porDispositivo.map(d => (
          <div key={d.dispositivo}>
            {d.dispositivo}: <b>{d.total}</b>
          </div>
        ))}
      </div>

      <div className="export-card">
        <span>Exportar</span>
        <button>Excel</button>
        <button>Pdf</button>
      </div>
    </div>
  );
}
