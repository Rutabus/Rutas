package com.aqp.rutabus.rutabusaqp.ui.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aqp.rutabus.rutabusaqp.R;
import com.aqp.rutabus.rutabusaqp.data.entity.Rutas;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RutasAdapter extends RecyclerView.Adapter<RutasAdapter.MyViewHolder> {
    private List<Rutas> listaRutas;
    private final LayoutInflater inflater;
    private final Context context;
//    private ItemInteractionListener listener;

    public RutasAdapter(List<Rutas> model,  Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context=context;
        listaRutas=new ArrayList<>();
//        for (Rutas ruta : model) {
//            listaRutas.add(ruta);
//        }
        listaRutas = model;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_ruta, viewGroup, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.bind(listaRutas.get(i));
    }

    @Override
    public int getItemCount() {
        return listaRutas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ruta)
        TextView tvRuta;
        private Rutas ruta;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Rutas rutas) {
            this.ruta = rutas;
            tvRuta.setText(ruta.getRuta());
        }
    }
}
