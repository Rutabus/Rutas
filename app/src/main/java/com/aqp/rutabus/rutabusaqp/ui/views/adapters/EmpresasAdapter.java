package com.aqp.rutabus.rutabusaqp.ui.views.adapters;


import android.content.res.Resources;
import android.media.Rating;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aqp.rutabus.rutabusaqp.App;
import com.aqp.rutabus.rutabusaqp.R;
import com.aqp.rutabus.rutabusaqp.data.entity.Empresa;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmpresasAdapter extends FirebaseAdapter<EmpresasAdapter.ViewHolder> {

    private static final String TAG = "Empresas Adapter";
    FirebaseStorage storage;

    public interface OnItemClickListener {
        void onItemClick(Empresa empresa);
    }
    private OnItemClickListener mListener;

    public EmpresasAdapter(Query query, OnItemClickListener listener) {
        super(query);
        mListener = listener;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public EmpresasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_bus, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpresasAdapter.ViewHolder vh, int i) {
        vh.bind(getSnapshot(i), mListener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civ_empresa_foto)
        public ImageView foto;
        @BindView(R.id.tv_empresa_nombre)
        public TextView nombreEmpresa;
        @BindView(R.id.tv_bus_subtitle)
        public TextView ruc;
        @BindView(R.id.bus_rating)
        public RatingBar rating;
        @BindView(R.id.tv_pasaje)
        TextView pasaje;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot, final OnItemClickListener mListener) {
            final Empresa empresa = snapshot.toObject(Empresa.class);
            Resources resources = itemView.getResources();

            //https://firebasestorage.googleapis.com/v0/b/rutabusaqpdb.appspot.com/o/
            String fotoEmpresa = String.format("rutas/%s", empresa.getImagen());

            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();

            // Create a reference with an initial file path and name
            StorageReference pathReference = storageRef.child(fotoEmpresa);

            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //Load Image
                    Glide.with(foto.getContext())
                            .load(uri)
                            .into(foto);
                }
            });

            nombreEmpresa.setText(empresa.getAlias());
            ruc.setText("RUC: " + empresa.getRuc());
            pasaje.setText("S/ " + empresa.getPrecioMedio());

            //Click Listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (empresa != null)
                        mListener.onItemClick(empresa);
                }
            });

        }
    }
}
