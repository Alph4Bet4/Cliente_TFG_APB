package Modelos;

public class SugerenciaActividadModel {

    private int id_sugerencia;

    private ConsumidorModel consumidor;

    private ActividadModel actividad;

    public SugerenciaActividadModel(int id_sugerencia, ConsumidorModel consumidor, ActividadModel actividad) {
        this.id_sugerencia = id_sugerencia;
        this.consumidor = consumidor;
        this.actividad = actividad;
    }

    public int getId_sugerencia() {
        return id_sugerencia;
    }

    public void setId_sugerencia(int id_sugerencia) {
        this.id_sugerencia = id_sugerencia;
    }

    public ConsumidorModel getConsumidor() {
        return consumidor;
    }

    public void setConsumidor(ConsumidorModel consumidor) {
        this.consumidor = consumidor;
    }

    public ActividadModel getActividad() {
        return actividad;
    }

    public void setActividad(ActividadModel actividad) {
        this.actividad = actividad;
    }

    @Override
    public String toString() {
        return "SugerenciaActividadModel{" +
                "id_sugerencia=" + id_sugerencia +
                ", consumidor=" + consumidor +
                ", actividad=" + actividad +
                '}';
    }
}
