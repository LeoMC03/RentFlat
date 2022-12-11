package pucp.edu.rentflat.Propietario;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import pucp.edu.rentflat.Adapter.PhotoAdapter;
import pucp.edu.rentflat.Entity.Inmueble;
import pucp.edu.rentflat.R;

public class PropiNewInmuebleActivity extends AppCompatActivity {

    Spinner tipo,distrito,amobladoTipo;
    EditText ubicacion,detalles,precio;
    Button btnRegister,btnEliminar,btnEditar,btnEstado;
    ImageSlider isInmuebles;

    BottomNavigationView bottomNavigationView;
    Inmueble inmueble;
    ImageView imageView;
    GridLayout glInmuebles;
    RecyclerView rvfoto;
    ImageButton btngaleria;
    ImageButton btncamara;
    ProgressBar pbfoto;

    private Uri cameraUri;
    String id;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    private List<String> listFotos = new ArrayList<>();
    PhotoAdapter fotoAdapter;
    ActivityResultLauncher<Intent> launcherPhotoDocument = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = result.getData().getData();
                    compressImageAndUpload(uri,50);
                } else {
                    Toast.makeText(PropiNewInmuebleActivity.this, "Debe seleccionar un archivo", Toast.LENGTH_SHORT).show();
                }
            }
    );

    ActivityResultLauncher<Intent> launcherPhotoCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    compressImageAndUpload(cameraUri,25);
                } else {
                    Toast.makeText(PropiNewInmuebleActivity.this, "Debe seleccionar un archivo", Toast.LENGTH_SHORT).show();
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propi_new_inmueble);

       firestore = FirebaseFirestore.getInstance();
       auth = FirebaseAuth.getInstance();

        id = getIntent().getStringExtra("id");

        tipo = findViewById(R.id.spTipoInmuRegister);
        distrito = findViewById(R.id.spDistrInmuRegister);
        amobladoTipo = findViewById(R.id.spAmobInmuRegister);
        ubicacion = findViewById(R.id.etUbicacionInmuRegister);
        detalles = findViewById(R.id.etDetInmuRegister);
        precio = findViewById(R.id.etPrecioInmuRegister);
        btnRegister = findViewById(R.id.buttonRegistrarInmu);
        btnEditar = findViewById(R.id.buttonEditarInmu);
        btnEliminar = findViewById(R.id.buttonEliminarInmu);
        isInmuebles = findViewById(R.id.isInmueblesImages);
        btnEstado = findViewById(R.id.buttonEstado);
        imageView = findViewById(R.id.image);

        glInmuebles = findViewById(R.id.glCreateInmueble);
        rvfoto = findViewById(R.id.rvCreateInmuebleFotos);
        btngaleria = findViewById(R.id.ibCreateInmueblePhotoGal);
        btncamara = findViewById(R.id.ibCreateInmueblePhotoCam);
        pbfoto = findViewById(R.id.pbAddPhotoInmueble);



        if(id==null || id==""){
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tipoStr = tipo.getSelectedItem().toString();
                    String distritoStr = distrito.getSelectedItem().toString();
                    String amobladoStr = amobladoTipo.getSelectedItem().toString();
                    String ubicacionStr = ubicacion.getText().toString();
                    String detallesStr = detalles.getText().toString();
                    String precioStr = precio.getText().toString();
                    if(tipoStr.isEmpty() || distritoStr.isEmpty() || amobladoStr.isEmpty() || ubicacionStr.isEmpty() || detallesStr.isEmpty() || precioStr.isEmpty()){
                        Toast.makeText(PropiNewInmuebleActivity.this,"Los campos no pueden estar vacios",Toast.LENGTH_SHORT).show();

                    }else {
                        crearInmueble(tipoStr,distritoStr,amobladoStr,ubicacionStr,detallesStr,precioStr,listFotos);
                    }

                }
            });
        }else{
            tipo.setFocusable(false);
            tipo.setClickable(false);
            distrito.setFocusable(false);
            distrito.setClickable(false);
            amobladoTipo.setFocusable(false);
            amobladoTipo.setClickable(false);
            ubicacion.setTextIsSelectable(false);
            ubicacion.setFocusable(false);
            detalles.setFocusable(false);
            detalles.setTextIsSelectable(false);
            precio.setFocusable(false);
            precio.setTextIsSelectable(false);
            isInmuebles.setVisibility(View.VISIBLE);
            glInmuebles.setVisibility(View.GONE);

            btnRegister.setVisibility(View.GONE);
            btnEditar.setVisibility(View.VISIBLE);
            btnEliminar.setVisibility(View.VISIBLE);
            btncamara.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            btngaleria.setVisibility(View.GONE);
            obtenerInmueble(id);
            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(inmueble.getEstado().equals("libre")){
                        btnEstado.setText("Alquilado");
                    }
                    if(inmueble.getEstado().equals("alquilado")){
                        btnEstado.setText("Libre");
                    }
                    btnEstado.setVisibility(View.VISIBLE);
                    tipo.setFocusable(true);
                    tipo.setClickable(true);
                    distrito.setFocusable(true);
                    distrito.setClickable(true);
                    amobladoTipo.setFocusable(true);
                    amobladoTipo.setClickable(true);
                    ubicacion.setTextIsSelectable(true);
                    ubicacion.setFocusable(true);
                    detalles.setFocusable(true);
                    detalles.setTextIsSelectable(true);
                    precio.setFocusable(true);
                    precio.setTextIsSelectable(true);
                    isInmuebles.setVisibility(View.GONE);
                    glInmuebles.setVisibility(View.VISIBLE);
                    btnRegister.setVisibility(View.VISIBLE);
                    btncamara.setVisibility(View.VISIBLE);
                    btngaleria.setVisibility(View.VISIBLE);
                    btnRegister.setText("Actualizar");
                    btnEditar.setVisibility(View.GONE);
                    btnEliminar.setVisibility(View.GONE);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(PropiNewInmuebleActivity.this, 2);
                    fotoAdapter = new PhotoAdapter(PropiNewInmuebleActivity.this, listFotos);
                    GridLayoutManager layoutManager = new GridLayoutManager(PropiNewInmuebleActivity.this, 6);
                    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            switch (position % 5) {
                                case 0:
                                case 1:
                                case 2:
                                    return 2;
                                case 3:
                                case 4:
                                    return 3;
                            }
                            throw new IllegalStateException("internal error");
                        }
                    });
                    rvfoto.setAdapter(fotoAdapter);
                    rvfoto.setLayoutManager(gridLayoutManager);
                    evaluarEmpty();
                    btnEliminar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            eliminarInmueble(view);
                        }
                    });

                    btnEstado.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(inmueble.getEstado().equals("libre")){
                                inmueble.setEstado("alquilado");

                            }
                            if(inmueble.getEstado().equals("alguilado")){
                                inmueble.setEstado("libre");
                            }

                        }
                    });

                    btnRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String tipoStr = tipo.getSelectedItem().toString();
                            String distritoStr = distrito.getSelectedItem().toString();
                            String amobladoStr = amobladoTipo.getSelectedItem().toString();
                            String ubicacionStr = ubicacion.getText().toString();
                            String detallesStr = detalles.getText().toString();
                            String precioStr = precio.getText().toString();
                            if(tipoStr.isEmpty() || distritoStr.isEmpty() || amobladoStr.isEmpty() || ubicacionStr.isEmpty() || detallesStr.isEmpty() || precioStr.isEmpty()){
                                Toast.makeText(PropiNewInmuebleActivity.this,"Los campos no pueden estar vacios",Toast.LENGTH_SHORT).show();

                            }else {
                                actualizarInmueble(tipoStr,distritoStr,amobladoStr,ubicacionStr,detallesStr,precioStr,id,listFotos);
                            }

                        }
                    });
                }
            });
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


        }


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        fotoAdapter = new PhotoAdapter(this, listFotos);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position % 5) {
                    case 0:
                    case 1:
                    case 2:
                        return 2;
                    case 3:
                    case 4:
                        return 3;
                }
                throw new IllegalStateException("internal error");
            }
        });

        rvfoto.setAdapter(fotoAdapter);
        rvfoto.setLayoutManager(gridLayoutManager);
        evaluarEmpty();


    }

    private void crearInmueble(String tipoStr, String distritoStr, String amobladoStr, String ubicacionStr, String detallesStr, String precioStr,List<String> lista) {

        if(lista.size()<2 || lista.size() > 5){
            Toast.makeText(PropiNewInmuebleActivity.this,"Debe tener entre dos a cinco fotos",Toast.LENGTH_SHORT).show();
        }else {
            Inmueble inmueble = new Inmueble(tipoStr,distritoStr,ubicacionStr,amobladoStr,precioStr,detallesStr,lista);
            inmueble.setUid(auth.getUid());
            inmueble.setEstado("libre");
            firestore.collection("inmuebles").add(inmueble).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(PropiNewInmuebleActivity.this,"Inmueble agregado correctamente",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PropiNewInmuebleActivity.this,PropiAlquileresActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PropiNewInmuebleActivity.this,"Error al crear",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void obtenerInmueble(String id){
        firestore.collection("inmuebles").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                inmueble = new Inmueble();
                inmueble = documentSnapshot.toObject(Inmueble.class);
                ubicacion.setText(inmueble.getUbicacion());
                detalles.setText(inmueble.getDetalles());
                precio.setText(inmueble.getPrecio());
                for(int i = 0; i < tipo.getCount(); i++){
                    if(tipo.getItemAtPosition(i).toString().equals(inmueble.getTipo())){
                        tipo.setSelection(i);
                        break;
                    }
                }
                for(int i = 0; i < amobladoTipo.getCount(); i++){
                    if(amobladoTipo.getItemAtPosition(i).toString().equals(inmueble.getAmoblado())){
                        amobladoTipo.setSelection(i);
                        break;
                    }
                }
                for(int i = 0; i < distrito.getCount(); i++){
                    if(distrito.getItemAtPosition(i).toString().equals(inmueble.getDistrito())){
                        distrito.setSelection(i);
                        break;
                    }
                }
                listFotos = inmueble.getUrlInmueble();
                //faltaria setear el slider
                ArrayList<SlideModel> slideModels = new ArrayList<>();
                for (String url : inmueble.getUrlInmueble()){
                    slideModels.add(new SlideModel(url, ScaleTypes.CENTER_CROP));
                }
                isInmuebles.setImageList(slideModels);
            }
        });
    }


    private void actualizarInmueble(String tipoStr, String distritoStr, String amobladoStr, String ubicacionStr, String detallesStr, String precioStr,String id,List<String> lista){

        if(lista.size()<2 || lista.size() > 5){
            Toast.makeText(PropiNewInmuebleActivity.this,"Debe tener entre dos a cinco fotos",Toast.LENGTH_SHORT).show();
        }else{
            inmueble.setTipo(tipoStr);
            inmueble.setAmoblado(amobladoStr);
            inmueble.setUbicacion(ubicacionStr);
            inmueble.setDistrito(distritoStr);
            inmueble.setDetalles(detallesStr);
            inmueble.setPrecio(precioStr);
            firestore.collection("inmuebles").document(id).set(inmueble).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(PropiNewInmuebleActivity.this,"Inmueble actualizado correctamente",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PropiNewInmuebleActivity.this,PropiAlquileresActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PropiNewInmuebleActivity.this,"Error al actualizar",Toast.LENGTH_SHORT).show();
                }
            });
        }




    }


    public void compressImageAndUpload(Uri uri, int quality){
        try{
            Bitmap originalImage = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            originalImage.compress(Bitmap.CompressFormat.JPEG,quality,stream);
            subirImagenAFirebase(stream.toByteArray());
        }catch (Exception e){
            Log.d("msg","error",e);
        }
    }

    public void subirImagenAFirebase(byte[] imageBytes) {
        StorageReference photoChild = FirebaseStorage.getInstance().getReference().child("fotos/inmuebles/" + "photo_" + Timestamp.now().getSeconds() + ".jpg");
        pbfoto.setVisibility(View.VISIBLE);
        photoChild.putBytes(imageBytes).addOnSuccessListener(taskSnapshot -> {
            pbfoto.setVisibility(View.GONE);
            photoChild.getDownloadUrl().addOnSuccessListener(uri -> {
                listFotos.add(uri.toString());
                fotoAdapter.notifyDataSetChanged();
                evaluarEmpty();
            }).addOnFailureListener(e ->{
                Toast.makeText(PropiNewInmuebleActivity.this, "Hubo un error al subir la imagen", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            pbfoto.setVisibility(View.GONE);
            Toast.makeText(PropiNewInmuebleActivity.this, "Hubo un error al subir la imagen", Toast.LENGTH_SHORT).show();
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                long bytesTransferred = snapshot.getBytesTransferred();
                long totalByteCount = snapshot.getTotalByteCount();
                double progreso = (100.0 * bytesTransferred) / totalByteCount;
                Long round = Math.round(progreso);
                pbfoto.setProgress(round.intValue());
            }
        });
    }

    public void evaluarEmpty(){
        if (listFotos.size()>0){
            rvfoto.setVisibility(View.VISIBLE);
            glInmuebles.setVisibility(View.GONE);
        }else{
            rvfoto.setVisibility(View.GONE);
            glInmuebles.setVisibility(View.VISIBLE);
        }
    }

    public void removerFoto(int position){
        listFotos.remove(position);
        fotoAdapter.notifyDataSetChanged();
        evaluarEmpty();
    }

    public void uploadPhotoFromDocument(View view) {
        if (pbfoto.getVisibility()==View.VISIBLE){
            Toast.makeText(PropiNewInmuebleActivity.this, "Espera a que se termine de subir la foto", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            launcherPhotoDocument.launch(intent);
        }
    }

    public void uploadPhotoFromCamera(View view) {
        if (pbfoto.getVisibility()==View.VISIBLE){
            Toast.makeText(PropiNewInmuebleActivity.this, "Espera a que se termine de subir la foto", Toast.LENGTH_SHORT).show();
        }else{
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
            cameraUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            launcherPhotoCamera.launch(cameraIntent);
        }
    }

    public void eliminarInmueble(View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("¿Esta seguro que desea eliminar este inmueble");
        alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firestore.collection("inmuebles").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PropiNewInmuebleActivity.this, "Inmueble eliminado correctamente", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PropiNewInmuebleActivity.this,PropiAlquileresActivity.class));
                        finish();
                    }
                });
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

}