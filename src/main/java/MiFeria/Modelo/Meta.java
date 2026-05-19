package MiFeria.Modelo;

public class Meta {

    private int id;
    private String nombre;
    private double montoObjetivo;
    private double montoActual;

    // Constructor
    public Meta(int id, String nombre, double montoObjetivo, double montoActual) {
        this.id = id;
        this.nombre = nombre;
        this.montoObjetivo = montoObjetivo;
        this.montoActual = montoActual;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getMontoObjetivo() { return montoObjetivo; }
    public double getMontoActual() { return montoActual; }

    // Setter
    public void setMontoActual(double montoActual) { this.montoActual = montoActual; }

    // toString
    @Override
    public String toString() {
        double restante = montoObjetivo - montoActual;
        return "[Meta #" + id + "] " + nombre +
                " | Objetivo: $" + montoObjetivo +
                " | Ahorrado: $" + montoActual +
                " | Restante: $" + restante;
    }
}