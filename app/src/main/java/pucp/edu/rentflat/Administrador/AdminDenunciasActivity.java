package pucp.edu.rentflat.Administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import pucp.edu.rentflat.Adapter.DenunciaAdapter;
import pucp.edu.rentflat.Adapter.InmuebleAdapter;
import pucp.edu.rentflat.Entity.Denuncia;
import pucp.edu.rentflat.Entity.Inmueble;
import pucp.edu.rentflat.R;

public class AdminDenunciasActivity extends AppCompatActivity {

    RecyclerView rvDenuncias;
    FirebaseFirestore firestore;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_denuncias);

        firestore = FirebaseFirestore.getInstance();

        rvDenuncias = findViewById(R.id.recycleviewDenuncias);

        rvDenuncias.setLayoutManager(new LinearLayoutManager(this));
        Query query = firestore.collection("denuncias");
        FirestoreRecyclerOptions<Denuncia> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Denuncia>()
                .setQuery(query,Denuncia.class).build();
        DenunciaAdapter denunciaAdapter = new DenunciaAdapter(firestoreRecyclerOptions,this);
        denunciaAdapter.notifyDataSetChanged();
        rvDenuncias.setAdapter(denunciaAdapter);
        denunciaAdapter.startListening();

        setBottomNavigationView();
    }

    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavDenunciaAdmi);
        bottomNavigationView.setSelectedItemId(R.id.pageAdmiDenuncias);
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.pageAdmiPrincipal:
                        startActivity(new Intent(AdminDenunciasActivity.this, AdmiPropietariosActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.pageAdmiDenuncias:
                        return true;
                    case R.id.pageAdmiPerfil:
                        startActivity(new Intent(AdminDenunciasActivity.this, AdmiPerfilActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}