import { useEffect, useState, useContext } from "react";
import "./GestionUsuarios.css";
import { AuthContext } from "../../context/AuthContext";
import { UserPlus } from "lucide-react";

export default function GestionUsuarios() {

    const { token } = useContext(AuthContext);

    const [usuarios, setUsuarios] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [search, setSearch] = useState("");
    const [loading, setLoading] = useState(false);

    const [mostrarModal, setMostrarModal] = useState(false);
    const [modoEdicion, setModoEdicion] = useState(false);

    const [preview, setPreview] = useState(null);
    const [toast, setToast] = useState(null);
    const [confirmDelete, setConfirmDelete] = useState(null);

    const [usuarioActual, setUsuarioActual] = useState({
        id: null,
        nombres: "",
        apellidos: "",
        dni: "",
        correo: "",
        rol: "ESTUDIANTE"
    });

    // 🔄 CARGAR USUARIOS
    const cargarUsuarios = async () => {
        if (!token) return;

        setLoading(true);

        try {
            const res = await fetch(
                `http://localhost:9000/api/usuarios/page?search=${search}&page=${page}&size=10`,
                {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                }
            );

            if (!res.ok) throw new Error("Error al cargar usuarios");

            const data = await res.json();

            setUsuarios(data.content);
            setTotalPages(data.totalPages);

        } catch (err) {
            console.error("Error:", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        cargarUsuarios();
    }, [page, token, search]);

    // 🔍 BUSCAR
    const buscar = () => {
        setPage(0);
    };

    // 🗑 ELIMINAR
    const eliminar = (id) => {
        setConfirmDelete(id);
    };

    const confirmarEliminar = async () => {
        try {
            await fetch(`http://localhost:9000/api/usuarios/${confirmDelete}`, {
                method: "DELETE",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });

            setToast({ type: "success", msg: "Usuario eliminado" });
            setConfirmDelete(null);
            cargarUsuarios();

        } catch {
            setToast({ type: "error", msg: "Error al eliminar" });
        }
    };

    useEffect(() => {
        if (toast) {
            const t = setTimeout(() => setToast(null), 3000);
            return () => clearTimeout(t);
        }
    }, [toast]);

    // ✏️ EDITAR
    const editar = (u) => {
        setModoEdicion(true);

        setUsuarioActual({
            ...u,
            file: null
        });

        if (u.fotoCarnetUrl) {
            const url = u.fotoCarnetUrl.startsWith("http")
                ? u.fotoCarnetUrl
                : `http://localhost:9000/${u.fotoCarnetUrl}`;

            setPreview(url);
        } else {
            setPreview(null);
        }

        setMostrarModal(true);
    };

    // ➕ NUEVO
    const nuevoUsuario = () => {
        setModoEdicion(false);
        setUsuarioActual({
            id: null,
            nombres: "",
            apellidos: "",
            dni: "",
            correo: "",
            rol: "ESTUDIANTE",
            file: null
        });

        setPreview(null); // 🔥 IMPORTANTE

        setMostrarModal(true);
    };

    // 💾 GUARDAR
    // 💾 GUARDAR
    const guardar = async () => {
        try {
            const formData = new FormData();

            formData.append("usuario", JSON.stringify({
                nombres: usuarioActual.nombres,
                apellidos: usuarioActual.apellidos,
                dni: usuarioActual.dni,
                correo: usuarioActual.correo,
                rol: usuarioActual.rol
            }));

            if (usuarioActual.file) {
                formData.append("file", usuarioActual.file);
            }

            // 🔥 CAMBIO CLAVE AQUÍ
            const metodo = modoEdicion ? "PUT" : "POST";

            const url = modoEdicion
                ? `http://localhost:9000/api/usuarios/${usuarioActual.id}/con-foto`
                : `http://localhost:9000/api/usuarios`;

            const res = await fetch(url, {
                method: metodo,
                headers: {
                    "Authorization": `Bearer ${token}`
                    // ❌ NO pongas Content-Type
                },
                body: formData
            });

            if (!res.ok) {
                const error = await res.text();
                throw new Error(error);
            }

            setMostrarModal(false);
            setToast({ type: "success", msg: modoEdicion ? "Usuario actualizado" : "Usuario creado" });
setPreview(null);
            cargarUsuarios();

        } catch (err) {
            console.error("Error al guardar:", err);
            alert("Error al guardar usuario");
            setToast({ type: "error", msg: "Error al guardar" });
        }
    };

    return (
        <div className="usuarios-container">

            <h1>GESTIÓN DE USUARIOS</h1>

            {/* 🔍 BUSCADOR */}
            <div className="buscador">
                <input
                    placeholder="Buscar por DNI, nombre..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />
                <button onClick={buscar} disabled={loading}>
                    Buscar
                </button>
            </div>

            {/* ➕ NUEVO */}
            <button className="btn-new" onClick={nuevoUsuario}>
                <UserPlus size={18} className="icon-btn" /> 
                Nuevo Usuario
            </button>

            {loading && <p>Cargando...</p>}

            {/* 📊 TABLA */}
            <table className="tabla-usuarios">
                <thead>
                    <tr>
                        <th>DNI</th>
                        <th>Nombre</th>
                        <th>Correo</th>
                        <th>Rol</th>
                        <th>Acciones</th>
                    </tr>
                </thead>

                <tbody>
                    {usuarios.map(u => (
                        <tr key={u.id}>
                            <td>{u.dni}</td>
                            <td>{u.nombres} {u.apellidos}</td>
                            <td>{u.correo}</td>
                            <td>
                                <span className={`rol ${u.rol?.toLowerCase()}`}>
                                    {u.rol}
                                </span>
                            </td>
                            <td>
                                <button
                                    className="btn-edit"
                                    onClick={() => editar(u)}
                                >
                                    ✏️
                                </button>

                                <button
                                    className="btn-delete"
                                    onClick={() => eliminar(u.id)}
                                >
                                    🗑
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {/* 🔁 PAGINACIÓN */}
            <div className="paginacion">
                <button
                    disabled={page === 0 || loading}
                    onClick={() => setPage(p => p - 1)}
                >
                    ◀
                </button>

                <span>
                    Página {page + 1} de {totalPages}
                </span>

                <button
                    disabled={page + 1 === totalPages || loading}
                    onClick={() => setPage(p => p + 1)}
                >
                    ▶
                </button>
            </div>

            {/* 🧾 MODAL */}
            {mostrarModal && (
                <div className="modal-overlay">

                    <div className="modal">

                        <h2>
                            {modoEdicion ? "Editar Usuario" : "Nuevo Usuario"}
                        </h2>

                        <input
                            placeholder="Nombres"
                            value={usuarioActual.nombres}
                            onChange={(e) =>
                                setUsuarioActual({
                                    ...usuarioActual,
                                    nombres: e.target.value
                                })
                            }
                        />

                        <input
                            placeholder="Apellidos"
                            value={usuarioActual.apellidos}
                            onChange={(e) =>
                                setUsuarioActual({
                                    ...usuarioActual,
                                    apellidos: e.target.value
                                })
                            }
                        />

                        <input
                            placeholder="DNI"
                            value={usuarioActual.dni}
                            onChange={(e) =>
                                setUsuarioActual({
                                    ...usuarioActual,
                                    dni: e.target.value
                                })
                            }
                        />

                        <input
                            placeholder="Correo"
                            value={usuarioActual.correo}
                            onChange={(e) =>
                                setUsuarioActual({
                                    ...usuarioActual,
                                    correo: e.target.value
                                })
                            }
                        />

                        <div className="file-upload">
                            <label className="file-label">
                                📷 Seleccionar imagen
                                <input
                                    type="file"
                                    accept="image/*"
                                    onChange={(e) => {
                                        const file = e.target.files[0];

                                        setUsuarioActual({
                                            ...usuarioActual,
                                            file
                                        });

                                        if (file) {
                                            setPreview(URL.createObjectURL(file));
                                        }
                                    }}
                                />
                            </label>

                            {preview && (
                                <div className="preview-container">
                                    <img src={preview} alt="preview" />

                                    <button
                                        className="remove-img"
                                        onClick={() => {
                                            setPreview(null);
                                            setUsuarioActual({
                                                ...usuarioActual,
                                                file: null
                                            });
                                        }}
                                    >
                                        ✕
                                    </button>
                                </div>
                            )}
                        </div>

                        <select
                            value={usuarioActual.rol}
                            onChange={(e) =>
                                setUsuarioActual({
                                    ...usuarioActual,
                                    rol: e.target.value
                                })
                            }
                        >
                            <option value="ESTUDIANTE">ESTUDIANTE</option>
                            <option value="VIGILANTE">VIGILANTE</option>
                            <option value="ADMIN_SISTEMA">ADMIN</option>
                        </select>

                        <div className="modal-actions">
                            <button className="btn-save" onClick={guardar}>
                                {modoEdicion ? "Actualizar" : "Crear"}
                            </button>

                            <button
                                className="btn-cancel"
                                onClick={() => setMostrarModal(false)}
                            >
                                Cancelar
                            </button>
                        </div>

                    </div>
                </div>
            )}

            {confirmDelete && (
                <div className="modal-overlay">
                    <div className="modal confirm">
                        <h3>¿Eliminar usuario?</h3>
                        <p>Esta acción no se puede deshacer</p>

                        <div className="modal-actions">
                            <button className="btn-delete" onClick={confirmarEliminar}>
                                Sí, eliminar
                            </button>
                            <button
                                className="btn-cancel"
                                onClick={() => setConfirmDelete(null)}
                            >
                                Cancelar
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {toast && (
                <div className={`toast ${toast.type}`}>
                    {toast.msg}
                </div>
            )}

        </div>
    );
}