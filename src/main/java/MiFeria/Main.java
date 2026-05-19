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
            System.out.println("1. Transacciones");
            System.out.println("2. Reportes");
            System.out.println("3. Ver resumen");
            System.out.println("4. Cerrar sesion");
            System.out.print("Elegir opcion: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1": menuTransacciones(); break;
                case "2": menuReportes();      break;
                case "3": verResumen();        break;
                case "4":
                    gestor.cerrarSesion();
                    System.out.println("Sesion cerrada.");
                    corriendo = false;
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }

    // ==================== MENU TRANSACCIONES ====================

    static void menuTransacciones() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n=== TRANSACCIONES ===");
            System.out.println("1. Gastos");
            System.out.println("2. Ingresos");
            System.out.println("3. Metas");
            System.out.println("4. Volver");
            System.out.print("Elegir opcion: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1": menuGastos();   break;
                case "2": menuIngresos(); break;
                case "3": menuMetasTransacciones(); break;
                case "4": corriendo = false; break;
                default: System.out.println("Opcion no valida."); break;
            }
        }
    }

    static void menuGastos() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n=== TRANSACCIONES > GASTOS ===");
            System.out.println("1. Agregar gasto");
            System.out.println("2. Eliminar gasto");
            System.out.println("3. Volver");
            System.out.print("Elegir opcion: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1": agregarGasto();        break;
                case "2": eliminarTransaccion(); break;
                case "3": corriendo = false;     break;
                default: System.out.println("Opcion no valida."); break;
            }
        }
    }

    static void menuIngresos() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n=== TRANSACCIONES > INGRESOS ===");
            System.out.println("1. Agregar ingreso");
            System.out.println("2. Volver");
            System.out.print("Elegir opcion: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1": menuTipoIngreso(); break;
                case "2": corriendo = false; break;
                default: System.out.println("Opcion no valida."); break;
            }
        }
    }

    static void menuTipoIngreso() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n=== INGRESOS > TIPO ===");
            System.out.println("1. Constante  (actualiza el ingreso mensual fijo)");
            System.out.println("2. Parciales  (agrega un ingreso extra, puedes ver su origen)");
            System.out.println("3. Volver");
            System.out.print("Elegir opcion: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1": agregarIngresoConstante(); corriendo = false; break;
                case "2": agregarIngresoParcial();   corriendo = false; break;
                case "3": corriendo = false; break;
                default: System.out.println("Opcion no valida."); break;
            }
        }
    }

    static void agregarIngresoConstante() {
        System.out.println("\n--- INGRESO CONSTANTE ---");
        System.out.println("Esto reemplazara tu ingreso mensual fijo actual.");

        double monto;
        while (true) {
            System.out.print("Nuevo monto mensual ($): ");
            try {
                monto = Double.parseDouble(scanner.nextLine().trim());
                if (monto > 0) break;
                System.out.println("Error: el monto debe ser mayor a 0.");
            } catch (NumberFormatException e) {
                System.out.println("Error: ingresa un numero valido. Ej: 3500.00");
            }
        }

        gestor.actualizarIngresoConstante(monto);
        System.out.println("Ingreso constante actualizado correctamente.");
    }

    static void agregarIngresoParcial() {
        System.out.println("\n--- INGRESO PARCIAL ---");

        String origen;
        while (true) {
            System.out.print("Origen del ingreso (ej: freelance, venta, bono): ");
            origen = scanner.nextLine().trim();
            if (!origen.isEmpty()) break;
            System.out.println("Error: el origen no puede estar vacio.");
        }

        double monto;
        while (true) {
            System.out.print("Monto ($): ");
            try {
                monto = Double.parseDouble(scanner.nextLine().trim());
                if (monto > 0) break;
                System.out.println("Error: el monto debe ser mayor a 0.");
            } catch (NumberFormatException e) {
                System.out.println("Error: ingresa un numero valido. Ej: 200.00");
            }
        }

        gestor.agregarTransaccion(monto, origen, "INGRESO", 9);
        System.out.println("Ingreso parcial registrado correctamente.");
    }

    static void menuMetasTransacciones() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n=== TRANSACCIONES > METAS ===");
            System.out.println("1. Crear meta");
            System.out.println("2. Abonar a la meta");
            System.out.println("3. Retirar de la meta");
            System.out.println("4. Volver");
            System.out.print("Elegir opcion: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1": crearMeta();     break;
                case "2": abonarMeta();    break;
                case "3": retirarDeMeta(); break;
                case "4": corriendo = false; break;
                default: System.out.println("Opcion no valida."); break;
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
            System.out.println("3. Volver al menu principal");
            System.out.print("Elegir opcion: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1": crearMeta(); break;
                case "2": abonarMeta(); break;
                case "3": corriendo = false; break;
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

    static void retirarDeMeta() {
        System.out.println("\n=== RETIRAR DE META ===");

        List<Meta> metas = gestor.listarMetas();
        if (metas.isEmpty()) {
            System.out.println("No tenés metas creadas.");
            return;
        }

        System.out.println("Tus metas:");
        for (Meta m : metas) {
            System.out.println(m);
        }

        int idMeta = 0;
        boolean idValido = false;
        while (!idValido) {
            System.out.print("ID de la meta a retirar: ");
            try {
                idMeta = Integer.parseInt(scanner.nextLine().trim());
                idValido = true;
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un numero valido.");
            }
        }

        double monto = 0;
        boolean montoValido = false;
        while (!montoValido) {
            System.out.print("Monto a retirar: $");
            try {
                monto = Double.parseDouble(scanner.nextLine().trim());
                if (monto <= 0) {
                    System.out.println("El monto debe ser mayor a 0.");
                } else {
                    montoValido = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un numero valido.");
            }
        }

        gestor.retirarDeMeta(idMeta, monto);
    }

    // ====================== MENU REPORTES ======================

    static void menuReportes() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n=== REPORTES ===");
            System.out.println("1. Ingresos");
            System.out.println("2. Gastos");
            System.out.println("3. Metas");
            System.out.println("4. Volver");
            System.out.print("Elegir opcion: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1": menuReporteIngresos(); break;
                case "2": menuReporteGastos();   break;
                case "3": reporteMetas();         break;
                case "4": corriendo = false;      break;
                default: System.out.println("Opcion no valida."); break;
            }
        }
    }

    // ---- Submenu reportes de ingresos ----

    static void menuReporteIngresos() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n=== REPORTES > INGRESOS ===");
            System.out.println("1. Parciales");
            System.out.println("2. Constantes");
            System.out.println("3. Volver");
            System.out.print("Elegir opcion: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1": reporteIngresosPorCategoria(9, "Ingresos Parciales");   break;
                case "2": reporteIngresosPorCategoria(8, "Ingresos Constantes"); break;
                case "3": corriendo = false; break;
                default: System.out.println("Opcion no valida."); break;
            }
        }
    }

    static void reporteIngresosPorCategoria(int idCategoria, String titulo) {
        System.out.println("\n--- " + titulo.toUpperCase() + " ---");
        List<Transaccion> lista = gestor.filtrarPorCategoria(idCategoria);
        if (lista.isEmpty()) {
            System.out.println("No hay registros.");
            return;
        }
        double total = 0;
        for (Transaccion t : lista) {
            System.out.printf("  ID:%-3d | $%8.2f | %-25s | %s%n",
                    t.getId(), t.getMonto(), t.getDescripcion(), t.getFecha());
            total += t.getMonto();
        }
        System.out.println("───────────────────────────────────");
        System.out.printf("  TOTAL: $%.2f%n", total);
    }

    // ---- Submenu reportes de gastos ----

    static void menuReporteGastos() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n=== REPORTES > GASTOS ===");
            System.out.println("1. Todas las categorias");
            List<Categoria> cats = gestor.getCategoriasGasto();
            for (int i = 0; i < cats.size(); i++) {
                System.out.println((i + 2) + ". " + cats.get(i).getNombre());
            }
            System.out.println((cats.size() + 2) + ". Volver");
            System.out.print("Elegir opcion: ");
            String opcion = scanner.nextLine().trim();

            int salida = cats.size() + 2;
            if (opcion.equals(String.valueOf(salida))) {
                corriendo = false;
            } else if (opcion.equals("1")) {
                reporteGastosTodas();
            } else {
                try {
                    int idx = Integer.parseInt(opcion) - 2;
                    if (idx >= 0 && idx < cats.size()) {
                        Categoria cat = cats.get(idx);
                        reporteGastosPorCategoria(cat.getId(), cat.getNombre());
                    } else {
                        System.out.println("Opcion no valida.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Opcion no valida.");
                }
            }
        }
    }

    static void reporteGastosTodas() {
        System.out.println("\n--- GASTOS: TODAS LAS CATEGORIAS ---");
        List<Transaccion> gastos = gestor.listarGastos();
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos registrados.");
            return;
        }

        java.util.Map<Integer, List<Transaccion>> porCategoria = new java.util.LinkedHashMap<>();
        for (Transaccion t : gastos) {
            porCategoria.computeIfAbsent(t.getIdCategoria(), k -> new java.util.ArrayList<>()).add(t);
        }

        double totalGeneral = 0;
        for (java.util.Map.Entry<Integer, List<Transaccion>> entry : porCategoria.entrySet()) {
            Categoria cat = gestor.buscarCategoriaPorId(entry.getKey());
            String nombreCat = (cat != null) ? cat.getNombre() : "Sin categoria";
            System.out.println("\n  [ " + nombreCat + " ]");
            double subtotal = 0;
            for (Transaccion t : entry.getValue()) {
                System.out.printf("    ID:%-3d | $%8.2f | %-25s | %s%n",
                        t.getId(), t.getMonto(), t.getDescripcion(), t.getFecha());
                subtotal += t.getMonto();
            }
            System.out.printf("    Subtotal: $%.2f%n", subtotal);
            totalGeneral += subtotal;
        }
        System.out.println("───────────────────────────────────");
        System.out.printf("  TOTAL GASTOS: $%.2f%n", totalGeneral);
    }

    static void reporteGastosPorCategoria(int idCategoria, String nombreCat) {
        System.out.println("\n--- GASTOS: " + nombreCat.toUpperCase() + " ---");
        List<Transaccion> lista = gestor.filtrarPorCategoria(idCategoria);
        if (lista.isEmpty()) {
            System.out.println("No hay gastos en esta categoria.");
            return;
        }
        double total = 0;
        for (Transaccion t : lista) {
            System.out.printf("  ID:%-3d | $%8.2f | %-25s | %s%n",
                    t.getId(), t.getMonto(), t.getDescripcion(), t.getFecha());
            total += t.getMonto();
        }
        System.out.println("───────────────────────────────────");
        System.out.printf("  TOTAL %s: $%.2f%n", nombreCat.toUpperCase(), total);
    }

    static void reporteMetas() {
        System.out.println("\n--- REPORTE DE METAS ---");
        List<Meta> metas = gestor.listarMetas();
        if (metas.isEmpty()) {
            System.out.println("No hay metas creadas.");
            return;
        }

        for (Meta m : metas) {
            double porcentaje = (m.getMontoObjetivo() > 0)
                    ? (m.getMontoActual() / m.getMontoObjetivo()) * 100
                    : 0;
            System.out.println("\n  Meta: " + m.getNombre());
            System.out.printf("  Objetivo : $%.2f%n", m.getMontoObjetivo());
            System.out.printf("  Ahorrado : $%.2f%n", m.getMontoActual());
            System.out.printf("  Avance   : %.1f%%%n", porcentaje);
            if (porcentaje >= 100) {
                System.out.println("  Estado   : Completada - Felicidades!");
            } else {
                System.out.println("  Estado   : Pendiente");
            }
        }
    }

    private static String generarBarra(double porcentaje) {
        int llenas = (int) (porcentaje / 10);
        StringBuilder barra = new StringBuilder("[");
        for (int i = 0; i < 10; i++) barra.append(i < llenas ? "█" : "░");
        barra.append("]");
        return barra.toString();
    }
}
