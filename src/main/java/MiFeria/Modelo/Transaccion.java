package MiFeria.Modelo;

import java.time.LocalDate;

// Transaccion representa cada movimiento de dinero
// Puede ser un INGRESO (dinero que entra) o un GASTO (dinero que sale)

public class Transaccion {

    // Atributos privados
    private int id;
    private double monto;       // cuanto dinero fue
    private String descripcion; // una nota, ej: "compra en el super"
    private LocalDate fecha;    // que dia fue
    private String tipo;        // "INGRESO" o "GASTO"
    private int idCategoria;    // a que categoria pertenece

    // Constructor
    public Transaccion(int id, double monto, String descripcion,
                       LocalDate fecha, String tipo, int idCategoria) {
        this.id = id;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.tipo = tipo;           // debe ser "INGRESO" o "GASTO"
        this.idCategoria = idCategoria;
    }

    // Getters
    public int getId() {
        return id;
    }

    public double getMonto() {
        return monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    // toString: util para mostrar la transaccion en un ListView de JavaFX
    @Override
    public String toString() {
        return "[" + tipo + "] $" + monto + " - " + descripcion + " (" + fecha + ")";
    }
}
