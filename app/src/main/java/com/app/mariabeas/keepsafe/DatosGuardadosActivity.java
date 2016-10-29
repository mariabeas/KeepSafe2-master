package com.app.mariabeas.keepsafe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MariaBeas on 16/2/16.
 */
public class DatosGuardadosActivity extends AppCompatActivity {

    TextView tvNombre;
    TextView tvEmail;
    TextView tvApellido;
    TextView tvFecha;
    TextView tvSexo;
    TextView tvSangre;
    TextView tvNum;


    ImageView image;
    TextView tvUsuario;
    Context context=this;

    //PARA PODER USAR LOS METODOS CREADOS EN DATABASEHELPER
    DatabaseHelper helper=new DatabaseHelper(this);

    LoginDataBaseAdapter loginDBAdapter;

    //PARA MOSTRAR LOS DATOS DEL WEBSERVICE
    private int posicion=0;
    private List listaUsuarios;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_guardados);


        //Crear una instancia de SQLiteDataBase
        loginDBAdapter=new LoginDataBaseAdapter(this);
        loginDBAdapter=loginDBAdapter.open();

        tvUsuario=(TextView)findViewById(R.id.edtUsuario);

        tvNombre=(TextView)findViewById(R.id.edtNombreAgenda);
        tvNombre.setText(getIntent().getStringExtra("nombreUsuario"));

        tvEmail=(TextView)findViewById(R.id.edtUser);
        tvEmail.setText(getIntent().getStringExtra("emailUsuario"));

        tvApellido=(TextView)findViewById(R.id.edtApellido);
        tvApellido.setText(getIntent().getStringExtra("apellidoUsuario"));
        tvFecha=(TextView)findViewById(R.id.edtFecha);
        tvFecha.setText(getIntent().getStringExtra("fechaUsuario"));
        tvSexo=(TextView)findViewById(R.id.edtSexo);
        tvSexo.setText(getIntent().getStringExtra("sexoUsuario"));
        tvSangre=(TextView)findViewById(R.id.edtSangre);
        tvSangre.setText(getIntent().getStringExtra("sangreUsuario"));
        tvNum=(TextView)findViewById(R.id.edtNum);
        tvNum.setText(getIntent().getStringExtra("numUsuario"));
        image=(ImageView)findViewById(R.id.imageView);
        int image_link=getIntent().getIntExtra("image", R.drawable.avatar);
        image.setImageResource(image_link);

        //WEB SERVICE
        listaUsuarios=new ArrayList();

        //PARA EJECUTAR EL MOSTRAR USUARIOS (HAY QUE MODIFICARLO PARA QUE SOLO MUESTRE 1 USUARIO NO TODOS
        //new Mostrar().execute();

        //Declaramos el toolbar del menu datos guardados
        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_datos);
        setSupportActionBar(toolbar);
        //para poner el boton de volver al menu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Modificar datos");

        //RECUPERAMOS LOS REGISTROS INSERTADOS
        LoginDataBaseAdapter MDB = new LoginDataBaseAdapter(getApplicationContext());


        Log.d("TOTAL", Integer.toString(MDB.recuperarUsuario().size()));
        int[] ids = new int[MDB.recuperarUsuario().size()];
        String[] noms = new String[MDB.recuperarUsuario().size()];
        String [] apellidos = new String[MDB.recuperarUsuario().size()];
        String[] pass = new String[MDB.recuperarUsuario().size()];
        String[] fechas = new String[MDB.recuperarUsuario().size()];
        String[] sexos = new String[MDB.recuperarUsuario().size()];
        String[] sangres = new String[MDB.recuperarUsuario().size()];
        String[] numeros = new String[MDB.recuperarUsuario().size()];
        String[] emls = new String[MDB.recuperarUsuario().size()];



        for (int i = 0; i < MDB.recuperarUsuario().size(); i++) {

            ids[i] = Integer.parseInt(MDB.recuperarUsuario().get(i).getIdUsuario());
            emls[i] = MDB.recuperarUsuario().get(i).getEmailUsuario();
            pass[i] = MDB.recuperarUsuario().get(i).getPasswordUsuario();
            noms[i] = MDB.recuperarUsuario().get(i).getNombreUsuario();
            apellidos[i] = MDB.recuperarUsuario().get(i).getApellidosUsuario();
            fechas[i] = MDB.recuperarUsuario().get(i).getFechaNac();
            sexos[i] = MDB.recuperarUsuario().get(i).getSexo();
            sangres[i] = MDB.recuperarUsuario().get(i).getGrupoSanguineo();
            numeros[i] = MDB.recuperarUsuario().get(i).getNumSeguridadSocial();
            Log.e("Usuario con id: " + ids[i], "email; " + emls[i]);
            Log.e("nombre; " + noms[i], "apellido: " + apellidos[i]);
            Log.e("contraseña: " + pass[i], "fecha nacimiento; " + fechas[i]);
            Log.e("sexo: " + sexos[i], "grupo sanguineo" + sangres[i]);

        }


       /* String emailUsuario=getIntent().getStringExtra("email");
        tvUsuario.setText(emailUsuario);
        String nombreUsuario=getIntent().getStringExtra("nombre");
        tvNombre.setText(nombreUsuario);

        String apellidoUsuario=getIntent().getStringExtra("apellido");
        tvApellido.setText(apellidoUsuario);

        String fechaNacUsuario=getIntent().getStringExtra("fechaNac");
        tvFecha.setText(fechaNacUsuario);

        String sexoUsuario=getIntent().getStringExtra("sexo");
        tvSexo.setText(sexoUsuario);

        String sangreUsuario=getIntent().getStringExtra("sangre");
        tvSangre.setText(sangreUsuario);

        String numSegSocialUsuario=getIntent().getStringExtra("numSegSocial");
        tvNum.setText(numSegSocialUsuario);*/

        //RECUPERAMOS EL REGISTRO CON EL ID

        for(int i=0;i<MDB.recuperarUsuario().size();i++) {
            int id=Integer.parseInt(MDB.recuperarUsuario().get(i).getIdUsuario());
            String nom = MDB.recuperarUsuario(id).getNombreUsuario();
            String apellido = MDB.recuperarUsuario(id).getApellidosUsuario();
            String password = MDB.recuperarUsuario(id).getPasswordUsuario();
            String fecha = MDB.recuperarUsuario(id).getFechaNac();
            String sexo = MDB.recuperarUsuario(id).getSexo();
            String sangre = MDB.recuperarUsuario(id).getGrupoSanguineo();
            String numero = MDB.recuperarUsuario(id).getNumSeguridadSocial();
            String eml = MDB.recuperarUsuario(id).getEmailUsuario();
            tvEmail.setText(eml.toString());
            tvNombre.setText(nom.toString());
            tvApellido.setText(apellido.toString());
            tvFecha.setText(fecha.toString());
            tvSexo.setText(sexo.toString());
            tvSangre.setText(sangre.toString());
            tvNum.setText(numero.toString());
        }



    }
    //METODO PARA OBTENER LOS DATOS DEL SERVIDOR EN FORMA DE STRING
    private String mostrar(){
        String resquest="";
        HttpClient httpClient=new DefaultHttpClient();
        HttpPost httpPost=new HttpPost("http://192.168.1.41:8888/hmis2015/selectAll.php");
        try{
            //EJECUTAMOS Y OBTENEMOS RESPUESTA DEL SERVIDOR
            ResponseHandler<String> responseHandler=new BasicResponseHandler();
            resquest=httpClient.execute(httpPost,responseHandler);
        }catch (UnsupportedEncodingException e){
        e.printStackTrace();
        } catch (ClientProtocolException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
        return resquest;
    }

    //METODO QUE DESCOMPONE, CREA UN OBJETO CON LOS DATOS DESCOMPUESTOS (COGIDOS DEL SERVIDOR) Y LO ALMACENA
    //EN UN ARRAYLIST
    private boolean filtrarDatos(){
        listaUsuarios.clear();
        if(!mostrar().equalsIgnoreCase("")){
            String [] cargarDatos=mostrar().split("/");
            for(int i=0;i<cargarDatos.length;i++){
                String datosUsuario []=cargarDatos[i].split("<br>");
                UsuariosApp usuariosApp=new UsuariosApp();
                usuariosApp.setEmail(datosUsuario[0]);
                usuariosApp.setNombre(datosUsuario[2]);
                usuariosApp.setApellido(datosUsuario[3]);
                usuariosApp.setFechaNac(datosUsuario[4]);
                usuariosApp.setSexo(datosUsuario[5]);
                usuariosApp.setSangre(datosUsuario[6]);
                usuariosApp.setNumSegSocial(datosUsuario[7]);
                //ID DEL USUARIO
                usuariosApp.setId(datosUsuario[8]);
            }
            return true;
        }
        return false;
    }

    //METODO QUE MUESTRA LOS DATOS ALMACENADOS EN EL ARRAYLIST
    //MUESTRA EL USUARIO ALMACENADO COMO OBJETO EN EL ARRAYLIST
    private void mostrarUsuario(final int posicion){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UsuariosApp usuariosApp= (UsuariosApp) listaUsuarios.get(posicion);
                tvEmail.setText(usuariosApp.getEmail());
                tvNombre.setText(usuariosApp.getNombre());
                tvApellido.setText(usuariosApp.getApellido());
                tvFecha.setText(usuariosApp.getFechaNac());
                tvSexo.setText(usuariosApp.getSexo());
                tvSangre.setText(usuariosApp.getSangre());
                tvNum.setText(usuariosApp.getNumSegSocial());

            }
        });
    }

    //CLASE PRIVADA PARA MOSTRAR PERSONAS
    class Mostrar extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            if(filtrarDatos())
                mostrarUsuario(posicion);
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_datos_guardados, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_editar:
                editarDatos();
                return true;
            case R.id.action_logout:
                cerrarSesion();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void cerrarSesion(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // Titulo del AlertDialog
        alertDialogBuilder.setTitle("¿Seguro que desea cerrar sesión?");

        alertDialogBuilder
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentLogin =new Intent(DatosGuardadosActivity.this,MainActivity.class);
                        startActivity(intentLogin);
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public void editarDatos(){
        Intent intentEditar=new Intent(DatosGuardadosActivity.this,DatosActivity.class);
       /* intentEditar.putExtra("email",tvEmail.getText());
        intentEditar.putExtra("nombre",tvUsuario.getText());
        intentEditar.putExtra("apellido",tvApellido.getText());
        intentEditar.putExtra("fechaNac",tvFecha.getText());
        intentEditar.putExtra("sexo",tvSexo.getText());
        intentEditar.putExtra("sangre",tvSangre.getText());
        intentEditar.putExtra("numSegSocial",tvNum.getText());*/
        startActivity(intentEditar);
    }




}
