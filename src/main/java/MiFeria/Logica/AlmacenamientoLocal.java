package MiFeria.Logica;

import MiFeria.Modelo.Transaccion;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// AlmacenamientoLocal guarda y carga datos en archivos .csv
// Los archivos se crean solos en la carpeta raiz del proyecto

public class AlmacenamientoLocal {

    private static final String ARCHIVO_TRANSACCIONES = "transacciones.csv";
    private static final String ARCHIVO_PERFIL        = "perfil.txt";

    // ==================== TRANSACCIONES ====================

    // Guarda una transaccion nueva al final del archivo
    public static void guardarTransaccion(Transaccion t) {
        try (FileWriter fw = new FileWriter(ARCHIVO_TRANSACCIONES, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            // Formato: id,monto,descripcion,fecha,tipo,idCategoria
            String linea = t.getId() + "," +
                           t.getMonto() + "," +
                           t.getDescripcion() + "," +
                           t.getFecha() + "," +
                           t.getTipo() + "," +
                           t.getIdCategoria();
            bw.write(linea);
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Error al guardar transaccion: " + e.getMessage());
        }
    }

    // Carga todas las transacciones desde el archivo
    public static List<Transaccion> cargarTransacciones() {
        List<Transaccion> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO_TRANSACCIONES);

        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split(",");
                Transaccion t = new Transaccion(
                    Integer.parseInt(partes[0]),
                    Double.parseDouble(partes[1]),
                    partes[2],
                    LocalDate.parse(partes[3]),
                    partes[4],
                    Integer.parseInt(partes[5])
                );
                lista.add(t);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar transacciones: " + e.getMessage());
        }

        return lista;
    }

    // Elimina una transaccion por id reescribiendo el archivo sin ella
    public static void eliminarTransaccion(int idEliminar) {
        List<Transaccion> todas = cargarTransacciones();

        try (FileWriter fw = new FileWriter(ARCHIVO_TRANSACCIONES, false);
             BufferedWriter bw = new BufferedWriter(fw)) {

            for (Transaccion t : todas) {
                if (t.getId() != idEliminar) {
                    String linea = t.getId() + "," +
                                   t.getMonto() + "," +
                                   t.getDescripcion() + "," +
                                   t.getFecha() + "," +
                                   t.getTipo() + "," +
                                   t.getIdCategoria();
                    bw.write(linea);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al eliminar transaccion: " + e.getMessage());
        }
    }

    // ==================== PERFIL ====================

    // Guarda el nombre del usuario en perfil.txt
    public static void guardarNombre(String nombre) {
        try (FileWriter fw = new FileWriter(ARCHIVO_PERFIL, false);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(nombre);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar perfil: " + e.getMessage());
        }
    }

    // Carga el nombre guardado, devuelve null si no existe
    public static String cargarNombre() {
        File archivo = new File(ARCHIVO_PERFIL);
        if (!archivo.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea = br.readLine();
            if (linea != null && !linea.trim().isEmpty()) return linea.trim();
        } catch (IOException e) {
            System.out.println("Error al cargar perfil: " + e.getMessage());
        }
        return null;
    }
}
