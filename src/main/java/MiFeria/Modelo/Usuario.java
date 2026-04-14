package MiFeria.Modelo;

// Usuario representa a la persona que usa la app
// Guarda sus datos y permite validar su contrasena para el login

public class Usuario {

    // Atributos privados
    private int id;
    private String nombre;
    private String correo;
    private String contrasena;

    // Constructor
    public Usuario(int id, String nombre, String correo, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    // Validar contrasena: compara lo que escribio el usuario con la contrasena guardada
    // Retorna true si es correcta, false si no
    public boolean validarContrasena(String contrasenaIngresada) {
        return this.contrasena.equals(contrasenaIngresada);
    }

    @Override
    public String toString() {
        return nombre + " (" + correo + ")";
    }
}
