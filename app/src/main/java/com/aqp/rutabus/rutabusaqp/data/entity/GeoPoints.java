package com.aqp.rutabus.rutabusaqp.data.entity;

import com.google.firebase.firestore.GeoPoint;

public class GeoPoints {
    private GeoPoint Punto;

    public GeoPoints() {
    }

    public GeoPoint getPunto() {
        return Punto;
    }

    public void setPunto(GeoPoint punto) {
        Punto = punto;
    }
}
