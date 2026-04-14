package MiFeria;

import MiFeria.Logica.GestorFinanciero;
import MiFeria.Modelo.Transaccion;

import java.time.LocalDate;

// Esta clase sirve para probar que todo funciona correctamente
// Simula lo que haria la app sin necesidad de la interfaz grafica

public class Main {

    public static void main(String[] args) {

        // 1. Crear el gestor y cargar categorias por defecto
        GestorFinanciero gestor = new GestorFinanciero();
        gestor.cargarCategoriasPorDefecto();

        // 2. Simular un login
        boolean loginExitoso = gestor.iniciarSesion("jose@mail.com", "1234");
        if (loginExitoso) {
            System.out.println("Login exitoso. Bienvenido, " +
                gestor.getUsuarioActivo().getNombre());
        } else {
            System.out.println("Correo o contrasena incorrectos.");
            return; // detener si el login falla
        }

        // 3. Agregar algunos ingresos de prueba
        // new Transaccion(id, monto, descripcion, fecha, tipo, idCategoria)
        gestor.agregarTransaccion(new Transaccion(
            1, 500.00, "Salario quincenal",
            LocalDate.now(), "INGRESO", 5
        ));
        gestor.agregarTransaccion(new Transaccion(
            2, 50.00, "Trabajo freelance",
            LocalDate.now(), "INGRESO", 5
        ));

        // 4. Agregar algunos gastos de prueba
        gestor.agregarTransaccion(new Transaccion(
            3, 80.00, "Compra del super",
            LocalDate.now(), "GASTO", 1
        ));
        gestor.agregarTransaccion(new Transaccion(
            4, 30.00, "Pasajes de la semana",
            LocalDate.now(), "GASTO", 2
        ));
        gestor.agregarTransaccion(new Transaccion(
            5, 15.00, "Cine con amigos",
            LocalDate.now(), "GASTO", 4
        ));

        // 5. Mostrar resumen en consola
        System.out.println("\n===== RESUMEN FINANCIERO =====");
        System.out.println("Total ingresos : $" + gestor.calcularTotalIngresos());
        System.out.println("Total gastos   : $" + gestor.calcularTotalGastos());
        System.out.println("Balance actual : $" + gestor.calcularBalance());

        // 6. Mostrar todas las transacciones
        System.out.println("\n===== TODAS LAS TRANSACCIONES =====");
        for (Transaccion t : gestor.listarTransacciones()) {
            System.out.println(t);
        }

        // 7. Mostrar solo gastos de alimentacion (categoria id=1)
        System.out.println("\n===== GASTOS DE ALIMENTACION =====");
        for (Transaccion t : gestor.filtrarPorCategoria(1)) {
            System.out.println(t);
        }
    }
}
