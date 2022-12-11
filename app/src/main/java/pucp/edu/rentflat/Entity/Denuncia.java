package pucp.edu.rentflat.Entity;

import android.media.AudioTimestamp;

import com.google.firebase.Timestamp;

public class Denuncia {
    private Usuario cliente;
    private Usuario propietario;
    private Timestamp fecha;
    private String motivo;


    public Denuncia(){

    }

    public Denuncia(Usuario cliente, Usuario propietario, Timestamp fecha,String motivo) {
        this.cliente = cliente;
        this.propietario = propietario;
        this.fecha = fecha;
        this.motivo = motivo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
