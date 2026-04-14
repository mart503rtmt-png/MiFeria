package MiFeria.Modelo;

// La categoria clasifica cada transaccion
// Ejemplo: "Alimentacion", "Transporte", "Salario"

public class Categoria {

    private int id;
    private String nombre;
    private String colorHex;

    public Categoria(int id, String nombre, String colorHex) {
        this.id = id;
        this.nombre = nombre;
        this.colorHex = colorHex;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getColorHex() { return colorHex; }

    // toString sirve para mostrar el nombre en un ComboBox de JavaFX
    @Override
    public String toString() { return nombre; }
}
