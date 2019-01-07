package com.aqp.rutabus.rutabusaqp.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.firebase.firestore.GeoPoint;

@Entity (tableName = "puntos")
public class Puntos {
    @NonNull
    @PrimaryKey
    private String id;
    private String orden;
    private String idRuta;
    private String idEmpresa;
    private String Latitud;
    private String Longitud;

    public Puntos() {
    }

    @Ignore
    public Puntos(@NonNull String id, String orden, String idRuta, String idEmpresa, String latitud, String longitud) {
        this.id = id;
        this.orden = orden;
        this.idRuta = idRuta;
        this.idEmpresa = idEmpresa;
        Latitud = latitud;
        Longitud = longitud;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
}
