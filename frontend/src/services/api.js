import { BACKEND_URL } from "../config/api";
import axios from "axios";

const api = axios.create({
  baseURL: BACKEND_URL
});

api.interceptors.request.use(config => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
