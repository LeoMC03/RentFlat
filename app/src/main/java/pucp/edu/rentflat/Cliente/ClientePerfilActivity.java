package pucp.edu.rentflat.Cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import pucp.edu.rentflat.Administrador.AdmiPerfilActivity;
import pucp.edu.rentflat.Entity.Usuario;
import pucp.edu.rentflat.LoginActivity;
import pucp.edu.rentflat.R;

public class ClientePerfilActivity extends AppCompatActivity {

    TextView nombre,dni,correo,cel;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    Button btncerrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_perfil);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        nombre = findViewById(R.id.tvnombreClientePerfil);
        cel = findViewById(R.id.tvcelClientePerfil);
        dni = findViewById(R.id.tvdniClientePerfil);
        correo = findViewById(R.id.tvcorreoClientePerfil);
        btncerrar = findViewById(R.id.button4);

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
                startActivity(new Intent(ClientePerfilActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

}