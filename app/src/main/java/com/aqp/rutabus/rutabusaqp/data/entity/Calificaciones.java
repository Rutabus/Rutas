package com.aqp.rutabus.rutabusaqp.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

@Entity (tableName = "calificaciones", primaryKeys = {"Calificacion", "UIDUsuario"})
public class Calificaciones {

    @NonNull
    private String Calificacion;
    @NonNull
    private String UIDUsuario;

    public Calificaciones(){}

    @NonNull
    public String getCalificacion() {
        return Calificacion;
    }

    public void setCalificacion(@NonNull String calificacion) {
        Calificacion = calificacion;
    }

    @NonNull
    public String getUIDUsuario() {
        return UIDUsuario;
    }

    public void setUIDUsuario(@NonNull String UIDUsuario) {
        this.UIDUsuario = UIDUsuario;
    }
}
