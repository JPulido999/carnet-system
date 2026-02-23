import axios from "axios";
import api from "./api";

const API_URL_USUARIOS = "http://localhost:9000/api/usuarios";
const API_URL_CARNET = "http://localhost:9000/api/carnet";

// Mantienes tu función actual
export const obtenerUsuarioPorCorreo = async (correo, token) => {
    const res = await axios.get(`${API_URL_USUARIOS}/correo/${correo}`, {
        headers: { Authorization: `Bearer ${token}` }
    });
    return res.data;
};

// Agregas esta NUEVA función
export const obtenerCarnetMe = async () => {
  const res = await api.get("/api/carnet/me");
  return res.data;
};
