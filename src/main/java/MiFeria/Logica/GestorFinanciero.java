package MiFeria.Logica;

import MiFeria.Modelo.Categoria;
import MiFeria.Modelo.Transaccion;
import MiFeria.Modelo.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// GestorFinanciero contiene toda la logica principal de la app
// Usa AlmacenamientoLocal para que los datos persistan entre sesiones

public class GestorFinanciero {

    private List<Transaccion> transacciones;
    private List<Categoria>   categorias;
    private Usuario           usuarioActivo;
    private int               contadorId;

    public GestorFinanciero() {
        this.categorias    = new ArrayList<>();
        this.usuarioActivo = null;

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

    // ==================== USUARIO ====================

    // Registra un usuario nuevo y lo guarda en el archivo
    public boolean registrarUsuario(String nombre, String correo, String contrasena) {
        if (AlmacenamientoLocal.correoExiste(correo)) {
            System.out.println("Ese correo ya esta registrado.");
            return false;
        }
        int nuevoId = (int)(Math.random() * 9000) + 1000;
        Usuario nuevo = new Usuario(nuevoId, nombre, correo, contrasena);
        AlmacenamientoLocal.guardarUsuario(nuevo);
        System.out.println("Usuario registrado: " + nombre);
        return true;
    }

    // Guarda o reemplaza el perfil local con el nombre dado
    public void guardarPerfil(String nombre) {
        int id = (usuarioActivo != null) ? usuarioActivo.getId()
                                        : (int)(Math.random() * 9000) + 1000;
        Usuario perfil = new Usuario(id, nombre, "local", "local");
        AlmacenamientoLocal.guardarUsuarioUnico(perfil);
        this.usuarioActivo = perfil;
    }

    // Carga automaticamente el perfil guardado (app local)
    public boolean cargarUsuarioGuardado() {
        Usuario guardado = AlmacenamientoLocal.cargarPrimerUsuario();
        if (guardado != null) {
            this.usuarioActivo = guardado;
            return true;
        }
        return false;
    }

    public void cerrarSesion() { this.usuarioActivo = null; }

    public Usuario getUsuarioActivo() { return usuarioActivo; }

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
        agregarCategoria(new Categoria(1, "Alimentacion",    "#4CAF50"));
        agregarCategoria(new Categoria(2, "Transporte",      "#2196F3"));
        agregarCategoria(new Categoria(3, "Salud",           "#F44336"));
        agregarCategoria(new Categoria(4, "Entretenimiento", "#9C27B0"));
        agregarCategoria(new Categoria(5, "Salario",         "#FF9800"));
        agregarCategoria(new Categoria(6, "Servicios",       "#607D8B"));
        agregarCategoria(new Categoria(7, "Otros",           "#795548"));
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
