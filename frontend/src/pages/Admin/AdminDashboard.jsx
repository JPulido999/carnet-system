import { useState } from "react";
import Estadisticas from "./Estadisticas";
import AccesosTabla from "./AccesosTabla";
import FiltrosFecha from "./FiltrosFecha";

import "./AdminDashboard.css";

export default function AdminDashboard() {

  // 📝 lo que el usuario escribe
  const [form, setForm] = useState({
    startDate: "2025-06-01T00:00:00",
    endDate: "2025-07-25T23:59:59",
    dni: "",
    nombre: "",
    departamento: ""
  });

  // 🔎 lo que realmente se usa para buscar
  const [filtro, setFiltro] = useState(form);

  const aplicarFiltro = () => {
    setFiltro({ ...form }); // 👈 solo aquí dispara la búsqueda
  };

  return (
    <div className="admin-dashboard-container">
      <h1 className="admin-title">Registro de accesos - Puertas 1, 2 y 4</h1>

      <FiltrosFecha
        form={form}
        setForm={setForm}
        onBuscar={aplicarFiltro}
      />

      <Estadisticas filtro={filtro} />

      <AccesosTabla filtro={filtro} />
    </div>
  );
}
