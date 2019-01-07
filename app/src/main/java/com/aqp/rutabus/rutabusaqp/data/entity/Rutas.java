package com.aqp.rutabus.rutabusaqp.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;
@Entity (tableName = "rutas")
public class Rutas {
    @NonNull
    @PrimaryKey
    private String id;
    private String ETAlias;
    private String ETRazonSocial;
    private String ETID;
    private String Imagen;
    private String Ruta;
    private String VigenciaInicio;
    private String VigenciaFin;
//    private List<GeoPoint>Punto;

    public Rutas() {
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getETAlias() {
        return ETAlias;
    }

    public void setETAlias(String ETAlias) {
        this.ETAlias = ETAlias;
    }

    public String getETRazonSocial() {
        return ETRazonSocial;
    }

    public void setETRazonSocial(String ETRazonSocial) {
        this.ETRazonSocial = ETRazonSocial;
    }

    public String getETID() {
        return ETID;
    }

    public void setETID(String ETID) {
        this.ETID = ETID;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getRuta() {
        return Ruta;
    }

    public void setRuta(String ruta) {
        Ruta = ruta;
    }

    public String getVigenciaInicio() {
        return VigenciaInicio;
    }

    public void setVigenciaInicio(String vigenciaInicio) {
        VigenciaInicio = vigenciaInicio;
    }

    public String getVigenciaFin() {
        return VigenciaFin;
    }

    public void setVigenciaFin(String vigenciaFin) {
        VigenciaFin = vigenciaFin;
    }
}
