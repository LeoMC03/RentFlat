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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import pucp.edu.rentflat.Administrador.AdmiDenunciasDetalleActivity;
import pucp.edu.rentflat.Entity.Denuncia;
import pucp.edu.rentflat.Propietario.PropiNewInmuebleActivity;
import pucp.edu.rentflat.R;

public class DenunciaAdapter extends FirestoreRecyclerAdapter<Denuncia,DenunciaAdapter.ViewHolder> {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Activity actividad;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DenunciaAdapter(@NonNull FirestoreRecyclerOptions<Denuncia> options,Activity activity) {
        super(options);
        this.actividad = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Denuncia model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        String nombreComp1 = model.getCliente().getNombre() + " " + model.getCliente().getApellido();
        String nombreComp2 = model.getPropietario().getNombre() + " "+model.getPropietario().getApellido();
        holder.cliente.setText("Cliente: "+nombreComp1);
        holder.propietario.setText("Propietario: "+nombreComp2);
        holder.fecha.setText(model.getFecha().toString());
        holder.btnvermas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(actividad, AdmiDenunciasDetalleActivity.class);
                i.putExtra("id",id);
                actividad.startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_denuncias,parent,false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cliente,propietario,fecha;
        Button btnvermas;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cliente = itemView.findViewById(R.id.tvclienteCardDenuncia);
            propietario = itemView.findViewById(R.id.tvpropiCardDenuncia);
            fecha = itemView.findViewById(R.id.tvfechaDenuncia);
            btnvermas = itemView.findViewById(R.id.btnVermasDenuncia);
        }
    }
}
