import api from "./api";

export const obtenerTransacciones = async ({
  startDate,
  endDate,
  dni,
  nombre,
  departamento,
  page = 0,
  size = 50
}) => {
  const res = await api.get("/api/transactions", {
    params: { startDate, endDate, dni, nombre, departamento, page, size }
  });
  return res.data;
};

export function obtenerMisAccesos({ startDate, endDate, page = 0, size = 20, dni }) {
  return api.get("/api/transactions/mis-accesos", {
    params: { startDate, endDate, page, size, dni }
  }).then(r => r.data);
}

export const obtenerTotalAccesos = async (startDate, endDate) => {
  const res = await api.get("/api/transactions/total", {
    params: { startDate, endDate }
  });
  return res.data;
};

export const obtenerAccesosPorDispositivo = async (startDate, endDate) => {
  const res = await api.get("/api/transactions/por-dispositivo", {
    params: { startDate, endDate }
  });
  return res.data;
};

export const obtenerTotalFiltrado = async (params) => {
  const res = await api.get("/api/transactions/total-filtrado", { params });
  return res.data;
};

