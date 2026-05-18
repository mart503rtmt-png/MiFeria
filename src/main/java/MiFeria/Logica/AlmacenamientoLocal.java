package MiFeria.Logica;

import MiFeria.Modelo.Transaccion;
import MiFeria.Modelo.Usuario;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// AlmacenamientoLocal guarda y carga datos en archivos .csv
// Los archivos se crean solos en la carpeta raiz del proyecto

public class AlmacenamientoLocal {

    private static final String ARCHIVO_TRANSACCIONES = "transacciones.csv";
    private static final String ARCHIVO_USUARIOS      = "usuarios.csv";

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

        // Si el archivo no existe aun, devuelve lista vacia
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

    // ==================== USUARIOS ====================

    // Guarda un usuario nuevo en el archivo
    public static void guardarUsuario(Usuario u) {
        try (FileWriter fw = new FileWriter(ARCHIVO_USUARIOS, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(u.aTexto());
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Error al guardar usuario: " + e.getMessage());
        }
    }

    // Busca un usuario por correo, devuelve null si no existe
    public static Usuario buscarUsuarioPorCorreo(String correo) {
        File archivo = new File(ARCHIVO_USUARIOS);
        if (!archivo.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                Usuario u = Usuario.desdeTexto(linea);
                if (u.getCorreo().equals(correo)) return u;
            }
        } catch (IOException e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }

        return null;
    }

    // Verifica si un correo ya esta registrado
    public static boolean correoExiste(String correo) {
        return buscarUsuarioPorCorreo(correo) != null;
    }

    // Sobreescribe el archivo de usuarios con un unico perfil
    public static void guardarUsuarioUnico(Usuario u) {
        try (FileWriter fw = new FileWriter(ARCHIVO_USUARIOS, false);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(u.aTexto());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error al guardar perfil: " + e.getMessage());
        }
    }

    // Carga el primer usuario guardado (app local de un solo perfil)
    public static Usuario cargarPrimerUsuario() {
        File archivo = new File(ARCHIVO_USUARIOS);
        if (!archivo.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea = br.readLine();
            if (linea != null && !linea.trim().isEmpty()) {
                return Usuario.desdeTexto(linea.trim());
            }
        } catch (IOException e) {
            System.out.println("Error al cargar usuario: " + e.getMessage());
        }
        return null;
    }
}
