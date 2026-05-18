package MiFeria.Logica;

import MiFeria.Modelo.Categoria;
import MiFeria.Modelo.Transaccion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// GestorFinanciero contiene toda la logica principal de la app
// Usa AlmacenamientoLocal para que los datos persistan entre sesiones

public class GestorFinanciero {

    private List<Transaccion> transacciones;
    private List<Categoria>   categorias;
    private String            nombreActivo;
    private int               contadorId;

    public GestorFinanciero() {
        this.categorias   = new ArrayList<>();
        this.nombreActivo = null;

        // Carga transacciones guardadas al iniciar
        this.transacciones = AlmacenamientoLocal.cargarTransacciones();

        // El id empieza desde el ultimo guardado + 1
        this.contadorId = calcularUltimoId() + 1;

        cargarCategoriasPorDefecto();
    }

    // Busca el id mas alto entre las transacciones cargadas
    private int calcularUltimoId() {
        int maxId = 0;
        for (Transaccion t : transacciones) {
            if (t.getId() > maxId) maxId = t.getId();
        }
        return maxId;
    }

    // ==================== PERFIL ====================

    // Guarda el nombre en archivo y lo activa
    public void guardarPerfil(String nombre) {
        AlmacenamientoLocal.guardarNombre(nombre);
        this.nombreActivo = nombre;
    }

    // Carga el nombre guardado desde archivo
    public boolean cargarPerfilGuardado() {
        String nombre = AlmacenamientoLocal.cargarNombre();
        if (nombre != null) {
            this.nombreActivo = nombre;
            return true;
        }
        return false;
    }

    public String getNombreActivo() { return nombreActivo; }

    public void cerrarSesion() { this.nombreActivo = null; }

    // ==================== CATEGORIAS ====================

    public void agregarCategoria(Categoria c) { categorias.add(c); }

    public List<Categoria> getCategorias() { return categorias; }

    public Categoria buscarCategoriaPorId(int id) {
        for (Categoria c : categorias) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public void cargarCategoriasPorDefecto() {
        // Categorias de gastos (ids 1-7)
        agregarCategoria(new Categoria(1, "Alimentacion",    "#4CAF50"));
        agregarCategoria(new Categoria(2, "Transporte",      "#2196F3"));
        agregarCategoria(new Categoria(3, "Salud",           "#F44336"));
        agregarCategoria(new Categoria(4, "Entretenimiento", "#9C27B0"));
        agregarCategoria(new Categoria(5, "Salario",         "#FF9800"));
        agregarCategoria(new Categoria(6, "Servicios",       "#607D8B"));
        agregarCategoria(new Categoria(7, "Otros",           "#795548"));

        // Categorias de ingresos (ids 8-9)
        agregarCategoria(new Categoria(8, "Ingreso Constante", "#00BCD4"));
        agregarCategoria(new Categoria(9, "Ingreso Parcial",   "#8BC34A"));
    }

    // Devuelve solo las categorias de ingresos
    public List<Categoria> getCategoriasIngreso() {
        List<Categoria> resultado = new ArrayList<>();
        for (Categoria c : categorias) {
            if (c.getId() == 8 || c.getId() == 9) resultado.add(c);
        }
        return resultado;
    }

    // Devuelve solo las categorias de gastos
    public List<Categoria> getCategoriasGasto() {
        List<Categoria> resultado = new ArrayList<>();
        for (Categoria c : categorias) {
            if (c.getId() >= 1 && c.getId() <= 7) resultado.add(c);
        }
        return resultado;
    }

    // ==================== TRANSACCIONES ====================

    // Agrega una transaccion, la guarda en archivo y en la lista
    public void agregarTransaccion(double monto, String descripcion,
                                   String tipo, int idCategoria) {
        Transaccion nueva = new Transaccion(
            contadorId, monto, descripcion,
            LocalDate.now(), tipo, idCategoria
        );
        transacciones.add(nueva);
        AlmacenamientoLocal.guardarTransaccion(nueva);
        contadorId++;
        System.out.println("Guardada: " + nueva);
    }

    // Elimina de la lista y del archivo
    public void eliminarTransaccion(int id) {
        transacciones.removeIf(t -> t.getId() == id);
        AlmacenamientoLocal.eliminarTransaccion(id);
        System.out.println("Eliminada transaccion id: " + id);
    }

    public List<Transaccion> listarTransacciones() { return transacciones; }

    public List<Transaccion> listarIngresos() {
        List<Transaccion> resultado = new ArrayList<>();
        for (Transaccion t : transacciones)
            if (t.getTipo().equals("INGRESO")) resultado.add(t);
        return resultado;
    }

    public List<Transaccion> listarGastos() {
        List<Transaccion> resultado = new ArrayList<>();
        for (Transaccion t : transacciones)
            if (t.getTipo().equals("GASTO")) resultado.add(t);
        return resultado;
    }

    public List<Transaccion> filtrarPorCategoria(int idCategoria) {
        List<Transaccion> resultado = new ArrayList<>();
        for (Transaccion t : transacciones)
            if (t.getIdCategoria() == idCategoria) resultado.add(t);
        return resultado;
    }

    // ==================== CALCULOS ====================

    public double calcularTotalIngresos() {
        double total = 0;
        for (Transaccion t : transacciones)
            if (t.getTipo().equals("INGRESO")) total += t.getMonto();
        return total;
    }

    public double calcularTotalGastos() {
        double total = 0;
        for (Transaccion t : transacciones)
            if (t.getTipo().equals("GASTO")) total += t.getMonto();
        return total;
    }

    public double calcularBalance() {
        return calcularTotalIngresos() - calcularTotalGastos();
    }
}
