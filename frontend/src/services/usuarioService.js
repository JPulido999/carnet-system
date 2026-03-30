import { BACKEND_URL } from "../config/api";
import axios from "axios";
import api from "./api";

const API_URL_USUARIOS = `${BACKEND_URL}/api/usuarios`;
const API_URL_CARNET = `${BACKEND_URL}/api/carnet`;

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
