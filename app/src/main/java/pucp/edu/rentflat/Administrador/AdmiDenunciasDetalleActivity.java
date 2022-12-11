package pucp.edu.rentflat.Administrador;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import pucp.edu.rentflat.Entity.Denuncia;
import pucp.edu.rentflat.R;

public class AdmiDenunciasDetalleActivity extends AppCompatActivity {

    TextView nombreCliente,correoCliente,dniCliente;
    TextView nombrePropi,correoPropi,dniPropi;
    TextView motivo;
    TextView numeroDenuncia;

    GridView gridView;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admi_denuncias_detalle);

        firestore = FirebaseFirestore.getInstance();

        nombreCliente = findViewById(R.id.tvnombreClienteDenuncia);
        correoCliente = findViewById(R.id.tvcorreoClienteDenuncia);
        dniCliente = findViewById(R.id.tvdniClienteDenuncia);
        nombrePropi = findViewById(R.id.tvnombrePropiDenuncia);
        correoPropi = findViewById(R.id.tvcorreoPropiDenuncia);
        dniPropi = findViewById(R.id.tvdniPropiDenuncia);
        motivo = findViewById(R.id.motivodenuncia);
        numeroDenuncia = findViewById(R.id.numerodenuncia);

        String id = getIntent().getStringExtra("id");

        firestore.collection("denuncias").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Denuncia denuncia = documentSnapshot.toObject(Denuncia.class);
                String nombreCompletoPropi = denuncia.getPropietario().getNombre() + " " + denuncia.getPropietario().getNombre();
                String nombreCompletoClie = denuncia.getCliente().getNombre() + " " + denuncia.getCliente().getApellido();
                nombrePropi.setText(nombreCompletoPropi);
                nombreCliente.setText(nombreCompletoClie);
                correoCliente.setText(denuncia.getCliente().getCorreo());
                dniCliente.setText(denuncia.getCliente().getDni());
                correoPropi.setText(denuncia.getPropietario().getCorreo());
                dniPropi.setText(denuncia.getPropietario().getDni());
                motivo.setText(denuncia.getMotivo());
                numeroDenuncia.setText("Denunia "+ documentSnapshot.getId());

            }
        });
    }
}