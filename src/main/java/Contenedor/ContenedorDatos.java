package Contenedor;

import Modelos.ConsumidorModel;
import Modelos.OfertanteModel;

/**
 * Clase que contiene los datos que se almacenaran en memoria durante la vida de la aplicacion
 */
public class ContenedorDatos {

    Object usuario;

    public ContenedorDatos() {
    }

    public Object getUsuario() {
        return usuario;
    }

    public void setUsuario(Object usuario) {
        if (usuario instanceof OfertanteModel) {
            this.usuario = usuario;
        } else if (usuario instanceof ConsumidorModel) {
            this.usuario = usuario;
        }
    }

    @Override
    public String toString() {
        return "ContenedorDatos{" +
                "usuario=" + usuario +
                '}';
    }
}
