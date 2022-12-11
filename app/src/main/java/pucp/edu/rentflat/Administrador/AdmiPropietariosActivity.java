package pucp.edu.rentflat.Administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import pucp.edu.rentflat.Adapter.InmuebleAdapter;
import pucp.edu.rentflat.Adapter.PropietarioAdapter;
import pucp.edu.rentflat.Entity.Inmueble;
import pucp.edu.rentflat.Entity.Usuario;
import pucp.edu.rentflat.Propietario.PropiAlquileresActivity;
import pucp.edu.rentflat.Propietario.PropiPerfilActivity;
import pucp.edu.rentflat.R;

public class AdmiPropietariosActivity extends AppCompatActivity {

    RecyclerView rvPropietario;
    FirebaseFirestore firestore;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admi_propietarios);


        rvPropietario = findViewById(R.id.recycleviewPropietario);

        Query query = firestore.collection("usuarios").whereEqualTo("rol","propietario");
        FirestoreRecyclerOptions<Usuario> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Usuario>()
                .setQuery(query,Usuario.class).build();
        PropietarioAdapter propietarioAdapter = new PropietarioAdapter(firestoreRecyclerOptions,this);
        propietarioAdapter.notifyDataSetChanged();
        rvPropietario.setAdapter(propietarioAdapter);
        propietarioAdapter.startListening();

        setBottomNavigationView();

    }

    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavPropiAdmin);
        bottomNavigationView.setSelectedItemId(R.id.pagePrincipal);
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.pageAdmiPrincipal:
                        return true;
                    case R.id.pageAdmiDenuncias:
                        startActivity(new Intent(AdmiPropietariosActivity.this,AdminDenunciasActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.pageAdmiPerfil:
                        startActivity(new Intent(AdmiPropietariosActivity.this, AdmiPerfilActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}