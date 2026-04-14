package MiFeria.Logica;

import MiFeria.Modelo.Categoria;
import MiFeria.Modelo.Transaccion;
import MiFeria.Modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

// GestorFinanciero es la clase mas importante de la primera entrega
// Aqui vive toda la logica: agregar transacciones, calcular balance, filtrar, etc.

public class GestorFinanciero {

    // Listas donde se guardan los datos en memoria
    // (en entregas futuras esto se conectaria a una base de datos)
    private List<Transaccion> transacciones;
    private List<Categoria> categorias;
    private Usuario usuarioActivo; // el usuario que inicio sesion

    // Constructor: inicializa las listas vacias
    public GestorFinanciero() {
        this.transacciones = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.usuarioActivo = null;
    }

    // ==================== USUARIO ====================

    // Simula el login: busca si el usuario existe y la contrasena es correcta
    public boolean iniciarSesion(String correo, String contrasena) {
        // Por ahora creamos un usuario de prueba hardcodeado
        // En entregas futuras esto vendria de una base de datos
        Usuario usuarioPrueba = new Usuario(1, "Jose Argueta", correo, "1234");

        if (usuarioPrueba.getCorreo().equals(correo) &&
            usuarioPrueba.validarContrasena(contrasena)) {
            this.usuarioActivo = usuarioPrueba;
            return true; // login exitoso
        }
        return false; // login fallido
    }

    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    // ==================== CATEGORIAS ====================

    // Agregar una nueva categoria a la lista
    public void agregarCategoria(Categoria categoria) {
        categorias.add(categoria);
    }

    // Devuelve todas las categorias (util para llenar un ComboBox en JavaFX)
    public List<Categoria> getCategorias() {
        return categorias;
    }

    // Busca una categoria por su id
    public Categoria buscarCategoriaPorId(int id) {
        for (Categoria c : categorias) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null; // si no la encuentra devuelve null
    }

    // Carga categorias basicas para que la app tenga datos desde el inicio
    public void cargarCategoriasPorDefecto() {
        agregarCategoria(new Categoria(1, "Alimentacion", "#4CAF50"));
        agregarCategoria(new Categoria(2, "Transporte",   "#2196F3"));
        agregarCategoria(new Categoria(3, "Salud",        "#F44336"));
        agregarCategoria(new Categoria(4, "Entretenimiento", "#9C27B0"));
        agregarCategoria(new Categoria(5, "Salario",      "#FF9800"));
        agregarCategoria(new Categoria(6, "Servicios",    "#607D8B"));
        agregarCategoria(new Categoria(7, "Otros",        "#795548"));
    }

    // ==================== TRANSACCIONES ====================

    // Agregar una nueva transaccion a la lista
    public void agregarTransaccion(Transaccion transaccion) {
        transacciones.add(transaccion);
    }

    // Devuelve todas las transacciones
    public List<Transaccion> listarTransacciones() {
        return transacciones;
    }

    // Filtra y devuelve solo los ingresos
    public List<Transaccion> listarIngresos() {
        List<Transaccion> ingresos = new ArrayList<>();
        for (Transaccion t : transacciones) {
            if (t.getTipo().equals("INGRESO")) {
                ingresos.add(t);
            }
        }
        return ingresos;
    }

    // Filtra y devuelve solo los gastos
    public List<Transaccion> listarGastos() {
        List<Transaccion> gastos = new ArrayList<>();
        for (Transaccion t : transacciones) {
            if (t.getTipo().equals("GASTO")) {
                gastos.add(t);
            }
        }
        return gastos;
    }

    // Filtra transacciones por categoria
    public List<Transaccion> filtrarPorCategoria(int idCategoria) {
        List<Transaccion> resultado = new ArrayList<>();
        for (Transaccion t : transacciones) {
            if (t.getIdCategoria() == idCategoria) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    // ==================== CALCULOS ====================

    // Suma todos los ingresos
    public double calcularTotalIngresos() {
        double total = 0;
        for (Transaccion t : transacciones) {
            if (t.getTipo().equals("INGRESO")) {
                total += t.getMonto();
            }
        }
        return total;
    }

    // Suma todos los gastos
    public double calcularTotalGastos() {
        double total = 0;
        for (Transaccion t : transacciones) {
            if (t.getTipo().equals("GASTO")) {
                total += t.getMonto();
            }
        }
        return total;
    }

    // Calcula el balance: ingresos menos gastos
    // Si es positivo tienes dinero, si es negativo estas en deficit
    public double calcularBalance() {
        return calcularTotalIngresos() - calcularTotalGastos();
    }
}
