package com.app.mariabeas.keepsafe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by MariaBeas on 20/9/16.
 */
public class AgendaBotonesActivity extends AppCompatActivity {


    ImageView logo;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_botones);

        //Declaramos el toolbar del menu datos
        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_datos);
        setSupportActionBar(toolbar);
        //para poner el boton de volver al menu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //ELEMENTOS DE LA INTERFAZ

        logo = (ImageView) findViewById(R.id.logo);
        Button btnBomberos = (Button) findViewById(R.id.btnBomberos);
        Button btnEmergencias = (Button) findViewById(R.id.btnEmergencias);
        Button btnPolicia = (Button) findViewById(R.id.btnPolicia);
        Button btnAmbulancia = (Button) findViewById(R.id.btnAmbulancia);

        ListenerAmbulancia listenerAmbulancia = new ListenerAmbulancia();
        btnAmbulancia.setOnClickListener(listenerAmbulancia);

        MiListener listener = new MiListener();
        btnBomberos.setOnClickListener(listener);

        Listener112 listener112 = new Listener112();
        btnEmergencias.setOnClickListener(listener112);

        ListenerPolicia listenerPolicia = new ListenerPolicia();
        btnPolicia.setOnClickListener(listenerPolicia);



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

    public void cerrarSesion() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // Titulo del AlertDialog
        alertDialogBuilder.setTitle("¿Seguro que desea cerrar sesión?");

        alertDialogBuilder
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentLogin = new Intent(AgendaBotonesActivity.this, MainActivity.class);
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

    private class ListenerPolicia implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnPolicia) {
                String numPolicia = "091";
                Intent intentPolicia = new Intent(AgendaBotonesActivity.this, EnviarSMSActivity.class);
                intentPolicia.putExtra("telefonoPolicia", numPolicia + "");
                startActivity(intentPolicia);

            }
        }
    }

    private class Listener112 implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnEmergencias) {
                String numEmergencias = "112";
                Intent intentEmergencias = new Intent(AgendaBotonesActivity.this, EnviarSMSActivity.class);
                intentEmergencias.putExtra("telefonoEmergencias", numEmergencias + "");
                startActivity(intentEmergencias);
            }
        }
    }

        private class ListenerAmbulancia implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnAmbulancia) {
                    String numAmbulancia = "061";
                    Intent intentAmbulancia = new Intent(AgendaBotonesActivity.this, EnviarSMSActivity.class);
                    intentAmbulancia.putExtra("telefonoAmbulancia", numAmbulancia + "");
                    startActivity(intentAmbulancia);
                }
            }
        }

            private class MiListener implements View.OnClickListener {

                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.btnBomberos) {
                        Intent intentBomberos = new Intent(AgendaBotonesActivity.this, EnviarSMSActivity.class);
                        String numBomberos = "080";
                        intentBomberos.putExtra("telefonoBombero", numBomberos + "");
                        startActivity(intentBomberos);
                    }
                }
            }
        }


