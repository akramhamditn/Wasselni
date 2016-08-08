package com.esprit.wasselni.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import com.esprit.wasselni.Entities.User;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_USER = "user";

    public static final String ID_USER = "id";
    public static final String NOM_USER = "nom";
    public static final String PRENOM_USER = "prenom";
    public static final String ADRESSE_USER = "adresse";
    public static final String EMAIL_USER = "email";
    public static final String PWD_USER = "password";
    public static final String TYPE_USER = "type";
    public static final String TEL_USER = "tel";
    private static final int DATABASE_VERSION = 1;




    private static final String CREATE_USER = "CREATE TABLE " + TABLE_USER + " (" +
            ID_USER + " TEXT, " +
            NOM_USER+ " TEXT, " +
            PRENOM_USER+ " TEXT, " +
            ADRESSE_USER+ " TEXT, " +
            EMAIL_USER+ " TEXT, " +
            PWD_USER+ " TEXT, " +
            TYPE_USER+ " TEXT, " +
            TEL_USER+ " TEXT);";

    public DBHelper(Context context) {
        super(context,TABLE_USER , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER + ";");
        onCreate(db);
    }

    public boolean insertUser(User u) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_USER, u.getId());
        Log.e("", u.getId());
        contentValues.put(NOM_USER, u.getNom());
        Log.e("", u.getNom());
        contentValues.put(PRENOM_USER, u.getPrenom());
        Log.e("", u.getPrenom());
        contentValues.put(ADRESSE_USER, u.getAdresse());
        Log.e("", u.getAdresse());
        contentValues.put(EMAIL_USER, u.getEmail());
        Log.e("", u.getEmail());
        contentValues.put(PWD_USER, u.getPassword());
        Log.e("", u.getPassword());
        contentValues.put(TYPE_USER, u.getType());
        Log.e("", u.getType());
        contentValues.put(TEL_USER, u.getTelephone());
        Log.e("", u.getTelephone());
        db.insert(TABLE_USER, null, contentValues);
        return true;
    }


    public ArrayList<User> getAllCandidat() {
        ArrayList<User> array_list = new ArrayList<User>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from user", null);

        if (res.moveToFirst()) {
            do {
                User user = new User();
                user.setId(res.getString(0));
                user.setNom(res.getString(1));
                user.setPrenom(res.getString(2));
                user.setAdresse(res.getString(3));
                user.setEmail(res.getString(4));
                user.setPassword(res.getString(5));
                user.setType(res.getString(6));
                user.setTelephone(res.getString(7));
                // Adding contact to list
                array_list.add(user);
            } while (res.moveToNext());
        }
        return array_list;
    }
    public boolean getCandidat(String email,String pwd) {
        ArrayList<User> array_list = new ArrayList<User>();
        User user = new User();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from user where email = '"+email+"' and password = '"+pwd+"'", null);

        if (res != null) {
            /*do {
                user.setId(res.getString(0));
                user.setNom(res.getString(1));
                user.setPrenom(res.getString(2));
                user.setAdresse(res.getString(3));
                user.setEmail(res.getString(4));
                user.setPassword(res.getString(5));
                user.setType(res.getString(6));
                user.setTelephone(res.getString(7));
                // Adding contact to list
               // array_list.add(user);
            } while (res.moveToNext());*/
            return true;
        }
        return false;
    }
}
