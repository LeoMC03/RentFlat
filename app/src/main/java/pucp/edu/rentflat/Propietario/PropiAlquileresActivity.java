package pucp.edu.rentflat.Propietario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import pucp.edu.rentflat.Adapter.InmuebleAdapter;
import pucp.edu.rentflat.Entity.Inmueble;
import pucp.edu.rentflat.R;

public class PropiAlquileresActivity extends AppCompatActivity {

    RecyclerView rvInmuebles;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FloatingActionButton floatadd;
    Query query;
    BottomNavigationView bottomNavigationView;
    TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propi_alquileres);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        rvInmuebles = findViewById(R.id.recycleviewInmueblesPropietario);
        floatadd = findViewById(R.id.floatbuttinAdd);
        bottomNavigationView = findViewById(R.id.bottomNavAlquiPropi);
        titulo = findViewById(R.id.titulo);

        String lista = getIntent().getStringExtra("lista");

        rvInmuebles.setLayoutManager(new LinearLayoutManager(this));
        if(lista== null || lista==""){
            bottomNavigationView.setSelectedItemId(R.id.pagePrincipal);
            floatadd.setVisibility(View.GONE);
            query = firestore.collection("inmuebles").whereEqualTo("uid",auth.getUid()).whereEqualTo("estado","alquilado");
        }else{
            titulo.setText("Lista Total de inmuebles");
            bottomNavigationView.setSelectedItemId(R.id.pageGestion);
            query = firestore.collection("inmuebles").whereEqualTo("uid",auth.getUid());

        }
        FirestoreRecyclerOptions<Inmueble> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Inmueble>()
                .setQuery(query,Inmueble.class).build();
        InmuebleAdapter inmuebleAdapter = new InmuebleAdapter(firestoreRecyclerOptions,this);
        inmuebleAdapter.notifyDataSetChanged();
        rvInmuebles.setAdapter(inmuebleAdapter);
        inmuebleAdapter.startListening();

        setBottomNavigationView();

    }

    public void irInmueble(View view){
        startActivity(new Intent(this,PropiNewInmuebleActivity.class));
        finish();
    }

    public void setBottomNavigationView(){
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.pagePrincipal:
                        startActivity(new Intent(PropiAlquileresActivity.this,PropiAlquileresActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.pageGestion:
                        Intent i = new Intent(PropiAlquileresActivity.this,PropiAlquileresActivity.class);
                        i.putExtra("lista","lista");
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.pagePerfil:
                        startActivity(new Intent(PropiAlquileresActivity.this,PropiPerfilActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}