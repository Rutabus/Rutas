package com.aqp.rutabus.rutabusaqp.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.aqp.rutabus.rutabusaqp.App;
import com.aqp.rutabus.rutabusaqp.R;
import com.aqp.rutabus.rutabusaqp.data.entity.Empresa;
import com.aqp.rutabus.rutabusaqp.data.entity.GeoPoints;
import com.aqp.rutabus.rutabusaqp.data.entity.Puntos;
import com.aqp.rutabus.rutabusaqp.data.entity.Rutas;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<Puntos> listaPuntos;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                id= null;
            } else {
                id= extras.getString("id");
            }
        } else {
            id= (String) savedInstanceState.getSerializable("id");
        }

        Empresa empresa = App.getDb().empresaDao().getPorAlias(id);
        cargarRuta();

    }

    void cargarRuta(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listaPuntos = new ArrayList<>();
        final Rutas ruta1 = App.getDb().rutasDao().getFirstR(id);

        if (ruta1 != null){
            db.collection("Ruta").document(ruta1.getId()).collection("Puntos")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document:task.getResult()){
                                    GeoPoints geo = document.toObject(GeoPoints.class);
                                    if (geo != null){
                                        Puntos puntos;
                                        if (geo.getPunto() != null){
                                             puntos =  new Puntos(
                                                    document.getId(), document.getId(),ruta1.getId(),id,
                                                    String.valueOf(geo.getPunto().getLatitude()) ,
                                                    String.valueOf(geo.getPunto().getLongitude())
                                            );
                                            listaPuntos.add(puntos);
                                        }

                                        Log.d("Maps", String.valueOf(listaPuntos.size()));
                                    }

                                }
                                agregarMiUbicacion();
                            }
                        }
                    });
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void agregarMiUbicacion() {
        PolylineOptions opciones = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
        Double lat=0.0;
        Double longi=0.0;


        for (int z = 0; z < listaPuntos.size(); z++){
            lat = Double.parseDouble(listaPuntos.get(z).getLatitud());
            longi = Double.parseDouble(listaPuntos.get(z).getLongitud());
            LatLng point = new LatLng(lat,longi );
            opciones.add(point);
        }
        LatLng coordenadas = new LatLng(lat, longi);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);

        Polyline line = mMap.addPolyline(opciones);
        mMap.animateCamera(miUbicacion);

    }

}
