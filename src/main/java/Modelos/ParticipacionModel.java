package Modelos;

public class ParticipacionModel {

    private int id_participacion_actividades;

    private ActividadModel actividad;

    private ConsumidorModel consumidor;

    public ParticipacionModel(int id_participacion_actividades, ActividadModel actividad, ConsumidorModel consumidor) {
        this.id_participacion_actividades = id_participacion_actividades;
        this.actividad = actividad;
        this.consumidor = consumidor;
    }

    public int getId_participacion_actividades() {
        return id_participacion_actividades;
    }

    public void setId_participacion_actividades(int id_participacion_actividades) {
        this.id_participacion_actividades = id_participacion_actividades;
    }

    public ActividadModel getActividad() {
        return actividad;
    }

    public void setActividad(ActividadModel actividad) {
        this.actividad = actividad;
    }

    public ConsumidorModel getConsumidor() {
        return consumidor;
    }

    public void setConsumidor(ConsumidorModel consumidor) {
        this.consumidor = consumidor;
    }

    @Override
    public String toString() {
        return "ParticipacionModel{" +
                "id_participacion_actividades=" + id_participacion_actividades +
                ", actividad=" + actividad +
                ", consumidor=" + consumidor +
                '}';
    }
}
