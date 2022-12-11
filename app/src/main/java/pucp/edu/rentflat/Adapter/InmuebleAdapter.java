package pucp.edu.rentflat.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import pucp.edu.rentflat.Entity.Inmueble;
import pucp.edu.rentflat.Propietario.PropiNewInmuebleActivity;
import pucp.edu.rentflat.R;

public class InmuebleAdapter extends FirestoreRecyclerAdapter<Inmueble,InmuebleAdapter.ViewHolder> {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Activity actividad;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public InmuebleAdapter(@NonNull FirestoreRecyclerOptions<Inmueble> options,Activity activity) {
        super(options);
        this.actividad = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Inmueble inmueble) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.tipo.setText(inmueble.getTipo());
        holder.distrito.setText(inmueble.getDistrito());
        holder.amoblado.setText(inmueble.getAmoblado());
        if(inmueble.getEstado().equals("libre")){
            holder.btnstatus.setVisibility(View.GONE);
        }
        if(inmueble.getEstado().equals("alquilado")){
            holder.btnstatus.setText(inmueble.getEstado());
        }

        Glide.with(actividad).load(inmueble.getUrlInmueble().get(0)).into(holder.imageView);

        holder.btnverdetalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(actividad, PropiNewInmuebleActivity.class);
                i.putExtra("id",id);
                actividad.startActivity(i);

            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_inmueble,parent,false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageView;
        TextView tipo,distrito,amoblado;
        Button btnverdetalles,btnstatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivInmuebleFotoCard);
            tipo = itemView.findViewById(R.id.tvTipoCard);
            distrito = itemView.findViewById(R.id.tvDistritoCard);
            amoblado = itemView.findViewById(R.id.tvAmobladoCard);
            btnstatus = itemView.findViewById(R.id.etStatusCard);
            btnverdetalles = itemView.findViewById(R.id.btnVerDetalles);
        }
    }
}
