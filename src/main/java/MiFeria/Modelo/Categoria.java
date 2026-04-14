package MiFeria.Modelo;

// La categoria sirve para clasificar cada transaccion
// Ejemplo: "Alimentacion", "Transporte", "Salario"

public class Categoria {

    // Atributos privados (nadie los puede cambiar directamente desde afuera)
    private int id;
    private String nombre;
    private String colorHex; // color para mostrar en la interfaz, ej: "#FF5733"

    // Constructor: se llama cuando creamos una nueva Categoria
    public Categoria(int id, String nombre, String colorHex) {
        this.id = id;
        this.nombre = nombre;
        this.colorHex = colorHex;
    }

    // Metodos publicos para obtener los valores (getters)
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getColorHex() {
        return colorHex;
    }

    // toString: sirve para mostrar el nombre cuando se usa en un ComboBox de JavaFX
    @Override
    public String toString() {
        return nombre;
    }
}
