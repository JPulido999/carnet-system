import "./MisAccesos.css";
import { useEffect, useState, useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
import { obtenerMisAccesos } from "../../services/zktecoService";

export default function MisAccesosTabla({ dni }) {

  const [data, setData] = useState({
    content: [],
    totalPages: 0,
    number: 0,
    first: true,
    last: true
  });
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);

  // arreglo de columnas
 function obtenerDia(fechaISO) {
    const dias = ["domingo", "lunes", "martes", "miércoles", "jueves", "viernes", "sábado"];
    const fecha = new Date(fechaISO);
    return dias[fecha.getDay()];
    }

function obtenerTipo(devAlias) {
    if (!devAlias) return "";
    if (devAlias.toUpperCase().endsWith("IN")) return "Ingreso";
    if (devAlias.toUpperCase().endsWith("OUT")) return "Salida";
    return "Desconocido";
    }


  //calendario
  /*const formatearFecha = (fecha) => {
    const d = new Date(fecha);

    const dias = ["Domingo","Lunes","Martes","Miércoles","Jueves","Viernes","Sábado"];

    const diaSemana = dias[d.getDay()];

    const fechaStr = d.toLocaleDateString();
    const horaStr = d.toLocaleTimeString();

    return `${diaSemana} ${fechaStr} ${horaStr}`;
  };*/

  // 📅 filtros simples
  const [startDate, setStartDate] = useState(() => {
    const d = new Date();
    d.setDate(d.getDate() - 7); // última semana
    return d.toISOString().slice(0, 16);
  });

  const [endDate, setEndDate] = useState(() => {
    const d = new Date();
    return d.toISOString().slice(0, 16);
  });

  const cargar = () => {
    if (!dni) return;

    setLoading(true);

    obtenerMisAccesos({
      startDate,
      endDate,
      page,
      size: 10,
      dni: dni
    })
    .then(res => {
        console.log("RESPUESTA REAL:", res);
        const page = res.content ? res : res.data;
        setData(page);
    })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    cargar();
  }, [page]);

  const onBuscar = () => {
    setPage(0);
    cargar();
  };

  if (loading && !data) return <p>Cargando accesos...</p>;
  if (!data || !data.content) return <p>Cargando accesos...</p>;

  return (
    <div className="mis-accesos-container">

      <h3>REGISTRO DE INGRESOS Y SALIDAS</h3>

      {/* FILTROS */}
      <div className="mis-accesos-filtros" style={{ display: "flex", gap: 10, marginBottom: 10 }}>
        <div>
          <label>Inicio</label><br />
          <input type="datetime-local" value={startDate} onChange={e => setStartDate(e.target.value)} />
        </div>

        <div>
          <label>Fin</label><br />
          <input type="datetime-local" value={endDate} onChange={e => setEndDate(e.target.value)} />
        </div>

        <button onClick={onBuscar}>🔍 Buscar</button>
      </div>

      {/* TABLA */}
        <table className="mis-accesos-tabla">
        <thead>
            <tr>
            <th>Día</th>
            <th>Fecha y hora</th>
            <th>Puerta</th>
            <th>Tipo</th>
            <th>Evento</th>
            </tr>
        </thead>
        <tbody>
            {data.content.length === 0 && (
            <tr>
                <td colSpan="5">No hay registros</td>
            </tr>
            )}

            {data.content.map(row => (
            <tr key={row.id}>
                {/* Día */}
                <td>{obtenerDia(row.eventTime)}</td>

                {/* Fecha y hora completa */}
                <td>{new Date(row.eventTime).toLocaleString()}</td>

                {/* Puerta */}
                <td>{row.devAlias}</td>

                {/* Tipo: Ingreso / Salida */}
                <td>{obtenerTipo(row.devAlias)}</td>

                {/* Evento */}
                <td>{row.eventDescription || row.eventName}</td>
            </tr>
            ))}
        </tbody>
        </table>

      {/* PAGINACIÓN */}
      <div className="mis-accesos-paginacion">
        <button disabled={data.first} onClick={() => setPage(p => p - 1)}>
          ◀ Anterior
        </button>

        <span>
          Página {data.number + 1} de {data.totalPages}
        </span>

        <button disabled={data.last} onClick={() => setPage(p => p + 1)}>
          Siguiente ▶
        </button>
      </div>

    </div>
  );
}
