package Modelos;

public class ConsumidorModel {

    private int id_consumidor;

    private String nombreConsumidor;

    private String primerApellidoConsumidor;

    private String segundoApellidoConsumidor;

    private String contrasenia;

    private String email_consumidor;

    public ConsumidorModel(int id_consumidor, String nombreConsumidor, String primerApellidoConsumidor, String segundoApellidoConsumidor, String contrasenia, String email_consumidor) {
        this.id_consumidor = id_consumidor;
        this.nombreConsumidor = nombreConsumidor;
        this.primerApellidoConsumidor = primerApellidoConsumidor;
        this.segundoApellidoConsumidor = segundoApellidoConsumidor;
        this.contrasenia = contrasenia;
        this.email_consumidor = email_consumidor;
    }

    public int getId_consumidor() {
        return id_consumidor;
    }

    public void setId_consumidor(int id_consumidor) {
        this.id_consumidor = id_consumidor;
    }

    public String getNombreConsumidor() {
        return nombreConsumidor;
    }

    public void setNombreConsumidor(String nombreConsumidor) {
        this.nombreConsumidor = nombreConsumidor;
    }

    public String getPrimerApellidoConsumidor() {
        return primerApellidoConsumidor;
    }

    public void setPrimerApellidoConsumidor(String primerApellidoConsumidor) {
        this.primerApellidoConsumidor = primerApellidoConsumidor;
    }

    public String getSegundoApellidoConsumidor() {
        return segundoApellidoConsumidor;
    }

    public void setSegundoApellidoConsumidor(String segundoApellidoConsumidor) {
        this.segundoApellidoConsumidor = segundoApellidoConsumidor;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getEmail_consumidor() {
        return email_consumidor;
    }

    public void setEmail_consumidor(String email_consumidor) {
        this.email_consumidor = email_consumidor;
    }

    @Override
    public String toString() {
        return "ConsumidorModel{" +
                "id_consumidor=" + id_consumidor +
                ", nombreConsumidor='" + nombreConsumidor + '\'' +
                ", primerApellidoConsumidor='" + primerApellidoConsumidor + '\'' +
                ", segundoApellidoConsumidor='" + segundoApellidoConsumidor + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", email_consumidor='" + email_consumidor + '\'' +
                '}';
    }
}
