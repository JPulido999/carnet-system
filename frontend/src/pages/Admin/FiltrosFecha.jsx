export default function FiltrosFecha({ form, setForm, onBuscar }) {

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: value
    }));
  };

  return (
    <div className="admin-filtros">
      <label>Inicio</label>
      <input type="datetime-local" name="startDate" value={form.startDate} onChange={handleChange} />

      <label>Fin</label>
      <input type="datetime-local" name="endDate" value={form.endDate} onChange={handleChange} />

      <label>DNI</label>
      <input type="text" name="dni" value={form.dni} onChange={handleChange} />

      <label>Nombre</label>
      <input type="text" name="nombre" value={form.nombre} onChange={handleChange} />

      <label>Departamento</label>
      <input type="text" name="departamento" value={form.departamento} onChange={handleChange} />

      <button onClick={onBuscar}>🔍 Buscar</button>
    </div>
  );
}
