package pucp.edu.rentflat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import pucp.edu.rentflat.Administrador.AdmiPropietariosActivity;
import pucp.edu.rentflat.Cliente.ClienteInmueblesActivity;
import pucp.edu.rentflat.Desconocido.RegisterActivity;
import pucp.edu.rentflat.Propietario.PropiAlquileresActivity;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Button btnLogin,btnForgotpasswd,btnregister;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editTextEmailLogin);
        password = findViewById(R.id.editTextPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnForgotpasswd = findViewById(R.id.btnForgotPwd);
        btnregister = findViewById(R.id.btnIrRegistar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailStr = email.getText().toString();
                String passwdStr = password.getText().toString();

                if(emailStr.isEmpty() || passwdStr.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Por favor lleno todos lo campos",Toast.LENGTH_SHORT).show();
                }else{
                    loginUsuario(emailStr,passwdStr);
                }
            }
        });


        btnForgotpasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });
    }

    private void loginUsuario(String emailStr, String passwdStr) {

        auth.signInWithEmailAndPassword(emailStr,passwdStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    firestore.collection("usuarios").document(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()){
                                    String rol = document.getString("rol");
                                    if(rol.equals("cliente")){
                                        startActivity(new Intent(LoginActivity.this, ClienteInmueblesActivity.class));
                                        finish();
                                    }else if(rol.equals("propietario")){
                                        startActivity(new Intent(LoginActivity.this, PropiAlquileresActivity.class));
                                        finish();
                                    }else{
                                        startActivity(new Intent(LoginActivity.this, AdmiPropietariosActivity.class));
                                        finish();
                                    }
                                }

                            }
                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,"Credenciales incorrectas",Toast.LENGTH_SHORT).show();
            }
        });
    }
}