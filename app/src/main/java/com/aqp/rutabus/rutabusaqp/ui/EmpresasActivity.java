package com.aqp.rutabus.rutabusaqp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aqp.rutabus.rutabusaqp.App;
import com.aqp.rutabus.rutabusaqp.R;
import com.aqp.rutabus.rutabusaqp.data.entity.Empresa;
import com.aqp.rutabus.rutabusaqp.data.entity.Puntos;
import com.aqp.rutabus.rutabusaqp.data.entity.Rutas;
import com.aqp.rutabus.rutabusaqp.ui.views.adapters.EmpresasAdapter;
import com.aqp.rutabus.rutabusaqp.ui.views.adapters.RutasAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmpresasActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.tv_det_nombre_bus)
    TextView tvAlias;
    @BindView(R.id.tv_det_ruc)
    TextView tvRuc;
    @BindView(R.id.tv_det_direccion)
    TextView tvDireccion;
    @BindView(R.id.tv_det_razon_social)
    TextView tvRazon_social;
    @BindView(R.id.tv_det_rutas)
    TextView tvRutas;
    @BindView(R.id.im_det_bus)
    ImageView imgBus;
    @BindView(R.id.rv_lista_ruta)
    RecyclerView rvRuta;
    private RecyclerView.LayoutManager layoutManager;
    private Rutas _ruta;

    RutasAdapter rAdapter;

    String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_bus_detalle);
        ButterKnife.bind(this);

        FirebaseStorage storage;
        storage = FirebaseStorage.getInstance();

        String alias = "";

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                alias= null;
            } else {
                alias= extras.getString("alias");
            }
        } else {
            alias= (String) savedInstanceState.getSerializable("alias");
        }

        Empresa empresa = App.getDb().empresaDao().getPorAlias(alias);
        id = empresa.getId();

        tvAlias.setText(alias);
        tvDireccion.setText("Dirección: " + empresa.getDireccion());
        tvRazon_social.setText("Razón Social: " + empresa.getRazonSocial());
        tvRutas.setText(empresa.getRepresentante());
        tvRuc.setText("RUC: " + empresa.getRuc());

        rvRuta.setLayoutManager(new GridLayoutManager(this,2));

        rvRuta.setLayoutManager(layoutManager);
        rvRuta.setHasFixedSize(true);
        rvRuta.setAdapter(null);


        FirebaseFirestore db;
        Task<QuerySnapshot> mQuery;

        db = FirebaseFirestore.getInstance();

        db.collection("Ruta")
                .whereEqualTo("ETID", id)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                         if (task.isSuccessful()){
                             for (QueryDocumentSnapshot document:task.getResult()){
                                 Rutas rutas = document.toObject(Rutas.class);
                                 rutas.setId(document.getId());
                                 App.getDb().rutasDao().insert(rutas);
                             }

                             List<Rutas> rutasAll = App.getDb().rutasDao().getByEmpresa(id);
                             if (rutasAll.size()>0){
                                 rAdapter = new RutasAdapter(rutasAll, EmpresasActivity.this);
                                 rvRuta.setAdapter(rAdapter);
                                 rAdapter.notifyDataSetChanged();
                             }
                         }
                }
            });

        String fotoEmpresa = String.format("rutas/%s", empresa.getImagen());

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference with an initial file path and name
        StorageReference pathReference = storageRef.child(fotoEmpresa);

        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Load Image
                Glide.with(imgBus.getContext())
                        .load(uri)
                        .into(imgBus);
            }
        });


        //tvAlias.setText(alias);
    }

    @OnClick(R.id.im_det_bus)
    void mostratrRuta(){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

}
