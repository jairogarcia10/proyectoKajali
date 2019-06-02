package com.example.kajali;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOutOfMemoryException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kajali.Modelo.Registro_Restaurantes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private String cameraFilePath;
    Button btn_registrar;
    EditText txtNombre, txtDescripcion, txtTelefono, txtHorario, txtProvincia, txtCategoria;
    ImageView  imgR, imgP;
    Uri imagen1, imagen2;
    private final int GALERIA_IMAGENES=1, CAMARA1=2, GALERIA=3, CAMARA2=4;
    private static final String NOMBRE_DB="appkajali";
    private static SQLiteDatabase db;
    public static final String tRestaurantes= "CREATE TABLE IF NOT EXISTS appRestaurantes("+"id INTEGER PRIMARY KEY AUTOINCREMENT," +" nombreR VARCHAR NOT NULL,"+" telefonoR VARCHAR NOT NULL,"+"descripcionR VARCHAR NOT NULL,"+
                                                   "horarioR VARCHAR NOT NULL,"+"imgR VARCHAR,"+"platoD VARCHAR,"+"provinciaR VARCHAR NOT NULL,"+"categoriaR VARCHAR NOT NULL);";
     String nombre, telefono,descripcion, horario, provincia, categoria;

    Registro_Restaurantes registroR= new Registro_Restaurantes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_registrar=findViewById(R.id.btnRegistrarse);
        txtNombre=findViewById(R.id.txtNombre);
        txtTelefono=findViewById(R.id.txtTelefono);
        txtDescripcion=findViewById(R.id.txtDescripcion);
        txtHorario=findViewById(R.id.txtHoraio);
        txtProvincia=findViewById(R.id.txtProvincia);
        txtCategoria=findViewById(R.id.txtCategoria);
        imgR=findViewById(R.id.dimgR);
        imgP=findViewById(R.id.dimgP);

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre=txtNombre.getText().toString();
                telefono=txtTelefono.getText().toString();
                descripcion=txtDescripcion.getText().toString();
                horario=txtHorario.getText().toString();
                provincia=txtProvincia.getText().toString();
                categoria=txtCategoria.getText().toString();

                if (nombre.isEmpty()||telefono.isEmpty()||descripcion.isEmpty()||horario.isEmpty()||provincia.isEmpty()||categoria.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Por favor llene todos los campos",Toast.LENGTH_SHORT).show();
                }else{
                    if (registroR.addRestaurante(nombre,telefono,descripcion,horario,imagen1,imagen2,provincia,categoria,db))
                    {
                        Toast.makeText(getApplicationContext(),"El restaurante fue agregado correctamente",Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                        Intent intent = new Intent(MainActivity.this,lista_Restaurantes.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"Error al agregar restaurantes",Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                    }
                }
            }
        });// fin del boton registrar
        /*imgR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mostrar un mensaje de confirmación antes de realizar una acción
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Seleccion de la imagen de usuario");
                alertDialog.setMessage("Qué desea utilizar?");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCancelable(true);

                //Define al botón positivo como galeria y si se selecciona se realiza un intent para obtener los datos
                alertDialog.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        //Es que reciba un resultado se inicia un activity llamado startActivityForResult
                        startActivityForResult(Intent.createChooser(intent,"Seleccionar foto"),GALERIA_IMAGENES);



                    }
                });
                alertDialog.setNegativeButton("Camara", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        //Es para que reciba un resultado, se inicia un activity llamado startActivityForResult
                        startActivityForResult(intent, CAMARA);
                    }
                });
                alertDialog.show();
            }
        });*/
        imgR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mostrar un mensaje de confirmación antes de realizar una acción
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Seleccion de la imagen de usuario");
                alertDialog.setMessage("Qué desea utilizar?");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCancelable(true);

                //Define al botón positivo como galeria y si se selecciona se realiza un intent para obtener los datos
                alertDialog.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pickFromGallery1();



                    }
                });
                alertDialog.setNegativeButton("Camara", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        captureFromCamera1();
                    }
                });
                alertDialog.show();
            }
        });




        imgP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mostrar un mensaje de confirmación antes de realizar una acción
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Seleccion de la imagen de usuario");
                alertDialog.setMessage("Qué desea utilizar?");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCancelable(true);

                //Define al botón positivo como galeria y si se selecciona se realiza un intent para obtener los datos
                alertDialog.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pickFromGallery2();



                    }
                });
                alertDialog.setNegativeButton("Camara", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        captureFromCamera2();
                    }
                });
                alertDialog.show();
            }
        });


       /* imgP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Mostrar un mensaje de confirmación antes de realizar una acción
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Seleccion de la imagen de usuario");
                alertDialog.setMessage("Qué desea utilizar?");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCancelable(true);

                //Define al botón positivo como galeria y si se selecciona se realiza un intent para obtener los datos
                alertDialog.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        //Es que reciba un resultado se inicia un activity llamado startActivityForResult
                        startActivityForResult(Intent.createChooser(intent,"Seleccionar foto"),GALERIA);



                    }
                });
                alertDialog.setNegativeButton("Camara", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        //Es para que reciba un resultado, se inicia un activity llamado startActivityForResult
                        startActivityForResult(intent, CAMARA);
                    }
                });
                alertDialog.show();
            }
        });*/

        createDatabase();

    }//fin de oncreate



    public void createDatabase(){
        try {
            db = openOrCreateDatabase(NOMBRE_DB, MODE_PRIVATE, null);
            //deleteDatabase(NOMBRE_DB);
            db.execSQL(tRestaurantes);
        } catch (SQLiteOutOfMemoryException e){
            e.printStackTrace();
        }
    }

    public void limpiarCampos(){
        txtNombre.setText("");
        txtTelefono.setText("");
        txtDescripcion.setText("");
        txtHorario.setText("");
        imgR.setImageResource(R.mipmap.ic_launcher);
        imgP.setImageResource(R.mipmap.ic_launcher);
        txtProvincia.setText("");
        txtCategoria.setText("");
    }


    /*public void onActivityResult(int rqCode, int resCode, Intent data){
        if(resCode==RESULT_OK){
            switch (rqCode){
                case GALERIA_IMAGENES:
                    imagen1= data.getData();

                    imgR.setImageURI(imagen1);

                    Toast.makeText(getApplicationContext(), "Imagen Cargada correctamente", Toast.LENGTH_SHORT).show();
                    break;
                case CAMARA:
                    if(data!=null){
                        Bitmap thumbnail = (Bitmap)data.getExtras().get("data");
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        File destination = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis()+".jpg");
                        FileOutputStream fo;
                        try{
                            fo = new FileOutputStream(destination);
                        }catch (FileNotFoundException ex){
                            ex.printStackTrace();
                        }catch (IOException io){
                            io.printStackTrace();
                        }
                        imgR.setImageBitmap(thumbnail);
                        imagen1 = data.getData();
                        imgP.setImageBitmap(thumbnail);
                        imagen2 = data.getData();
                        Toast.makeText(getApplicationContext(),"Exito al cargar la foto de la Camara", Toast.LENGTH_SHORT).show();
                    }//end if

                    break;
                case GALERIA:
                    imagen2=data.getData();
                    imgP.setImageURI(imagen2);
                    Toast.makeText(getApplicationContext(), "Imagen Cargada correctamente", Toast.LENGTH_SHORT).show();
                    break;
            }
        }else {
            Toast.makeText(getApplicationContext(),"Ha ocurrido un error: Camara", Toast.LENGTH_SHORT).show();
        }
    }//end*/



    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALERIA_IMAGENES:
                    //data.getData return the content URI for the selected Image
                    imagen1 = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(imagen1, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    //Get the column index of MediaStore.Images.Media.DATA
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //Gets the String value in the column
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    // Set the Image in ImageView after decoding the String
                    imgR.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                    Toast.makeText(getApplicationContext(), "Imagen Cargada correctamente", Toast.LENGTH_SHORT).show();
                    break;

                case GALERIA:
                    //data.getData return the content URI for the selected Image
                    imagen2 = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    // Get the cursor
                    cursor = getContentResolver().query(imagen2, filePathColumns, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    //Get the column index of MediaStore.Images.Media.DATA
                    columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                    //Gets the String value in the column
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    // Set the Image in ImageView after decoding the String
                    imgP.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                    Toast.makeText(getApplicationContext(), "Imagen Cargada correctamente", Toast.LENGTH_SHORT).show();
                    break;

                case CAMARA1:
                    imgR.setImageURI(Uri.parse(cameraFilePath));
                    Toast.makeText(getApplicationContext(), "Imagen Cargada correctamente", Toast.LENGTH_SHORT).show();

                    break;
                case CAMARA2:
                    imgP.setImageURI(Uri.parse(cameraFilePath));
                    Toast.makeText(getApplicationContext(), "Imagen Cargada correctamente", Toast.LENGTH_SHORT).show();
                    break;
            }
        }else {
            Toast.makeText(getApplicationContext(),"Ha ocurrido un error: Camara", Toast.LENGTH_SHORT).show();
        }
    }//end
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }
    private void captureFromCamera1() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, CAMARA1);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void captureFromCamera2() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, CAMARA2);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void pickFromGallery1(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALERIA_IMAGENES);
    }
    private void pickFromGallery2(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALERIA);
    }
}// fin de la class
