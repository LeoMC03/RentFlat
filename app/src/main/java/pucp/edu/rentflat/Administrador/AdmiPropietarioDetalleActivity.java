package pucp.edu.rentflat.Administrador;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import pucp.edu.rentflat.Entity.Usuario;
import pucp.edu.rentflat.R;

public class AdmiPropietarioDetalleActivity extends AppCompatActivity {

    TextView nombre,telfono,correo,dni;
    Button btnbloquear;

    FirebaseFirestore firestore;
    Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admi_propietario_detalle);
        firestore = FirebaseFirestore.getInstance();

        nombre = findViewById(R.id.tvnombrePropi);
        telfono = findViewById(R.id.tvcelPropi);
        correo = findViewById(R.id.tvcorreoPropi);
        dni = findViewById(R.id.tvdniPropi);
        btnbloquear = findViewById(R.id.btnbloquear);

        String id = getIntent().getStringExtra("id");

        firestore.collection("usuarios").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuario = documentSnapshot.toObject(Usuario.class);
                String nombreCompleto = usuario.getNombre() + " "+ usuario.getApellido();
                nombre.setText(nombreCompleto);
                telfono.setText(usuario.getTelefono());
                correo.setText(usuario.getCorreo());
                dni.setText(usuario.getDni());
            }
        });

        btnbloquear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario.setEstado("bloqueado");
                firestore.collection("usuarios").document(id).set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AdmiPropietarioDetalleActivity.this, "El propetario fue bloqueado exitosamente", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}