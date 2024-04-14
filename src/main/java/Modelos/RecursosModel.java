package Modelos;

public class RecursosModel {

    private int id_recurso;

    private ActividadModel actividad;

    private String nombre_recurso;

    private String descripcion;

    private int cantidad;

    private boolean is_ofertada_por_ofertante;

    public RecursosModel(int id_recurso, ActividadModel actividad, String nombre_recurso, String descripcion, int cantidad, boolean is_ofertada_por_ofertante) {
        this.id_recurso = id_recurso;
        this.actividad = actividad;
        this.nombre_recurso = nombre_recurso;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.is_ofertada_por_ofertante = is_ofertada_por_ofertante;
    }

    public int getId_recurso() {
        return id_recurso;
    }

    public void setId_recurso(int id_recurso) {
        this.id_recurso = id_recurso;
    }

    public ActividadModel getActividad() {
        return actividad;
    }

    public void setActividad(ActividadModel actividad) {
        this.actividad = actividad;
    }

    public String getNombre_recurso() {
        return nombre_recurso;
    }

    public void setNombre_recurso(String nombre_recurso) {
        this.nombre_recurso = nombre_recurso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isIs_ofertada_por_ofertante() {
        return is_ofertada_por_ofertante;
    }

    public void setIs_ofertada_por_ofertante(boolean is_ofertada_por_ofertante) {
        this.is_ofertada_por_ofertante = is_ofertada_por_ofertante;
    }

    @Override
    public String toString() {
        return "RecursosModel{" +
                "id_recurso=" + id_recurso +
                ", actividad=" + actividad +
                ", nombre_recurso='" + nombre_recurso + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", cantidad=" + cantidad +
                ", is_ofertada_por_ofertante=" + is_ofertada_por_ofertante +
                '}';
    }
}
