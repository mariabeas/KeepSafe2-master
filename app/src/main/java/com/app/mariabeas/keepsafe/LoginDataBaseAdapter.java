package com.app.mariabeas.keepsafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maria on 04/02/2016.
 */

public class LoginDataBaseAdapter {
    //DECLARAR LAS VARIABLES

    static final String DATABASE_NAME="dbkeepsafe.db";
    static final int DATABASE_VERSION=1;



    //Clase privada para establecer los atributos de la tabla de la BD implicada EN ESTE CASO LA TABLA USUARIO
    private class usuariosDBInfo{
        private static final String TABLE_NAME="Usuario";
        private static final String IDUSUARIO_COLUMN="idUsuario";
        private static final String EMAIL_COLUMN="emailUsuario";
        private static final String PASSWORD_COLUMN="passwordUsuario";
        private static final String NOMBRE_COLUMN="nombreUsuario";
        private static final String APELLIDOS_COLUMN="apellidosUsuario";
        private static final String FECHANAC_COLUMN="fechaNac";
        private static final String SEXO_COLUMN="sexo";
        private static final String GRUPOSANGUINEO_COLUMN="grupoSanguineo";
        private static final String NUMSEGURIDADSOCIAL_COLUMN="numSeguridadSocial";
        private static final String FOTO_COLUMN="foto";
    }

    //Clase privada para establecer los atributos de la tabla de AGENDA
    private class agendaDBInfo{
        private static final String TABLE_NAME="Agenda";
        private static final String IDAGENDA_COLUMN="idAgenda";
        private static final String NOMBREAGENDA_COLUMN="nombreAgenda";
        private static final String TELEFONOAGENDA_COLUMN="telefonoAgenda";
    }

    //Clase privada para establecer los atributos de la tabla Mensajes
    private class mensajesDBInfo{
        private static final String TABLE_NAME="Mensajes";
        private static final String IDMENSAJE_COLUMN="idMensaje";
        private static final String MENSAJE_COLUMN="mensaje";
    }

    //Clase privada para establecer los atributos de la tabla Ubicacion
    private class ubicacionDBInfo{
        private static final String TABLE_NAME="Ubicacion";
        private static final String IDUBICACION_COLUMN="idUbicacion";
        private static final String LATITUD_COLUMN="latitud";
        private static final String ALTITUD_COLUMN="altitud";
        private static final String NOMBREUBICACION_COLUMN="nombreUbicacion";
    }

    //public static final int NAME_COLUMN=1;

    //CREAR LA TABLA DE LA DB USUARIO
    public static final String DATABASE_CREATE = "create table "+ usuariosDBInfo.TABLE_NAME +
            " ( "
            + usuariosDBInfo.IDUSUARIO_COLUMN+" integer primary key autoincrement, "
            + usuariosDBInfo.EMAIL_COLUMN+" TEXT NOT NULL, "
            + usuariosDBInfo.PASSWORD_COLUMN+" TEXT NOT NULL, "
            + usuariosDBInfo.NOMBRE_COLUMN+" TEXT, "
            + usuariosDBInfo.APELLIDOS_COLUMN+" TEXT, "
            + usuariosDBInfo.FECHANAC_COLUMN+" TEXT, "
            + usuariosDBInfo.SEXO_COLUMN+" TEXT, "
            + usuariosDBInfo.GRUPOSANGUINEO_COLUMN+" TEXT, "
            +usuariosDBInfo.FOTO_COLUMN+" TEXT, "
            + usuariosDBInfo.NUMSEGURIDADSOCIAL_COLUMN+" TEXT); ";


    //crear la tabla de la db Agenda
    public static final String AGENDA_CREATE="create table "+ agendaDBInfo.TABLE_NAME + " ( "
            +agendaDBInfo.IDAGENDA_COLUMN + " integer primary key autoincrement, "
            +agendaDBInfo.NOMBREAGENDA_COLUMN+ " TEXT NOT NULL, "
            +agendaDBInfo.TELEFONOAGENDA_COLUMN+ " INTEGER NOT NULL, "
            +"FOREIGN KEY ("+agendaDBInfo.IDAGENDA_COLUMN +") REFERENCES " +usuariosDBInfo.TABLE_NAME + "(" + usuariosDBInfo.IDUSUARIO_COLUMN +") "
            +"FOREIGN KEY ("+agendaDBInfo.IDAGENDA_COLUMN +") REFERENCES " +mensajesDBInfo.TABLE_NAME + "(" + mensajesDBInfo.IDMENSAJE_COLUMN+ "));";

