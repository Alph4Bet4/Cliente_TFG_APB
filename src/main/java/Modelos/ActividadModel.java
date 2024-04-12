package Modelos;


import java.sql.Time;
import java.sql.Date;


public class ActividadModel {

    private int id_actividad;
    private String tipoActividad;
    private String descripcionActividad;
    private String direccion;
    private Date fecha;
    private Time hora_inicio;
    private Time hora_fin;
    private int cantidad_max_personas;
    private int cantidad_actual_personas;
    private OfertanteModel Creador_ofertante;

    public ActividadModel(int id_actividad, String tipoActividad, String descripcionActividad, String direccion, Date fecha, Time hora_inicio, Time hora_fin, int cantidad_max_personas, int cantidad_actual_personas, OfertanteModel creador_ofertante) {
        this.id_actividad = id_actividad;
        this.tipoActividad = tipoActividad;
        this.descripcionActividad = descripcionActividad;
        this.direccion = direccion;
        this.fecha = fecha;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.cantidad_max_personas = cantidad_max_personas;
        this.cantidad_actual_personas = cantidad_actual_personas;
        Creador_ofertante = creador_ofertante;
    }

    public int getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(String tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public String getDescripcionActividad() {
        return descripcionActividad;
    }

    public void setDescripcionActividad(String descripcionActividad) {
        this.descripcionActividad = descripcionActividad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(Time hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public Time getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(Time hora_fin) {
        this.hora_fin = hora_fin;
    }

    public int getCantidad_max_personas() {
        return cantidad_max_personas;
    }

    public void setCantidad_max_personas(int cantidad_max_personas) {
        this.cantidad_max_personas = cantidad_max_personas;
    }

    public int getCantidad_actual_personas() {
        return cantidad_actual_personas;
    }

    public void setCantidad_actual_personas(int cantidad_actual_personas) {
        this.cantidad_actual_personas = cantidad_actual_personas;
    }

    public OfertanteModel getCreador_ofertante() {
        return Creador_ofertante;
    }

    public void setCreador_ofertante(OfertanteModel creador_ofertante) {
        Creador_ofertante = creador_ofertante;
    }

    @Override
    public String toString() {
        return "ActividadModel{" +
                "id_actividad=" + id_actividad +
                ", tipoActividad='" + tipoActividad + '\'' +
                ", descripcionActividad='" + descripcionActividad + '\'' +
                ", direccion='" + direccion + '\'' +
                ", fecha=" + fecha +
                ", hora_inicio=" + hora_inicio +
                ", hora_fin=" + hora_fin +
                ", cantidad_max_personas=" + cantidad_max_personas +
                ", cantidad_actual_personas=" + cantidad_actual_personas +
                ", Creador_ofertante=" + Creador_ofertante +
                '}';
    }
}
