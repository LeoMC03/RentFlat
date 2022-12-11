package pucp.edu.rentflat.Cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import pucp.edu.rentflat.Adapter.InmuebleAdapter;
import pucp.edu.rentflat.Entity.Inmueble;
import pucp.edu.rentflat.R;

public class ClienteInmueblesActivity extends AppCompatActivity {

    RecyclerView rvInmuebles;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_inmuebles);

        rvInmuebles = findViewById(R.id.recycleviewInmuebles);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        rvInmuebles.setLayoutManager(new LinearLayoutManager(this));
        Query query = firestore.collection("inmuebles");
        FirestoreRecyclerOptions<Inmueble> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Inmueble>()
                .setQuery(query,Inmueble.class).build();
        InmuebleAdapter inmuebleAdapter = new InmuebleAdapter(firestoreRecyclerOptions,this);
        inmuebleAdapter.notifyDataSetChanged();
        rvInmuebles.setAdapter(inmuebleAdapter);
        inmuebleAdapter.startListening();

    }
}