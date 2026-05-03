const BACKEND_URL =
  import.meta.env.DEV
    ? "http://localhost:9000/api"
    : "/api";

export { BACKEND_URL };