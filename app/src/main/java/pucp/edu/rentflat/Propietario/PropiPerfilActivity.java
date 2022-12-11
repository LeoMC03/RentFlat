package pucp.edu.rentflat.Propietario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import pucp.edu.rentflat.Entity.Usuario;
import pucp.edu.rentflat.LoginActivity;
import pucp.edu.rentflat.R;

public class PropiPerfilActivity extends AppCompatActivity {

    TextView nombre,dni,correo,cel;
    BottomNavigationView bottomNavigationView;

    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    Button btncerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propi_perfil);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        nombre = findViewById(R.id.tvnombreperfilpropi);
        dni = findViewById(R.id.tvdniperfilpropi);
        correo = findViewById(R.id.tvcorreoperfilpropi);
        cel = findViewById(R.id.tvcelperfilpropi);
        btncerrar = findViewById(R.id.button3);

        firestore.collection("usuarios").document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuario usuario = documentSnapshot.toObject(Usuario.class);
                String nombreCompleto = usuario.getNombre() + " " + usuario.getApellido();
                nombre.setText(nombreCompleto);
                cel.setText(usuario.getTelefono());
                dni.setText(usuario.getDni());
                correo.setText(usuario.getCorreo());

            }
        });

        btncerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(PropiPerfilActivity.this, LoginActivity.class));
                finish();
            }
        });

        setBottomNavigationView();
    }

    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavPerfilPropi);
        bottomNavigationView.setSelectedItemId(R.id.pagePerfil);
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.pagePrincipal:
                        Intent intent = new Intent(PropiPerfilActivity.this,PropiAlquileresActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.pageGestion:
                        Intent i = new Intent(PropiPerfilActivity.this,PropiAlquileresActivity.class);
                        i.putExtra("lista","lista");
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.pagePerfil:
                        return true;
                }
                return false;
            }
        });
    }



}