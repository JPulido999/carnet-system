import { useState } from "react";
import Estadisticas from "./Estadisticas";
import AccesosTabla from "./AccesosTabla";
import FiltrosFecha from "./FiltrosFecha";

import "./AdminDashboard.css";

// 🔧 función para formatear fecha en LOCAL (evita problemas de zona horaria)
const formatLocal = (fecha) => {
  const pad = (n) => String(n).padStart(2, "0");

  return `${fecha.getFullYear()}-${pad(fecha.getMonth() + 1)}-${pad(fecha.getDate())}T${pad(fecha.getHours())}:${pad(fecha.getMinutes())}:${pad(fecha.getSeconds())}`;
};

// 🔧 obtiene hoy (inicio y fin del día)
const getFechasHoy = () => {
  const hoy = new Date();

  const inicio = new Date(hoy);
  inicio.setHours(0, 0, 0, 0);

  const fin = new Date(hoy);
  fin.setHours(23, 59, 59, 999);

  return {
    startDate: formatLocal(inicio),
    endDate: formatLocal(fin)
  };
};

export default function AdminDashboard() {

  // 📝 lo que el usuario escribe (inicia con HOY)
  const [form, setForm] = useState({
    ...getFechasHoy(),
    dni: "",
    nombre: "",
    departamento: ""
  });

  // 🔎 lo que realmente se usa para buscar
  const [filtro, setFiltro] = useState({
    ...getFechasHoy(),
    dni: "",
    nombre: "",
    departamento: ""
  });

  const aplicarFiltro = () => {
    setFiltro({ ...form }); // 👈 dispara la búsqueda
  };

  return (
    <div className="admin-dashboard-container">
      <h1 className="admin-title">
        Registro de accesos - Puertas 1, 2 y 4
      </h1>

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