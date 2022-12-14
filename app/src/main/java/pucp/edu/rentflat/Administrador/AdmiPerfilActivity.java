package pucp.edu.rentflat.Administrador;

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
import pucp.edu.rentflat.Propietario.PropiPerfilActivity;
import pucp.edu.rentflat.R;

public class AdmiPerfilActivity extends AppCompatActivity {

    TextView nombre,correo,dni,cel;

    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    BottomNavigationView bottomNavigationView;
    Button btncerrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admi_perfil);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        nombre = findViewById(R.id.tvnombreAdmi);
        cel = findViewById(R.id.tvcelAdmi);
        correo = findViewById(R.id.tvcorreoAdmi);
        dni = findViewById(R.id.tvdniAdmi);
        btncerrar = findViewById(R.id.button2);

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
                startActivity(new Intent(AdmiPerfilActivity.this, LoginActivity.class));
                finish();
            }
        });

        setBottomNavigationView();
    }

    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavPerfilAdmi);
        bottomNavigationView.setSelectedItemId(R.id.pageAdmiPerfil);
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.pageAdmiPrincipal:
                        startActivity(new Intent(AdmiPerfilActivity.this, AdmiPropietariosActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.pageAdmiDenuncias:
                        startActivity(new Intent(AdmiPerfilActivity.this,AdminDenunciasActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.pageAdmiPerfil:
                        return true;
                }
                return false;
            }
        });
    }
}