package com.app.mariabeas.keepsafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by MariaBeas on 28/10/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //DECLARAR LAS VARIABLES

    static final String DATABASE_NAME="dbkeepsafe2.db";
    static final int DATABASE_VERSION=1;
    SQLiteDatabase db;




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


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        this.db=db;

    }

    //AÑADE UN USUARIO A LA BD
    public void insertarUsuario(Usuario u){
        db=this.getWritableDatabase();
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
    }

    //COMPROBAR SI EL EMAIL Y LA CONTRASEÑA DEL USUARIO COINCIDEN, ES DECIR, SI ESTA EN NUESTRA DB
    public String comprobarEP(String email){
        db=this.getReadableDatabase();
        String emailB,passwordB="no existe";
        try {
            db.beginTransaction();
            String query="SELECT "+usuariosDBInfo.EMAIL_COLUMN+" , "+usuariosDBInfo.PASSWORD_COLUMN+" FROM "+usuariosDBInfo.TABLE_NAME;
            Cursor cursor=db.rawQuery(query,null);
            if(cursor.moveToFirst()){
                do{
                    emailB=cursor.getString(0);
                    if(emailB.equals(email)){
                        passwordB=cursor.getString(1);
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
        return passwordB;
    }
    //SEGUN EL EMAIL DEVUELVE EL NOMBRE DEL USUARIO
    public String obtenerNombre(String email){
        db=this.getReadableDatabase();
        String nombre="prueba",emailB;
        try{
            db.beginTransaction();
            String query="SELECT "+usuariosDBInfo.EMAIL_COLUMN+" , "+usuariosDBInfo.NOMBRE_COLUMN+" FROM "+usuariosDBInfo.TABLE_NAME;
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
        }catch (Exception e){

        }finally {
            db.endTransaction();
            db.close();
        }
        return nombre;
    }


    //SEGUN EL EMAIL DEVUELVE LOS APELLIDOS DEL USUARIO
    public String obtenerApellido(String email){
        db=this.getReadableDatabase();
        String apellido="no existe",emailB;
        try{
            db.beginTransaction();
            String query="SELECT "+usuariosDBInfo.EMAIL_COLUMN+" , "+usuariosDBInfo.APELLIDOS_COLUMN+" FROM "+usuariosDBInfo.TABLE_NAME;
            Cursor cursor=db.rawQuery(query,null);
            if(cursor.moveToFirst()){
                do{
                    emailB=cursor.getString(0);
                    if(emailB.equals(email)){
                        apellido=cursor.getString(1);
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
        return apellido;
    }
    //SEGUN EL EMAIL DEVUELVE LA FECHA NACIMIENTO DEL USUARIO
    public String obtenerFecha(String email){
        db=this.getReadableDatabase();
        String fecha="no existe",emailB;
        try{
            db.beginTransaction();
            String query="SELECT "+usuariosDBInfo.EMAIL_COLUMN+" , "+usuariosDBInfo.FECHANAC_COLUMN+" FROM "+usuariosDBInfo.TABLE_NAME;
            Cursor cursor=db.rawQuery(query,null);
            if(cursor.moveToFirst()){
                do{
                    emailB=cursor.getString(0);
                    if(emailB.equals(email)){
                        fecha=cursor.getString(1);
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
        return fecha;
    }
    //SEGUN EL EMAIL DEVUELVE EL SEXO DEL USUARIO
    public String obtenerSexo(String email){
        db=this.getReadableDatabase();
        String sexo="no existe",emailB;
        try{
            db.beginTransaction();
            String query="SELECT "+usuariosDBInfo.EMAIL_COLUMN+" , "+usuariosDBInfo.SEXO_COLUMN+" FROM "+usuariosDBInfo.TABLE_NAME;
            Cursor cursor=db.rawQuery(query,null);
            if(cursor.moveToFirst()){
                do{
                    emailB=cursor.getString(0);
                    if(emailB.equals(email)){
                        sexo=cursor.getString(1);
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
        return sexo;
    }
    //SEGUN EL EMAIL DEVUELVE EL GRUPO SANGUINEO DEL USUARIO
    public String obtenerSangre(String email){
        db=this.getReadableDatabase();
        String sangre="no existe",emailB;
        try{
            db.beginTransaction();
            String query="SELECT "+usuariosDBInfo.EMAIL_COLUMN+" , "+usuariosDBInfo.GRUPOSANGUINEO_COLUMN+" FROM "+usuariosDBInfo.TABLE_NAME;
            Cursor cursor=db.rawQuery(query,null);
            if(cursor.moveToFirst()){
                do{
                    emailB=cursor.getString(0);
                    if(emailB.equals(email)){
                        sangre=cursor.getString(1);
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
        return sangre;
    }
    //SEGUN EL EMAIL DEVUELVE EL NUMERO DE SEGURIDAD SOCIAL DEL USUARIO
    public String obtenerNum(String email){
        db=this.getReadableDatabase();
        String num="no existe",emailB;
        try{
            db.beginTransaction();
            String query="SELECT "+usuariosDBInfo.EMAIL_COLUMN+" , "+usuariosDBInfo.NUMSEGURIDADSOCIAL_COLUMN+" FROM "+usuariosDBInfo.TABLE_NAME;
            Cursor cursor=db.rawQuery(query,null);
            if(cursor.moveToFirst()){
                do{
                    emailB=cursor.getString(0);
                    if(emailB.equals(email)){
                        num=cursor.getString(1);
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
        return num;
    }
    //DEVUELVE LOS EMAILS DE LOS USUARIOS QUE HAY EN LA BD
    public ArrayList<String> getUsuarios(){
        db=this.getReadableDatabase();
        ArrayList<String >usuarios=new ArrayList<String>();
        try{
            db.beginTransaction();
            String query="SELECT "+usuariosDBInfo.EMAIL_COLUMN+" FROM "+usuariosDBInfo.TABLE_NAME;
            Cursor cursor=db.rawQuery(query,null);
            while (cursor.moveToNext()){
                usuarios.add(cursor.getString(0));
            }
            db.setTransactionSuccessful();

        }catch (Exception e){

        }finally {
            db.endTransaction();
            db.close();
        }
        return usuarios;
    }

    //MODIFICAR LOS DATOS DEL USUARIO
    public void modificarUsuario(int id,String mail, String nombre, String apellido, String fechaNac, String sexo, String sangre, String numSeguridad) {
        db=getWritableDatabase();
        try{
         db.beginTransaction();
            ContentValues values=new ContentValues();
            // ASIGNAR VALORES PARA CADA FILA
            values.put(usuariosDBInfo.EMAIL_COLUMN, mail);
            values.put(usuariosDBInfo.NOMBRE_COLUMN,nombre);
            values.put(usuariosDBInfo.APELLIDOS_COLUMN,apellido);
            values.put(usuariosDBInfo.FECHANAC_COLUMN,fechaNac);
            values.put(usuariosDBInfo.SEXO_COLUMN,sexo);
            values.put(usuariosDBInfo.GRUPOSANGUINEO_COLUMN,sangre);
            values.put(usuariosDBInfo.NUMSEGURIDADSOCIAL_COLUMN, numSeguridad);
            db.update(usuariosDBInfo.TABLE_NAME, values, usuariosDBInfo.IDUSUARIO_COLUMN + " = " + id, null);
            db.setTransactionSuccessful();
        }catch (Exception e){

        }finally {
            db.endTransaction();
            db.close();
        }
    }
    //A PARTIR DEL EMAIL OBTENER EL ID DEL USUARIO
    public int obtenerID(String email){
        db=this.getReadableDatabase();
        int id = 0;
        String emailB;
        try{
            db.beginTransaction();
            String query="SELECT "+usuariosDBInfo.EMAIL_COLUMN+" , "+usuariosDBInfo.IDUSUARIO_COLUMN+" FROM "+usuariosDBInfo.TABLE_NAME;
            Cursor cursor=db.rawQuery(query,null);
            if(cursor.moveToFirst()){
                do{
                    emailB=cursor.getString(0);
                    if(emailB.equals(email)){
                        id=Integer.parseInt(cursor.getString(1));
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
        return id;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query="DROP TABLE IF EXISTS"+usuariosDBInfo.TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);

    }
}
