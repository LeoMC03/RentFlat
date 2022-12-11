package pucp.edu.rentflat.Cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.awt.font.TextAttribute;

import pucp.edu.rentflat.Entity.Denuncia;
import pucp.edu.rentflat.Entity.Inmueble;
import pucp.edu.rentflat.Entity.Usuario;
import pucp.edu.rentflat.R;

public class ClienteInmuebleInfoActivity extends AppCompatActivity {

    TextView tipo,distrito,direccion,amboblado,precio,detalle;
    TextView nombre,telefono,correo;
    Button btndenunciar,btnenviar,btncancelar,btnmensaje;
    TextView motivo;

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Inmueble inmueble;
    Usuario propietario;
    Usuario cliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_inmueble_info);

        firestore = FirebaseFirestore.getInstance();

        tipo = findViewById(R.id.tvDepaDetalle);
        distrito = findViewById(R.id.tvDistritoDetalle);
        direccion = findViewById(R.id.tvLocacionDetalle);
        amboblado = findViewById(R.id.tvAmobladoDetalle);
        precio = findViewById(R.id.tvCostoDetalle);
        detalle = findViewById(R.id.tvDescripcionDetalle);
        nombre = findViewById(R.id.tvPropietarioNombreDetalle);
        telefono = findViewById(R.id.tvPropietarioTelefonoDetalle);
        correo = findViewById(R.id.tvPropietariocorreoDetalle);

        String id = getIntent().getStringExtra("id");

        firestore.collection("inmuebles").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                inmueble = documentSnapshot.toObject(Inmueble.class);
                firestore.collection("usuarios").document(inmueble.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        propietario = documentSnapshot.toObject(Usuario.class);
                        tipo.setText(inmueble.getTipo());
                        distrito.setText(inmueble.getDistrito());
                        direccion.setText(inmueble.getUbicacion());
                        amboblado.setText(inmueble.getAmoblado());
                        precio.setText(inmueble.getPrecio()+" (soles)");
                        detalle.setText(inmueble.getDetalles());
                        String nombreCompleto = propietario.getNombre() + " "+ propietario.getApellido();
                        nombre.setText(nombreCompleto);
                        telefono.setText(propietario.getTelefono());
                        correo.setText(propietario.getCorreo());
                    }
                });
            }
        });

        btndenunciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnenviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firestore.collection("usuarios").document(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                cliente = documentSnapshot.toObject(Usuario.class);

                                motivo.setVisibility(View.VISIBLE);
                                btnmensaje.setVisibility(View.GONE);
                                btndenunciar.setVisibility(View.GONE);
                                btnenviar.setVisibility(View.VISIBLE);
                                btncancelar.setVisibility(View.VISIBLE);
                                String motivoStr = motivo.getText().toString();
                                Denuncia denuncia = new Denuncia(cliente,propietario, Timestamp.now(),motivoStr);
                                firestore.collection("denuncias").add(denuncia).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(ClienteInmuebleInfoActivity.this, "Se ha enviado la denuncia correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });

                    }
                });
                btncancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btncancelar.setVisibility(View.GONE);
                        btnenviar.setVisibility(View.GONE);
                        btnmensaje.setVisibility(View.VISIBLE);
                        motivo.setVisibility(View.GONE);
                        btndenunciar.setVisibility(View.VISIBLE);
                    }
                });

            }
        });

    }
}