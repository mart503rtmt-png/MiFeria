package MiFeria.Modelo;

import java.time.LocalDate;

// Transaccion representa cada movimiento de dinero
// tipo puede ser "INGRESO" o "GASTO"

public class Transaccion {

    private int id;
    private double monto;
    private String descripcion;
    private LocalDate fecha;
    private String tipo;         // "INGRESO" o "GASTO"
    private int idCategoria;

    public Transaccion(int id, double monto, String descripcion,
                       LocalDate fecha, String tipo, int idCategoria) {
        this.id = id;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.tipo = tipo;
        this.idCategoria = idCategoria;
    }

    public int getId() { return id; }
    public double getMonto() { return monto; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFecha() { return fecha; }
    public String getTipo() { return tipo; }
    public int getIdCategoria() { return idCategoria; }

    @Override
    public String toString() {
        return "[" + tipo + "] $" + monto + " - " + descripcion + " (" + fecha + ")";
    }
}
