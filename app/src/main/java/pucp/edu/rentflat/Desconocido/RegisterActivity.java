package pucp.edu.rentflat.Desconocido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import pucp.edu.rentflat.Entity.Usuario;
import pucp.edu.rentflat.LoginActivity;
import pucp.edu.rentflat.R;

public class RegisterActivity extends AppCompatActivity {

    EditText nombres,apellidos,dni,correo,password,passwordRepetido,telefono;
    Button btnRegister,btnSesion;
    Spinner rol;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    ArrayList<String> listaUsuarios = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        nombres = findViewById(R.id.etNombresRegister);
        apellidos = findViewById(R.id.etApellidosRegister);
        dni = findViewById(R.id.etdniRegister);
        correo = findViewById(R.id.etCorreoRegister);
        password = findViewById(R.id.etcontrasenaRegister);
        passwordRepetido = findViewById(R.id.etcontrasenaRepetidaRegister);
        btnRegister = findViewById(R.id.buttonRegistar);
        btnSesion = findViewById(R.id.buttonIniciarSesion);
        rol = findViewById(R.id.spinnerRolesRegister);
        telefono = findViewById(R.id.etcelRegister);
        

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreStr = nombres.getText().toString().trim();
                String apellidoStr = apellidos.getText().toString().trim();
                String dniStr = dni.getText().toString();
                String correoStr = correo.getText().toString().trim();
                String passwordStr = password.getText().toString();
                String passwordRepStr = passwordRepetido.getText().toString();
                String rolStr = rol.getSelectedItem().toString();
                String telefonoStr = telefono.getText().toString();
                if(nombreStr.isEmpty() || apellidoStr.isEmpty() || dniStr.isEmpty() || correoStr.isEmpty() || passwordRepStr.isEmpty() || passwordStr.isEmpty() || rolStr.isEmpty() || telefonoStr.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Porfavor llenar todos los campos para registrarse",Toast.LENGTH_SHORT).show();
                }else{
                    if(!passwordStr.equals(passwordRepStr)){
                        Toast.makeText(RegisterActivity.this,"Las contraseñas deben ser iguales",Toast.LENGTH_SHORT).show();

                    }else {

                        firestore.collection("usuarios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot document : task.getResult()){
                                        listaUsuarios.add(document.getString("correo"));
                                    }
                                    if(!listaUsuarios.contains(correoStr)){
                                        registrarUsuario(nombreStr,apellidoStr,dniStr,correoStr,rolStr,passwordStr,telefonoStr);
                                    }else{
                                        Toast.makeText(RegisterActivity.this,"Existe una cuenta asociada a este correo",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

        btnSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void registrarUsuario(String nombreStr, String apellidoStr, String dniStr, String correoStr, String rolStr,String password,String telefonoStr) {

        if(dniStr.length()!=8){
            Toast.makeText(RegisterActivity.this,"El dni debe tener 8 digitos",Toast.LENGTH_SHORT).show();
        }else if(telefonoStr.length()!=9){
            Toast.makeText(RegisterActivity.this,"El telefono debe tener 9 digitos",Toast.LENGTH_SHORT).show();
        }else{
            auth.createUserWithEmailAndPassword(correoStr,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Usuario usuario = new Usuario(nombreStr,apellidoStr,rolStr,dniStr,correoStr,telefonoStr);
                        usuario.setEstado("activo");
                        String id = auth.getCurrentUser().getUid();
                        firestore.collection("usuarios").document(id).set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                auth.getCurrentUser().sendEmailVerification();
                                Toast.makeText(RegisterActivity.this,"Se ha registrado correctamente",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this,"Error al registrarse",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });


        }
    }
}