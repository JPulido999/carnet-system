import { useEffect, useState } from "react";
import { obtenerTransacciones } from "../../services/zktecoService";

export default function AccesosTabla({ filtro }) {

  const [data, setData] = useState(null);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);

  // 🔁 Resetear página cuando cambia el filtro
  useEffect(() => {
    setPage(0);
  }, [filtro]);

  // 📡 Cargar datos cuando cambia filtro o página
  useEffect(() => {
    let cancel = false;

    setLoading(true);

    obtenerTransacciones({
      ...filtro,
      page,
      size: 50
    })
      .then(res => {
        if (!cancel) {
          setData(res);
        }
      })
      .finally(() => {
        if (!cancel) setLoading(false);
      });

    return () => {
      cancel = true; // evita race conditions
    };

  }, [filtro, page]);

  if (loading && !data) {
    return <p>Cargando accesos desde ZKTeco...</p>;
  }

  if (!data) {
    return <p>Cargando...</p>;
  }

  if (data.content.length === 0) {
    return <p>No hay registros para el rango seleccionado.</p>;
  }

  return (
    <div className="admin-table-container">

      <table className="admin-table">
        <thead>
          <tr>
            <th>Fecha y hora</th>
            <th>Puerta</th>
            <th>Evento</th>
            <th>DNI</th>
            <th>Nombres</th>
            <th>Apellidos</th>
            <th>Departamento</th>
          </tr>
        </thead>
        <tbody>
          {data.content.map(row => (
            <tr key={row.id}>
              <td>{new Date(row.eventTime).toLocaleString()}</td>
              <td>{row.devAlias}</td>
              <td>{row.eventDescription || row.eventName}</td>
              <td>{row.personPin}</td>
              <td>{row.personName}</td>
              <td>{row.personLastName}</td>
              <td>{row.departmentName}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="admin-pagination">
        <button
          disabled={data.first}
          onClick={() => setPage(p => p - 1)}
        >
          ◀ Anterior
        </button>

        <span style={{ fontWeight: 600 }}>
          Página {data.number + 1} de {data.totalPages}
        </span>

        <button
          disabled={data.last}
          onClick={() => setPage(p => p + 1)}
        >
          Siguiente ▶
        </button>
      </div>

    </div>
  );
}
