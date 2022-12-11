package pucp.edu.rentflat.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import pucp.edu.rentflat.Administrador.AdmiPropietarioDetalleActivity;
import pucp.edu.rentflat.Entity.Usuario;
import pucp.edu.rentflat.Propietario.PropiNewInmuebleActivity;
import pucp.edu.rentflat.R;

public class PropietarioAdapter extends FirestoreRecyclerAdapter<Usuario,PropietarioAdapter.ViewHolder> {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Activity actividad;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PropietarioAdapter(@NonNull FirestoreRecyclerOptions<Usuario> options,Activity activity) {
        super(options);
        this.actividad = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Usuario model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();
        String nombreCompleto = model.getNombre() + " " + model.getApellido();
        holder.nombre.setText(nombreCompleto);
        holder.estado.setText(model.getEstado());
        if(model.getEstado().equals("activo")){
            holder.estado.setBackgroundColor(Color.GREEN);
        }
        holder.btnVermas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(actividad, AdmiPropietarioDetalleActivity.class);
                i.putExtra("id",id);
                actividad.startActivity(i);

            }
        });



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_propietario,parent,false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        Button btnVermas,estado;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombreCardProp);
            btnVermas = itemView.findViewById(R.id.btnVermas);
            estado = itemView.findViewById(R.id.btnEstadoCardProp);
        }
    }
}
