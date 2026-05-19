package MiFeria.Modelo;

// La categoria clasifica cada transaccion
// Ejemplo: "Alimentacion", "Transporte", "Salario"

public class Categoria {

    private int id;
    private String nombre;


    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;

    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }


    // toString sirve para mostrar el nombre en un ComboBox de JavaFX
    @Override
    public String toString() { return nombre; }
}