    //crear la tabla de la db Mensaje
    public static final String MENSAJES_CREATE="create table "+mensajesDBInfo.TABLE_NAME + " ( "
            +mensajesDBInfo.IDMENSAJE_COLUMN + " integer primary key autoincrement, "
            +mensajesDBInfo.MENSAJE_COLUMN+ " TEXT);";

    //crear la tabla de la db Ubicacion
    public static final String UBICACION_CREATE="CREATE TABLE "+ubicacionDBInfo.TABLE_NAME + " ( "
            +ubicacionDBInfo.IDUBICACION_COLUMN +" integer primary key autoincrement, "
            +ubicacionDBInfo.ALTITUD_COLUMN+ " TEXT NOT NULL, "
            +ubicacionDBInfo.LATITUD_COLUMN+" TEXT NOT NULL, "
            +ubicacionDBInfo.NOMBREUBICACION_COLUMN+ " TEXT, "
            +"FOREIGN KEY ("+ubicacionDBInfo.IDUBICACION_COLUMN + ") REFERENCES "+mensajesDBInfo.TABLE_NAME + "(" + mensajesDBInfo.IDMENSAJE_COLUMN +"));";

    //DECLARAR LA VARIABLE PARA INSTANCIAR LA DB
    public static SQLiteDatabase db;
    //DECLARAR LA VARIABLE PARA ABRIR Y ACTUALIZAR EL HELPER
    private DBHelper dbHelper;

    public LoginDataBaseAdapter(Context context) {
        dbHelper = new DBHelper(context, DATABASE_NAME,DATABASE_VERSION);
    }

   //METODO PARA ABRIR LA DB Y ESCRIBIR EN ELLA
    public LoginDataBaseAdapter open(){
        db=dbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        db.close();
    }


    public SQLiteDatabase getDatabaseInstance(){
        return db;
    }


    // Método para obtener la dupla email/contraseña para validar al usuario que ha iniciado sesión
    public String getSingleEntry(String user) {
        Cursor cursor=db.query(usuariosDBInfo.TABLE_NAME, null, usuariosDBInfo.EMAIL_COLUMN + "=?", new String[]{user}, null, null, null);
        if(cursor.getCount()<1){ //EL USUARIO NO EXISTE
            cursor.close();
            return "El email introducido no existe en la base de datos";
        }
        cursor.moveToFirst();
        String password=cursor.getString(cursor.getColumnIndex(usuariosDBInfo.PASSWORD_COLUMN));
        cursor.close();
        return password;
    }


    ////comprueba si el email y la contraseña de un paciente coinciden, es decir, si está en nuestra BD
    public String comprobarPassword(String email){
        db=this.getDatabaseInstance();
        String emailUsuario,password="no existe";
        try{
            db.beginTransaction();
            String query="SELECT "+usuariosDBInfo.EMAIL_COLUMN+usuariosDBInfo.PASSWORD_COLUMN+" FROM "+usuariosDBInfo.TABLE_NAME;
            Cursor cursor=db.rawQuery(query,null);
            if(cursor.moveToFirst()){
                do{
                    emailUsuario=cursor.getString(0);
                    if(emailUsuario.equals(email)){
                        password=cursor.getString(1);
                        break;
                    }
                }while (cursor.moveToNext());
            }
            db.setTransactionSuccessful();
        }catch (Exception e){

        }finally {
            db.endTransaction();
            db.close();
        }
        return password;
    }



