package TransformadorJSON;

public class TransformadorOfertante {

    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String contrasenia;
    private String nombreEmpresa;
    private String email_ofertante;
    private boolean is_administrador;

    private String esqueletoOfertante = "{\n" +
            "    \"nombreOfertante\": " + nombre + ",\n" +
            "    \"primerApellidoOfertante\": " + primerApellido + ",\n" +
            "    \"segundoApellidoOfertante\": " + segundoApellido + ",\n" +
            "    \"contrasenia\": " + contrasenia + ",\n" +
            "    \"nombreEmpresa\": " + nombreEmpresa + ",\n" +
            "    \"email_ofertante\": " + email_ofertante + ",\n" +
            "    \"is_administrador\": " + false + "\n" +
            "}";

    public TransformadorOfertante(String nombre, String primerApellido, String segundoApellido, String contrasenia, String nombreEmpresa, String email_ofertante, boolean is_administrador) {
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.contrasenia = contrasenia;
        this.nombreEmpresa = nombreEmpresa;
        this.email_ofertante = email_ofertante;
        this.is_administrador = is_administrador;
    }

    public void enviarInformacion() {
        //TODO
    }


}
