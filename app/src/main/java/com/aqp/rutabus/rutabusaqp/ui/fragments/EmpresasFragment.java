package com.aqp.rutabus.rutabusaqp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aqp.rutabus.rutabusaqp.App;
import com.aqp.rutabus.rutabusaqp.R;
import com.aqp.rutabus.rutabusaqp.data.entity.Empresa;
import com.aqp.rutabus.rutabusaqp.ui.EmpresasActivity;
import com.aqp.rutabus.rutabusaqp.ui.MainActivity;
import com.aqp.rutabus.rutabusaqp.ui.views.adapters.EmpresasAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

public class EmpresasFragment extends Fragment {
    public static final String TAG = "EmpresasFragment";

    //@BindView(R.id.rv_lista_buses)
    RecyclerView rv_buses;
    RecyclerView.LayoutManager layoutManager;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private Query queryRating;
    private EmpresasAdapter mAdapter;
    private View mView;
    public EmpresasFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_empresas, container, false);

        //Agregando Toolbar
//        Toolbar toolbar = (Toolbar) mView.findViewById(R.id.toolbar);
//
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(toolbar);
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize Firestore and the main RecyclerView
        initFirestore();
        initRecyclerView();
        return mView;
    }

    public static EmpresasFragment newInstance(){
        EmpresasFragment myFrag = new EmpresasFragment();
        return myFrag;
    }

    private void initFirestore() {
        // TODO(developer): Implement
        mFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);

        mQuery = mFirestore.collection("EmpresaTransporte")
                .orderBy("Alias", Query.Direction.ASCENDING);

        //queryRating = mFirestore.collection("Puntos")

    }

    private void initRecyclerView() {
        rv_buses =(RecyclerView) mView.findViewById(R.id.rvLista_buses);
        if (mQuery == null) {
            Log.w(TAG, "No query, RecyclerView no inicializado");
        }

        mAdapter = new EmpresasAdapter(mQuery, new EmpresasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Empresa empresa) {
                //Toast.makeText(getContext(),empresa.getAlias(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),EmpresasActivity.class);
                intent.putExtra("alias", empresa.getAlias());
//                intent.putExtra("razon_social", empresa.getRazonSocial());
//                intent.putExtra("representante", empresa.getRepresentante());
//                intent.putExtra("direccion", empresa.getDireccion());
//                intent.putExtra("imagen", empresa.getImagen());
//                intent.putExtra("ruc", empresa.getRuc());
                startActivity(intent);
            }
        }) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    rv_buses.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"No se encontraron buses",Toast.LENGTH_SHORT).show();
//                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    rv_buses.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(),"Total buses: " + mAdapter.getItemCount(),Toast.LENGTH_SHORT).show();
//                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "ERROR AL DESCARGAR DATA!!!!!!!!!!!!!!!.", Snackbar.LENGTH_LONG).show();
            }
        };
        if (mAdapter != null) {
            mAdapter.startListening();
            Toast.makeText(getContext(),"Escuchando!",Toast.LENGTH_SHORT).show();
        }
        rv_buses.setHasFixedSize(true);
        rv_buses.setLayoutManager(new GridLayoutManager(getContext(),2));
        rv_buses.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAdapter = null;
    }

}
