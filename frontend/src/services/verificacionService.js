import api from "./api";

const API_URL = "http://localhost:9000/verificacion";

export async function obtenerVerificacionPorUuid(uuid) {
  const res = await api.get(`/verificacion/${uuid}`);
  return res.data;
}

export async function obtenerVerificacionManual(params) {
  const res = await api.get("/verificacion/manual", { params });
  return res.data;
}