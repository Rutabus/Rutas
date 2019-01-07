package com.aqp.rutabus.rutabusaqp.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;



@Entity (tableName = "empresa")
public class Empresa {
    @NonNull
    @PrimaryKey
    private String id;
    private String Ruc;
    private String RazonSocial;
    private String Alias;
    private String Direccion;
    private String Representante;
    private String Imagen;
    private String PrecioEntero;
    private String PrecioEscolar;
    private String PrecioMedio;

    public Empresa() {
    }

    public String getRuc() {
        return Ruc;
    }

    public void setRuc(String ruc) {
        this.Ruc = ruc;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.RazonSocial = razonSocial;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        this.Direccion = direccion;
    }

    public String getRepresentante() {
        return Representante;
    }

    public void setRepresentante(String representante) {
        this.Representante = representante;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        this.Imagen = imagen;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias( String alias) {
        Alias = alias;
    }

    public String getPrecioEntero() {
        return PrecioEntero;
    }

    public void setPrecioEntero(String precioEntero) {
        PrecioEntero = precioEntero;
    }

    public String getPrecioEscolar() {
        return PrecioEscolar;
    }

    public void setPrecioEscolar(String precioEscolar) {
        PrecioEscolar = precioEscolar;
    }

    public String getPrecioMedio() {
        return PrecioMedio;
    }

    public void setPrecioMedio(String precioMedio) {
        PrecioMedio = precioMedio;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}
