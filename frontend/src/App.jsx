import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "./context/AuthContext";

import Home from "./pages/Home/Home";
import Dashboard from "./pages/Dashboard/Dashboard";
import AdminDashboard from "./pages/Admin/AdminDashboard";

import ErrorPage from "./pages/ErrorPage/ErrorPage";
import RutaProtegida from "./components/RutaProtegida";
import AppLayout from "./components/AppLayout";

import VerificacionUsuario from "./pages/Verification/VerificacionUsuario.jsx";
import EscanearQR from "./pages/Verification/EscanearQR";
import VerificacionManual from "./pages/Verification/VerificacionManual";


export default function App() {
    const { token } = useContext(AuthContext);

    console.log("TOKEN ACTUAL:", token);

    return (
        <BrowserRouter>
            <AppLayout>
                <Routes>

                    <Route path="/" element={<Home />} />

                    {/* RUTA PROTEGIDA */}
                    <Route
                        path="/dashboard"
                        element={
                            <RutaProtegida roles={["ESTUDIANTE", "VIGILANTE", "ADMIN_SISTEMA"]}>
                                <Dashboard />
                            </RutaProtegida>
                        }
                    />

                    {/* VERIFICACIÓN SIN LOGIN */}
                    <Route
                        path="/verificacion/:uuid"
                        element={
                            <RutaProtegida roles={["VIGILANTE", "ADMIN_SISTEMA"]}>
                                <VerificacionUsuario />
                            </RutaProtegida>
                        }
                    />

                    <Route
                        path="/verificacion/scan"
                        element={
                            <RutaProtegida roles={["VIGILANTE", "ADMIN_SISTEMA"]}>
                                <EscanearQR />
                            </RutaProtegida>
                        }
                    />

                    <Route
                        path="/verificacion/manual"
                        element={
                            <RutaProtegida roles={["VIGILANTE", "ADMIN_SISTEMA"]}>
                                <VerificacionManual />
                            </RutaProtegida>
                        }
                    />

                    <Route
                        path="/admin"
                        element={
                            <RutaProtegida roles={["ADMIN_SISTEMA"]}>
                            <AdminDashboard />
                            </RutaProtegida>
                        }
                    />


                    <Route path="/error" element={<ErrorPage />} />
                    <Route path="*" element={<ErrorPage />} />
                </Routes>
            </AppLayout>
        </BrowserRouter>
    );
}