    //METODO PARA OBTENER LOS DATOS PERSONALES DEL USUARIO
    public String getSingleEntryDatos(TextView id){

        String[]columnas=new String[]{usuariosDBInfo.IDUSUARIO_COLUMN,usuariosDBInfo.EMAIL_COLUMN,usuariosDBInfo.NOMBRE_COLUMN,
                usuariosDBInfo.APELLIDOS_COLUMN,usuariosDBInfo.FECHANAC_COLUMN,usuariosDBInfo.SEXO_COLUMN, usuariosDBInfo.GRUPOSANGUINEO_COLUMN,
                usuariosDBInfo.NUMSEGURIDADSOCIAL_COLUMN,usuariosDBInfo.FOTO_COLUMN};
        SQLiteDatabase db=this.getDatabaseInstance();
        Cursor cursor=db.query(usuariosDBInfo.TABLE_NAME, columnas, usuariosDBInfo.IDUSUARIO_COLUMN + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        return null;

    }

    //METODO PARA OBTENER LOS DATOS GUARDADOS AL REGISTRAR
    public static void getEntry(String mail, String pass,String nombre,String apellido, String fechaNac, String sexo, String sangre, String numSeguridad) {
        ContentValues values = new ContentValues();

    }

    //METODO PARA GUARDAR LOS VALORES QUE INSERTAMOS EN LA DB
    public static void insertEntry(String mail, String pass,String nombre,String apellido, String fechaNac, String sexo, String sangre, String numSeguridad) {
        ContentValues values=new ContentValues();
        // ASIGNAR VALORES PARA CADA FILA
        values.put(usuariosDBInfo.EMAIL_COLUMN, mail);
        values.put(usuariosDBInfo.PASSWORD_COLUMN, pass);
        values.put(usuariosDBInfo.NOMBRE_COLUMN,nombre);
        values.put(usuariosDBInfo.APELLIDOS_COLUMN,apellido);
        values.put(usuariosDBInfo.FECHANAC_COLUMN,fechaNac);
        values.put(usuariosDBInfo.SEXO_COLUMN,sexo);
        values.put(usuariosDBInfo.GRUPOSANGUINEO_COLUMN,sangre);
        values.put(usuariosDBInfo.NUMSEGURIDADSOCIAL_COLUMN,numSeguridad);

        //INSERTAR LA FILA EN LA TABLE
        db.insert(usuariosDBInfo.TABLE_NAME, null, values);
        db.close();
    }

    /*public void insertarUsuario(Usuario u){
        db=this.getDatabaseInstance();
        try{
            db.beginTransaction();
        ContentValues values=new ContentValues();
            // ASIGNAR VALORES PARA CADA FILA
            values.put(usuariosDBInfo.EMAIL_COLUMN, u.getEmailUsuario());
            values.put(usuariosDBInfo.PASSWORD_COLUMN, u.getPasswordUsuario());
            values.put(usuariosDBInfo.NOMBRE_COLUMN,u.getNombreUsuario());
            values.put(usuariosDBInfo.APELLIDOS_COLUMN,u.getApellidosUsuario());
            values.put(usuariosDBInfo.FECHANAC_COLUMN,u.getFechaNac());
            values.put(usuariosDBInfo.SEXO_COLUMN,u.getSexo());
            values.put(usuariosDBInfo.GRUPOSANGUINEO_COLUMN,u.getGrupoSanguineo());
            values.put(usuariosDBInfo.NUMSEGURIDADSOCIAL_COLUMN,u.getNumSeguridadSocial());
            db.insert(usuariosDBInfo.TABLE_NAME, null, values);
            db.setTransactionSuccessful();

        }catch (Exception e){

        }finally {
            db.endTransaction();
            db.close();
        }
    }*/

    //PRUEBA
    public static void insertDatos(String mail,String nombre, String apellido, String fechaNac,
                                   String sexo, String sangre, String numSeguridad) {

            // Definiar las filas actualizadas
            ContentValues values = new ContentValues();
            // Asignar valores para cada fila
            values.put(usuariosDBInfo.EMAIL_COLUMN, mail);
            values.put(usuariosDBInfo.NOMBRE_COLUMN, nombre);
            values.put(usuariosDBInfo.APELLIDOS_COLUMN, apellido);
            values.put(usuariosDBInfo.FECHANAC_COLUMN, fechaNac);
            values.put(usuariosDBInfo.SEXO_COLUMN, sexo);
            values.put(usuariosDBInfo.GRUPOSANGUINEO_COLUMN, sangre);
            values.put(usuariosDBInfo.NUMSEGURIDADSOCIAL_COLUMN, numSeguridad);

            String where=usuariosDBInfo.EMAIL_COLUMN+" = ?";
        //INSERTAR LA FILA EN LA TABLE
        db.insert(usuariosDBInfo.TABLE_NAME, null, values);

    }


    //METODO  PARA BORRAR ENTRADAS DE LA DB
    public int deleteEntry(String idUsuario){
        String where=usuariosDBInfo.EMAIL_COLUMN+"=?";
        int numEntryDelete= db.delete(usuariosDBInfo.TABLE_NAME, where, new String[]{idUsuario}) ;
        return numEntryDelete;


    }

    //METODO PARA ACTUALIZAR LAS ENTRADAS
    public static void  updateEntry(String mail,String nombre, String apellido, String fechaNac, String sexo, String sangre, String numSeguridad)
    {
        // Definiar las filas actualizadas
        ContentValues updatedValues = new ContentValues();
        // Asignar valores para cada fila
        updatedValues.put(usuariosDBInfo.EMAIL_COLUMN, mail);
        updatedValues.put(usuariosDBInfo.NOMBRE_COLUMN,nombre);
        updatedValues.put(usuariosDBInfo.APELLIDOS_COLUMN,apellido);
        updatedValues.put(usuariosDBInfo.FECHANAC_COLUMN,fechaNac);
        updatedValues.put(usuariosDBInfo.SEXO_COLUMN,sexo);
        updatedValues.put(usuariosDBInfo.GRUPOSANGUINEO_COLUMN,sangre);
        updatedValues.put(usuariosDBInfo.NUMSEGURIDADSOCIAL_COLUMN, numSeguridad);

        String where=usuariosDBInfo.EMAIL_COLUMN+" = ?";
        //INSERTAR LA FILA EN LA TABLE
        //db.insert(usuariosDBInfo.TABLE_NAME, null, updatedValues);
        db.update(usuariosDBInfo.TABLE_NAME, updatedValues, where, new String[]{mail});
        db.close();

    }


    AdapterUsuario getUsuario(int id){
        String[]columnas=new String[]{usuariosDBInfo.IDUSUARIO_COLUMN,usuariosDBInfo.EMAIL_COLUMN,usuariosDBInfo.NOMBRE_COLUMN,
                usuariosDBInfo.APELLIDOS_COLUMN,usuariosDBInfo.FECHANAC_COLUMN,usuariosDBInfo.SEXO_COLUMN, usuariosDBInfo.GRUPOSANGUINEO_COLUMN,
                usuariosDBInfo.NUMSEGURIDADSOCIAL_COLUMN,usuariosDBInfo.FOTO_COLUMN};
        SQLiteDatabase db=this.getDatabaseInstance();
        Cursor cursor=db.query(usuariosDBInfo.TABLE_NAME,columnas,usuariosDBInfo.IDUSUARIO_COLUMN+"=?",new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
       return null;

    }

    //COGER LOS DATOS DEL USUARIO!!!
    public Usuario recuperarUsuario(int id){
        SQLiteDatabase db=this.getDatabaseInstance();
        String[] valores_recuperar = {usuariosDBInfo.IDUSUARIO_COLUMN,usuariosDBInfo.EMAIL_COLUMN,usuariosDBInfo.NOMBRE_COLUMN,
                usuariosDBInfo.APELLIDOS_COLUMN,usuariosDBInfo.FECHANAC_COLUMN,usuariosDBInfo.SEXO_COLUMN, usuariosDBInfo.GRUPOSANGUINEO_COLUMN,
                usuariosDBInfo.NUMSEGURIDADSOCIAL_COLUMN,null};
        Cursor cursor=db.query(usuariosDBInfo.TABLE_NAME,valores_recuperar,usuariosDBInfo.IDUSUARIO_COLUMN+"=?",new String[]{String.valueOf(id)},null,null,null);

        if(cursor != null) {
            cursor.moveToFirst();
        }
       // Usuario usuario = new Usuario(cursor.getString(0), cursor.getString(1),
         //       cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7));

        cursor.close();
       // return usuario;
        return null;
    }

    public List<Usuario> recuperarUsuario() {
        SQLiteDatabase db = this.getDatabaseInstance();
        List<Usuario> lista_usuarios = new ArrayList<Usuario>();
        String[] valores_recuperar = {usuariosDBInfo.IDUSUARIO_COLUMN,usuariosDBInfo.EMAIL_COLUMN,usuariosDBInfo.NOMBRE_COLUMN,
                usuariosDBInfo.APELLIDOS_COLUMN,usuariosDBInfo.FECHANAC_COLUMN,usuariosDBInfo.SEXO_COLUMN, usuariosDBInfo.GRUPOSANGUINEO_COLUMN,
                usuariosDBInfo.NUMSEGURIDADSOCIAL_COLUMN,null};
        Cursor cursor = db.query(usuariosDBInfo.TABLE_NAME,valores_recuperar,usuariosDBInfo.IDUSUARIO_COLUMN,null,null,null,null);
        cursor.moveToFirst();
        do {
            //Usuario usuario = new Usuario(cursor.getString(0), cursor.getString(1),
              //      cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7));

            //lista_usuarios.add(usuario);
        } while (cursor.moveToNext());
        //db.close();
        cursor.close();
        return lista_usuarios;
    }

    //SEGUN EL EMAIL DEVUELVE EL NOMBRE DEL USUARIO
    public String obtenerNombreUsuario(String email){
        db=this.getDatabaseInstance();
        String nombre="no existe",emailB;
        try{
            db.beginTransaction();
            String query="SELECT "+usuariosDBInfo.NOMBRE_COLUMN+" FROM "+usuariosDBInfo.TABLE_NAME;
            Cursor cursor=db.rawQuery(query,null);
            if(cursor.moveToFirst()){
                do{
                    emailB=cursor.getString(0);
                    if(emailB.equals(email)){
                        nombre=cursor.getString(1);
                        break;
                    }
                }while (cursor.moveToNext());
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
            db.close();
        }
        return nombre;
    }





}
