package pucp.edu.rentflat.Entity;

import java.util.ArrayList;
import java.util.List;

public class Inmueble {

    private String tipo;
    private String distrito;
    private String ubicacion;
    private String amoblado;
    private String precio;
    private String detalles;
    private String uid;
    private String estado;
    private List<String> urlInmueble;

    public Inmueble(){

    }

    public Inmueble(String tipo, String distrito, String ubicacion, String amoblado, String precio, String detalles, List<String> urlInmueble) {
        this.tipo = tipo;
        this.distrito = distrito;
        this.ubicacion = ubicacion;
        this.amoblado = amoblado;
        this.precio = precio;
        this.detalles = detalles;
        this.urlInmueble = urlInmueble;
    }

    public List<String> getUrlInmueble() {
        return urlInmueble;
    }

    public void setUrlInmueble(List<String> urlInmueble) {
        this.urlInmueble = urlInmueble;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getAmoblado() {
        return amoblado;
    }

    public void setAmoblado(String amoblado) {
        this.amoblado = amoblado;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
}
