package Modelos;

public class OfertanteModel {

    private int id_ofertante;
    private String nombreOfertante;
    private String primerApellidoOfertante;
    private String segundoApellidoOfertante;
    private String contrasenia;
    private String nombreEmpresa;
    private String email_ofertante;
    private String is_administrador;

    public OfertanteModel(int id_ofertante, String nombreOfertante, String primerApellidoOfertante, String segundoApellidoOfertante, String contrasenia, String nombreEmpresa, String email_ofertante, String is_administrador) {
        this.id_ofertante = id_ofertante;
        this.nombreOfertante = nombreOfertante;
        this.primerApellidoOfertante = primerApellidoOfertante;
        this.segundoApellidoOfertante = segundoApellidoOfertante;
        this.contrasenia = contrasenia;
        this.nombreEmpresa = nombreEmpresa;
        this.email_ofertante = email_ofertante;
        this.is_administrador = is_administrador;
    }

    public int getId_ofertante() {
        return id_ofertante;
    }

    public void setId_ofertante(int id_ofertante) {
        this.id_ofertante = id_ofertante;
    }

    public String getNombreOfertante() {
        return nombreOfertante;
    }

    public void setNombreOfertante(String nombreOfertante) {
        this.nombreOfertante = nombreOfertante;
    }

    public String getPrimerApellidoOfertante() {
        return primerApellidoOfertante;
    }

    public void setPrimerApellidoOfertante(String primerApellidoOfertante) {
        this.primerApellidoOfertante = primerApellidoOfertante;
    }

    public String getSegundoApellidoOfertante() {
        return segundoApellidoOfertante;
    }

    public void setSegundoApellidoOfertante(String segundoApellidoOfertante) {
        this.segundoApellidoOfertante = segundoApellidoOfertante;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getEmail_ofertante() {
        return email_ofertante;
    }

    public void setEmail_ofertante(String email_ofertante) {
        this.email_ofertante = email_ofertante;
    }

    public String getIs_administrador() {
        return is_administrador;
    }

    public void setIs_administrador(String is_administrador) {
        this.is_administrador = is_administrador;
    }
}
