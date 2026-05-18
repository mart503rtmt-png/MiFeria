package MiFeria;

import MiFeria.Logica.GestorFinanciero;
import MiFeria.Modelo.Categoria;
import MiFeria.Modelo.Transaccion;

import java.util.Scanner;

// Main con menu interactivo: el usuario escribe todos los datos
// No hay nada hardcodeado, todo lo ingresa el usuario desde el teclado

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static GestorFinanciero gestor = new GestorFinanciero();

    public static void main(String[] args) {
        pantallaBienvenida();
    }

    // ==================== PANTALLA DE BIENVENIDA ====================

    static void pantallaBienvenida() {
        System.out.println("╔═══════════════════════════════════╗");
        System.out.println("║       MI FERIA - App Financiera   ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println();

        // Carga el nombre guardado para mostrarlo como sugerencia
        gestor.cargarUsuarioGuardado();
        String nombreGuardado = (gestor.getUsuarioActivo() != null)
                ? gestor.getUsuarioActivo().getNombre()
                : "";

        String nombre;
        if (!nombreGuardado.isEmpty()) {
            System.out.println("Usuario anterior: " + nombreGuardado);
            System.out.print("Ingresa tu nombre (Enter para continuar como \"" + nombreGuardado + "\"): ");
            String entrada = scanner.nextLine().trim();
            nombre = entrada.isEmpty() ? nombreGuardado : entrada;
        } else {
            System.out.print("Ingresa tu nombre para comenzar: ");
            nombre = scanner.nextLine().trim();
            while (nombre.isEmpty()) {
                System.out.print("El nombre no puede estar vacio: ");
                nombre = scanner.nextLine().trim();
            }
        }

        // Si el nombre cambio, guarda el nuevo perfil
        if (!nombre.equals(nombreGuardado)) {
            gestor.guardarPerfil(nombre);
        }

        System.out.println();
        System.out.println("Bienvenido, " + nombre + "!");
        System.out.println("───────────────────────────────────");
        menuPrincipal();
    }

    // ==================== MENU PRINCIPAL ====================

    static void menuPrincipal() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("Usuario: " + gestor.getUsuarioActivo().getNombre());
            System.out.println("----------------------");
            System.out.println("1. Agregar transaccion");
            System.out.println("2. Ver todas mis transacciones");
            System.out.println("3. Ver resumen (balance)");
            System.out.println("4. Filtrar por categoria");
            System.out.println("5. Eliminar transaccion");
            System.out.println("6. Cerrar sesion");
            System.out.print("Elegir opcion: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1": agregarTransaccion(); break;
                case "2": verTransacciones();   break;
                case "3": verResumen();          break;
                case "4": filtrarCategoria();    break;
                case "5": eliminarTransaccion(); break;
                case "6":
                    gestor.cerrarSesion();
                    System.out.println("Sesion cerrada.");
                    corriendo = false;
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }

    // ==================== AGREGAR TRANSACCION ====================

    static void agregarTransaccion() {
        System.out.println("\n--- NUEVA TRANSACCION ---");

        // Elegir tipo
        System.out.println("Tipo:");
        System.out.println("1. Ingreso");
        System.out.println("2. Gasto");
        System.out.print("Elegir: ");
        String tipoOpcion = scanner.nextLine().trim();

        String tipo;
        if (tipoOpcion.equals("1")) {
            tipo = "INGRESO";
        } else if (tipoOpcion.equals("2")) {
            tipo = "GASTO";
        } else {
            System.out.println("Opcion no valida.");
            return;
        }

        // Ingresar monto
        System.out.print("Monto ($): ");
        double monto;
        try {
            monto = Double.parseDouble(scanner.nextLine().trim());
            if (monto <= 0) {
                System.out.println("El monto debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Monto invalido, debe ser un numero. Ej: 25.50");
            return;
        }

        // Ingresar descripcion
        System.out.print("Descripcion (ej: compra del super): ");
        String descripcion = scanner.nextLine().trim();
        if (descripcion.isEmpty()) {
            System.out.println("La descripcion no puede estar vacia.");
            return;
        }

        // Mostrar categorias y elegir
        System.out.println("Categorias:");
        for (Categoria c : gestor.getCategorias()) {
            System.out.println("  " + c.getId() + ". " + c.getNombre());
        }
        System.out.print("Numero de categoria: ");
        int idCategoria;
        try {
            idCategoria = Integer.parseInt(scanner.nextLine().trim());
            if (gestor.buscarCategoriaPorId(idCategoria) == null) {
                System.out.println("Categoria no valida.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Numero invalido.");
            return;
        }

        gestor.agregarTransaccion(monto, descripcion, tipo, idCategoria);
        System.out.println("Transaccion guardada correctamente.");
    }

    // ==================== VER TRANSACCIONES ====================

    static void verTransacciones() {
        System.out.println("\n--- MIS TRANSACCIONES ---");
        if (gestor.listarTransacciones().isEmpty()) {
            System.out.println("No hay transacciones registradas aun.");
            return;
        }
        for (Transaccion t : gestor.listarTransacciones()) {
            Categoria cat = gestor.buscarCategoriaPorId(t.getIdCategoria());
            String nombreCat = (cat != null) ? cat.getNombre() : "Sin categoria";
            System.out.println("ID:" + t.getId() +
                    " | " + t.getTipo() +
                    " | $" + t.getMonto() +
                    " | " + t.getDescripcion() +
                    " | " + nombreCat +
                    " | " + t.getFecha());
        }
    }

    // ==================== VER RESUMEN ====================

    static void verResumen() {
        System.out.println("\n--- RESUMEN FINANCIERO ---");
        System.out.printf("Total ingresos : $%.2f%n", gestor.calcularTotalIngresos());
        System.out.printf("Total gastos   : $%.2f%n", gestor.calcularTotalGastos());
        System.out.printf("Balance actual : $%.2f%n", gestor.calcularBalance());
        if (gestor.calcularBalance() >= 0) {
            System.out.println("Estado: Positivo, vas bien!");
        } else {
            System.out.println("Estado: Negativo, cuidado con los gastos!");
        }
    }

    // ==================== FILTRAR POR CATEGORIA ====================

    static void filtrarCategoria() {
        System.out.println("\n--- FILTRAR POR CATEGORIA ---");
        for (Categoria c : gestor.getCategorias()) {
            System.out.println("  " + c.getId() + ". " + c.getNombre());
        }
        System.out.print("Numero de categoria: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Numero invalido.");
            return;
        }

        Categoria cat = gestor.buscarCategoriaPorId(id);
        if (cat == null) {
            System.out.println("Categoria no encontrada.");
            return;
        }

        System.out.println("Transacciones de: " + cat.getNombre());
        java.util.List<Transaccion> lista = gestor.filtrarPorCategoria(id);
        if (lista.isEmpty()) {
            System.out.println("No hay transacciones en esta categoria.");
            return;
        }
        for (Transaccion t : lista) {
            System.out.println("  ID:" + t.getId() +
                    " | " + t.getTipo() +
                    " | $" + t.getMonto() +
                    " | " + t.getDescripcion() +
                    " | " + t.getFecha());
        }
    }

    // ==================== ELIMINAR TRANSACCION ====================

    static void eliminarTransaccion() {
        System.out.println("\n--- ELIMINAR TRANSACCION ---");
        verTransacciones();
        if (gestor.listarTransacciones().isEmpty()) return;

        System.out.print("ID de la transaccion a eliminar: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
            return;
        }

        System.out.print("Seguro que deseas eliminar la transaccion " + id + "? (s/n): ");
        String confirmacion = scanner.nextLine().trim();
        if (confirmacion.equalsIgnoreCase("s")) {
            gestor.eliminarTransaccion(id);
        } else {
            System.out.println("Operacion cancelada.");
        }
    }
}