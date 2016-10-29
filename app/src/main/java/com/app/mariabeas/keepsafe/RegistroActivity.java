package com.app.mariabeas.keepsafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maria on 02/02/2016.
 */
public class RegistroActivity extends AppCompatActivity {
    ImageView logo;
    ImageView avatar;
    EditText edtUsuario;
    EditText edtPass;
    EditText edtConfiPass;
    EditText edtNombre;
    EditText edtApellido;
    EditText edtFecha;
    EditText edtSexo;
    EditText edtSangre;
    EditText edtNum;
    final int CAMERA_REQUEST;
    final int SELECT_FILE;
    Context context=this;


    LoginDataBaseAdapter loginDBAdapter=new LoginDataBaseAdapter(context);


    public RegistroActivity() {
        SELECT_FILE = 1;
        CAMERA_REQUEST = 1888;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        //Crear una instancia de SQLiteDataBase
        loginDBAdapter=new LoginDataBaseAdapter(this);
        loginDBAdapter=loginDBAdapter.open();
        //ELEMENTOS DE LA INTERFAZ
        logo=(ImageView)findViewById(R.id.logo);
        avatar=(ImageView)findViewById(R.id.avatarGuardado);
        edtUsuario=(EditText)findViewById(R.id.edtUsuario);
        edtPass=(EditText)findViewById(R.id.edtPass);
        edtConfiPass=(EditText)findViewById(R.id.edtConfiPass);
        Button btnAceptar=(Button)findViewById(R.id.btnAceptar);

        edtNombre=(EditText)findViewById(R.id.edtNombreAgenda);
        edtApellido=(EditText)findViewById(R.id.edtApellido);
        edtFecha=(EditText)findViewById(R.id.edtFecha);
        edtSexo=(EditText)findViewById(R.id.edtSexo);
        edtSangre=(EditText)findViewById(R.id.edtSangre);
        edtNum=(EditText)findViewById(R.id.edtNum);

        //SPINNERS PARA LOS TIPOS DE SANGRE DEL USUARIO
        Spinner spinnerSangre=(Spinner)findViewById(R.id.spinnerSangre);
        ArrayList tiposSangre=new ArrayList();
        tiposSangre.add("A+");
        tiposSangre.add("A-");
        tiposSangre.add("B+");
        tiposSangre.add("B-");
        tiposSangre.add("AB+");
        tiposSangre.add("AB-");
        tiposSangre.add("O+");
        tiposSangre.add("0-");
        spinnerSangre.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,tiposSangre));

        //SPINNERS PARA EL SEXO DEL USUARIO
        Spinner spinnerSexo=(Spinner)findViewById(R.id.spinnerSexo);
        ArrayList tiposSexo=new ArrayList();
        tiposSexo.add("Hombre");
        tiposSexo.add("Mujer");
        spinnerSexo.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,tiposSexo));

        MiListener listener=new MiListener();
        btnAceptar.setOnClickListener(listener);
        FloatingActionButton btnFoto = (FloatingActionButton) findViewById(R.id.btnFoto);
        btnFoto.setOnClickListener(listener);
    }

    //METODO PARA ENVIAR LA INFO DEL REGISTRO AL WEB SERVICE
    private boolean insertarUsuario(){
        HttpClient httpClient=new DefaultHttpClient();
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost=new HttpPost("http://172.20.56.250:8888/hmis2015/insertUsuario.php");
        nameValuePairs=new ArrayList<>(8);
        //añadimos nuestros datos
        nameValuePairs.add(new BasicNameValuePair("email",edtUsuario.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("password",edtPass.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("nombre",edtNombre.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("apellido",edtApellido.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("fechaNac",edtFecha.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("sexo",edtSexo.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("sangre",edtSangre.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("numSegSocial",edtNum.getText().toString().trim()));
        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpClient.execute(httpPost);
            return true;

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //CLASE INTERNA QUE HEREDE DE ASYNCTASK
    class InsertarUsuario extends AsyncTask<String,String,String> {
        private Activity context;
        InsertarUsuario (Activity context){
            this.context=context;
        }

        @Override
        protected String doInBackground(String... params) {
            if(insertarUsuario())
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"Usuario registrado con éxito",Toast.LENGTH_LONG).show();
                        //PARA LIMPIAR EL TEXTO DE LA PANTALLA NO ES NECESARIO
                        // edtContacto.setText("");
                        //edtSMS.setText("");
                        //tvUbi.setText("");
                        //tvDireccion.setText("");
                    }
                });
            else
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"Error al registrar al usuario",Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }

    //clase privada para implementar la funcionalidad de los botones de la activity
    private class MiListener implements View.OnClickListener{

        public void onClick(View v) {
            String usuario=edtUsuario.getText().toString();
            String pass=edtPass.getText().toString();
            String confiPass=edtConfiPass.getText().toString();
            ///////////////
            String nombre=edtNombre.getText().toString();
            String apellido=edtApellido.getText().toString();
            String fecha=edtFecha.getText().toString();
            String sexo=edtSexo.getText().toString();
            String sangre=edtSangre.getText().toString();
            String num=edtNum.getText().toString();
            /*String nombre=((EditText)findViewById(R.id.edtNombreAgenda)).getText().toString();
            String apellido=((EditText)findViewById(R.id.edtApellido)).getText().toString();
            String fecha=((EditText)findViewById(R.id.edtFecha)).getText().toString();
            String sexo=((EditText)findViewById(R.id.edtSexo)).getText().toString();
            String sangre=((EditText)findViewById(R.id.edtSangre)).getText().toString();
            String num=((EditText)findViewById(R.id.edtNum)).getText().toString();*/

            //String foto=((Button)findViewById(R.id.btnFoto)).getText().toString();

            ////SPINNERS! PONERLOS VISIBLES TAMBIEN EN REGISTRO.XML
            //String sangre=((Spinner)findViewById(R.id.spinnerSangre)).getSelectedItem().toString();
            //String sexo=((Spinner)findViewById(R.id.spinnerSexo)).getSelectedItem().toString();
            if (v.getId() == R.id.btnFoto) {
                selectImage();
            }
            if(v.getId()==R.id.btnAceptar) {
               if(usuario.isEmpty()||pass.isEmpty()||confiPass.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Compruebe que los campos no esten vacíos", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pass.equals(confiPass)){
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(edtNum.length()<12 || edtNum.length()>12){
                    Toast.makeText(getApplicationContext(),"El número de seguridad social debe tener 12 dígitos",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                    //INSERTAR USUARIO EN EL SERVIDOR A LA VEZ
                    new InsertarUsuario(RegistroActivity.this).execute();
                    loginDBAdapter.insertEntry(usuario,pass,nombre,apellido,fecha,sexo,sangre,num);
                    Toast.makeText(getApplicationContext(), "Registro completado", Toast.LENGTH_SHORT).show();
                    //PARA PASAR DE UNA PANTALLA A OTRA
                    //Intent intentactivity = new Intent(RegistroActivity.this, MainActivity.class);
                    //startActivity(intentactivity);

                    Intent i=new Intent(RegistroActivity.this,MainActivity.class);
                    //PASAMOS LOS DATOS AL MAIN
                   //i.putExtra("emailUsuario",edtUsuario.getText());
                    //i.putExtra("password", edtPass.getText());

                    /////////NUEVOS
                    i.putExtra("nombreUsuario",nombre);
                    i.putExtra("apellidoUsuario",apellido);
                    i.putExtra("fechaUsuario",fecha);
                    i.putExtra("sexoUsuario",sexo);
                    i.putExtra("sangreUsuario",sangre);
                    i.putExtra("numUsuario",num);
                    //////////////////////


                    startActivity(i);




                }
            }

        }

    }


    private void selectImage() {
        final CharSequence[] items = { "Hacer foto", "Acceder al carrete", "Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
        builder.setTitle("Añadir foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Hacer foto")) {
                    //final int CAMERA_REQUEST = 1888;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,CAMERA_REQUEST);
                } else if (items[item].equals("Acceder al carrete")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    //REVISAR!!!
                    //final int SELECT_FILE=1888;
                    startActivityForResult(Intent.createChooser(intent, "Seleccionar foto"),SELECT_FILE);
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // final int CAMERA_REQUEST = 1888;
            if (requestCode == CAMERA_REQUEST) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                avatar.setImageBitmap(thumbnail);
                // final int SELECT_FILE=1888;
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                avatar.setImageBitmap(bm);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //CERRAR LA DB
        loginDBAdapter.close();
    }
}
