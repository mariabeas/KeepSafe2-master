package com.app.mariabeas.keepsafe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Maria on 01/02/2016.
 */
public class MenuActivity extends AppCompatActivity {

    public static SQLiteDatabase db;

    ImageView logo;
    Context context=this;

    //////PRUEBAAAAA
    public String email,nombre,apellido,fecha,sexo,sangre,num;
    DatabaseHelper helper=new DatabaseHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        ////////
        email=getIntent().getStringExtra("emailUsuario");
        nombre=helper.obtenerNombre(email);
        apellido=helper.obtenerApellido(email);
        fecha=helper.obtenerFecha(email);
        sexo=helper.obtenerSexo(email);
        sangre=helper.obtenerSangre(email);
        num=helper.obtenerNum(email);
        /////////////////
        /*nombre=getIntent().getStringExtra("nombreUsuario");
        apellido=getIntent().getStringExtra("apellidoUsuario");
        fecha=getIntent().getStringExtra("fechaUsuario");
        sexo=getIntent().getStringExtra("sexoUsuario");
        sangre=getIntent().getStringExtra("sangreUsuario");
        num=getIntent().getStringExtra("numUsuario");*/



        //Declaramos el toolbar del menu datos
        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_datos);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("");

        //ELEMENTOS DE LA INTERFAZ

        logo=(ImageView)findViewById(R.id.logo);
        Button btnDatos=(Button)findViewById(R.id.btnDatos);
        Button btnAgenda=(Button)findViewById(R.id.btnAgenda);
        Button btnSMS=(Button)findViewById(R.id.btnSMS);

        Button btnInfo=(Button)findViewById(R.id.btnInfo);
        Button btnUbi=(Button)findViewById(R.id.btnUbi);

        //TEXTVIEW QUE ME MUESTRE EL ID DEL USUARIO QUE HA ENTRADO
        TextView tvId=(TextView)findViewById(R.id.tvId);


        MiListener listener=new MiListener();
        btnDatos.setOnClickListener(listener);

        btnInfo.setOnClickListener(listener);
        btnAgenda.setOnClickListener(listener);
        btnSMS.setOnClickListener(listener);

        btnUbi.setOnClickListener(listener);



        //UbicacionNoApi ubi=new UbicacionNoApi(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                cerrarSesion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void cerrarSesion(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // Titulo del AlertDialog
        alertDialogBuilder.setTitle("¿Seguro que desea cerrar sesión?");

        alertDialogBuilder
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentLogin =new Intent(MenuActivity.this,MainActivity.class);
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




    private class MiListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.btnDatos) {

                //PARA PASAR DE UNA PANTALLA A OTRA
                Intent intentDatos = new Intent(MenuActivity.this, DatosGuardadosActivity.class);

                /////////////////
                intentDatos.putExtra("emailUsuario",email);

                intentDatos.putExtra("nombreUsuario",nombre);
                intentDatos.putExtra("apellidoUsuario",apellido);
                intentDatos.putExtra("fechaUsuario",fecha);
                intentDatos.putExtra("sexoUsuario",sexo);
                intentDatos.putExtra("sangreUsuario",sangre);
                intentDatos.putExtra("numUsuario",num);
                //////////////

                startActivity(intentDatos);



            }else if(v.getId()==R.id.btnInfo){
                Intent intentInfo=new Intent(MenuActivity.this,InformacionActivity.class);
                startActivity(intentInfo);
            }else if(v.getId()==R.id.btnAgenda){
                //Intent intentAgenda=new Intent(MenuActivity.this,AgendaActivity.class);
                Intent intentAgenda=new Intent(MenuActivity.this,AgendaBotonesActivity.class);
                startActivity(intentAgenda);
            }else if(v.getId()==R.id.btnSMS){
                Intent intentSMS=new Intent(MenuActivity.this,EnviarSMSActivity.class);
                startActivity(intentSMS);
            }
            else if(v.getId()==R.id.btnUbi){
                Intent intentUbi=new Intent(MenuActivity.this,UbiActivity.class);
                startActivity(intentUbi);
            }



        }
    }


}
