package MiFeria;

import MiFeria.Logica.GestorFinanciero;
import MiFeria.Modelo.Categoria;
import MiFeria.Modelo.Transaccion;
import MiFeria.Modelo.Meta;

import java.util.Scanner;
import java.util.List;

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
        gestor.cargarPerfilGuardado();
        String nombreGuardado = (gestor.getNombreActivo() != null)
                ? gestor.getNombreActivo()
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
            System.out.println("Usuario: " + gestor.getNombreActivo());
            System.out.println("----------------------");
            System.out.println("1. Agregar transaccion");
            System.out.println("2. Ver todas mis transacciones");
            System.out.println("3. Ver resumen (balance)");
            System.out.println("4. Filtrar por categoria");
            System.out.println("5. Eliminar transaccion");
            System.out.println("6. Metas de ahorro");
            System.out.println("7. Cerrar sesion");
            System.out.print("Elegir opcion: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1": agregarTransaccion(); break;
                case "2": verTransacciones();   break;
                case "3": verResumen();          break;
                case "4": filtrarCategoria();    break;
                case "5": eliminarTransaccion(); break;
                case "6": menuMetas(); break;

                case "7":
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
        String tipoOpcion;
        while (true) {
            System.out.println("Tipo:");
            System.out.println("1. Ingreso");
            System.out.println("2. Gasto");
            System.out.print("Elegir: ");
            tipoOpcion = scanner.nextLine().trim();
            if (tipoOpcion.equals("1") || tipoOpcion.equals("2")) break;
            System.out.println("Error: opcion no valida, escribe 1 o 2.");
        }

        if (tipoOpcion.equals("1")) {
            agregarIngreso();
        } else {
            agregarGasto();
        }
    }

    // ==================== AGREGAR INGRESO ====================

    static void agregarIngreso() {
        System.out.println("\n--- NUEVO INGRESO ---");

        // Nombre del ingreso
        String nombre;
        while (true) {
            System.out.print("Nombre del ingreso (ej: sueldo, freelance, venta): ");
            nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) break;
            System.out.println("Error: el nombre no puede estar vacio.");
        }

        // Monto
        double monto;
        while (true) {
            System.out.print("Monto ($): ");
            try {
                monto = Double.parseDouble(scanner.nextLine().trim());
                if (monto > 0) break;
                System.out.println("Error: el monto debe ser mayor a 0.");
            } catch (NumberFormatException e) {
                System.out.println("Error: ingresa un numero valido. Ej: 500.00");
            }
        }

        // Categoria de ingreso
        int idCategoria;
        while (true) {
            System.out.println("Tipo de ingreso:");
            System.out.println("  8. Ingreso Constante  (salario fijo, pension, renta...)");
            System.out.println("  9. Ingreso Parcial    (freelance, venta ocasional, bono...)");
            System.out.print("Elegir (8 o 9): ");
            try {
                idCategoria = Integer.parseInt(scanner.nextLine().trim());
                if (idCategoria == 8 || idCategoria == 9) break;
                System.out.println("Error: opcion no valida, escribe 8 o 9.");
            } catch (NumberFormatException e) {
                System.out.println("Error: escribe 8 o 9.");
            }
        }

        gestor.agregarTransaccion(monto, nombre, "INGRESO", idCategoria);
        System.out.println("Ingreso guardado correctamente.");
    }

    // ==================== AGREGAR GASTO ====================

    static void agregarGasto() {
        System.out.println("\n--- NUEVO GASTO ---");

        // Monto
        double monto;
        while (true) {
            System.out.print("Monto ($): ");
            try {
                monto = Double.parseDouble(scanner.nextLine().trim());
                if (monto > 0) break;
                System.out.println("Error: el monto debe ser mayor a 0.");
            } catch (NumberFormatException e) {
                System.out.println("Error: ingresa un numero valido. Ej: 25.50");
            }
        }

        // Descripcion
        String descripcion;
        while (true) {
            System.out.print("Descripcion (ej: compra del super): ");
            descripcion = scanner.nextLine().trim();
            if (!descripcion.isEmpty()) break;
            System.out.println("Error: la descripcion no puede estar vacia.");
        }

        // Categoria de gasto
        int idCategoria;
        while (true) {
            System.out.println("Categoria:");
            for (Categoria c : gestor.getCategoriasGasto()) {
                System.out.println("  " + c.getId() + ". " + c.getNombre());
            }
            System.out.print("Numero de categoria: ");
            try {
                idCategoria = Integer.parseInt(scanner.nextLine().trim());
                if (gestor.buscarCategoriaPorId(idCategoria) != null && idCategoria <= 7) break;
                System.out.println("Error: categoria no valida, elige un numero de la lista.");
            } catch (NumberFormatException e) {
                System.out.println("Error: ingresa el numero de la categoria.");
            }
        }

        gestor.agregarTransaccion(monto, descripcion, "GASTO", idCategoria);
        System.out.println("Gasto guardado correctamente.");
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

        int id;
        Categoria cat;
        while (true) {
            for (Categoria c : gestor.getCategorias()) {
                System.out.println("  " + c.getId() + ". " + c.getNombre());
            }
            System.out.print("Numero de categoria: ");
            try {
                id = Integer.parseInt(scanner.nextLine().trim());
                cat = gestor.buscarCategoriaPorId(id);
                if (cat != null) break;
                System.out.println("Error: categoria no encontrada, elige un numero de la lista.");
            } catch (NumberFormatException e) {
                System.out.println("Error: ingresa el numero de la categoria.");
            }
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

        int id;
        while (true) {
            System.out.print("ID de la transaccion a eliminar: ");
            try {
                id = Integer.parseInt(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: ingresa un ID valido (solo numeros).");
            }
        }

        String confirmacion;
        while (true) {
            System.out.print("Seguro que deseas eliminar la transaccion " + id + "? (s/n): ");
            confirmacion = scanner.nextLine().trim().toLowerCase();
            if (confirmacion.equals("s") || confirmacion.equals("n")) break;
            System.out.println("Error: escribe 's' para confirmar o 'n' para cancelar.");
        }

        if (confirmacion.equals("s")) {
            gestor.eliminarTransaccion(id);
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    // ====================== METAS DE AHORRO ======================

    static void menuMetas() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n=== METAS DE AHORRO ===");
            System.out.println("1. Crear meta");
            System.out.println("2. Abonar a una meta");
            System.out.println("3. Ver mis metas");
            System.out.println("4. Volver al menu principal");
            System.out.print("Elegir opcion: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1": crearMeta(); break;
                case "2": abonarMeta(); break;
                case "3": verMetas(); break;
                case "4": corriendo = false; break;
                default: System.out.println("Opcion no valida."); break;
            }
        }
    }

    static void crearMeta() {
        System.out.println("\n=== CREAR META ===");

        // Nombre
        System.out.print("Nombre de la meta: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }

        // Monto objetivo
        double montoObjetivo = 0;
        boolean montoValido = false;
        while (!montoValido) {
            System.out.print("Monto objetivo: $");
            String input = scanner.nextLine().trim();
            try {
                montoObjetivo = Double.parseDouble(input);
                if (montoObjetivo <= 0) {
                    System.out.println("El monto debe ser mayor a 0.");
                } else {
                    montoValido = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un numero valido.");
            }
        }

        gestor.crearMeta(nombre, montoObjetivo);
    }

    static void abonarMeta() {
        System.out.println("\n=== ABONAR A META ===");

        List<Meta> metas = gestor.listarMetas();
        if (metas.isEmpty()) {
            System.out.println("No tenés metas creadas.");
            return;
        }

        // Mostrar metas disponibles
        System.out.println("Tus metas:");
        for (Meta m : metas) {
            System.out.println(m);
        }

        // ID de la meta
        int idMeta = 0;
        boolean idValido = false;
        while (!idValido) {
            System.out.print("ID de la meta a abonar: ");
            String input = scanner.nextLine().trim();
            try {
                idMeta = Integer.parseInt(input);
                idValido = true;
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un numero valido.");
            }
        }

        // Monto a abonar
        double monto = 0;
        boolean montoValido = false;
        while (!montoValido) {
            System.out.print("Monto a abonar: $");
            String input = scanner.nextLine().trim();
            try {
                monto = Double.parseDouble(input);
                if (monto <= 0) {
                    System.out.println("El monto debe ser mayor a 0.");
                } else {
                    montoValido = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un numero valido.");
            }
        }

        gestor.abonarAMeta(idMeta, monto);
    }

    static void verMetas() {
        System.out.println("\n=== MIS METAS ===");

        List<Meta> metas = gestor.listarMetas();
        if (metas.isEmpty()) {
            System.out.println("No tenés metas creadas.");
            return;
        }

        for (Meta m : metas) {
            System.out.println(m);
        }
    }
}
