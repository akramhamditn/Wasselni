package com.esprit.wasselni.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import com.esprit.wasselni.Entities.Trajets;

public class DBHelperTrajes extends SQLiteOpenHelper {

    public static final String TABLE_TRAJES = "trajes";

    public static final String ID_TRAJES = "id";
    public static final String CIN_TRAJES = "cin";
    public static final String NUM_TRAJES = "numPermis";
    public static final String DES_TRAJES = "destination";
    public static final String LAN_TRAJES = "latitude";
    public static final String LON_TRAJES= "longitude";
    public static final String LAN_CHAUFF_TRAJES = "latChauffeur";
    public static final String LON_CHAUFF_TRAJES = "lonChauffeur";
    private static final int DATABASE_VERSION = 1;




    private static final String CREATE_USER = "CREATE TABLE " + TABLE_TRAJES + " (" +
            ID_TRAJES + " TEXT, " +
            CIN_TRAJES+ " TEXT, " +
            NUM_TRAJES+ " TEXT, " +
            DES_TRAJES+ " TEXT, " +
            LAN_TRAJES+ " TEXT, " +
            LON_TRAJES+ " TEXT, " +
            LAN_CHAUFF_TRAJES+ " TEXT, " +
            LON_CHAUFF_TRAJES+ " TEXT);";

    public DBHelperTrajes(Context context) {
        super(context,TABLE_TRAJES , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAJES + ";");
        onCreate(db);
    }

    public boolean insertTrajets(Trajets t) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_TRAJES, t.getId());
        Log.e("", t.getId());
        contentValues.put(CIN_TRAJES, t.getCin());
        Log.e("", t.getCin());
        contentValues.put(NUM_TRAJES, t.getNumPermis());
        Log.e("", t.getNumPermis());
        contentValues.put(DES_TRAJES, t.getDestination());
        Log.e("", t.getDestination());
        contentValues.put(LAN_TRAJES, t.getLatitude());
       // Log.e("", t.getLatitude());
        contentValues.put(LON_TRAJES, t.getLongitude());
        //Log.e("", t.getLongitude());
        contentValues.put(LAN_CHAUFF_TRAJES, t.getLatChauffeur());
        //Log.e("", t.getLatChauffeur());
        contentValues.put(LON_CHAUFF_TRAJES, t.getLonChauffeur());
        //Log.e("", t.getLonChauffeur());
        db.execSQL("delete from trajes");
        db.insert(TABLE_TRAJES, null, contentValues);
        return true;
    }


    public ArrayList<Trajets> getAllTrajets() {
        ArrayList<Trajets> array_list = new ArrayList<Trajets>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from trajes", null);

        if (res.moveToFirst()) {
            do {
                Trajets trajets = new Trajets();
                trajets.setId(res.getString(0));
                trajets.setCin(res.getString(1));
                trajets.setNumPermis(res.getString(2));
                trajets.setDestination(res.getString(3));
                trajets.setLatitude(res.getString(4));
                trajets.setLongitude(res.getString(5));
                trajets.setLatChauffeur(res.getString(6));
                trajets.setLonChauffeur(res.getString(7));
                // Adding contact to list
                array_list.add(trajets);
            } while (res.moveToNext());
        }
        return array_list;
    }


   /* public boolean getCandidat(String email,String pwd) {
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
            } while (res.moveToNext());
            return true;
        }
        return false;
    }*/
}
