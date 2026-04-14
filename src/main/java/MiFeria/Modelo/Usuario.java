package MiFeria.Modelo;

// Usuario representa a la persona que usa la app

public class Usuario {

    private int id;
    private String nombre;
    private String correo;
    private String contrasena;

    public Usuario(int id, String nombre, String correo, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }

    // Compara la contrasena ingresada con la guardada
    public boolean validarContrasena(String contrasenaIngresada) {
        return this.contrasena.equals(contrasenaIngresada);
    }

    // Convierte el usuario a texto para guardarlo en archivo
    // Formato: id,nombre,correo,contrasena
    public String aTexto() {
        return id + "," + nombre + "," + correo + "," + contrasena;
    }

    // Reconstruye un Usuario desde una linea del archivo
    public static Usuario desdeTexto(String linea) {
        String[] partes = linea.split(",");
        return new Usuario(
            Integer.parseInt(partes[0]),
            partes[1],
            partes[2],
            partes[3]
        );
    }

    @Override
    public String toString() { return nombre + " (" + correo + ")"; }
}
