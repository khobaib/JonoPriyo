package com.priyo.apps.jonopriyo.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.priyo.apps.jonopriyo.model.Profession;

public class ProfessionDbManager {
    
    private static final String TAG = ProfessionDbManager.class.getSimpleName();
    
    private static String TABLE_PROFESSION_LIST = "profession_list_table";
    
    public static final String TABLE_PRIMARY_KEY = "_id";
    
    private static String ID = "id";
    private static String TYPE = "type";

    
    private static final String CREATE_TABLE_PROFESSION_LIST = "create table " + TABLE_PROFESSION_LIST + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + ID + " integer, " + TYPE + " text);";
    
    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PROFESSION_LIST);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFESSION_LIST);
    }
    
    
    public static long insert(SQLiteDatabase db, Profession profession) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(ID, profession.getId());
        cv.put(TYPE, profession.getType());
        
        return db.insert(TABLE_PROFESSION_LIST, null, cv);
    }
    
    
    public static List<Profession> retrieve(SQLiteDatabase db) throws SQLException {
        List<Profession> professionList = new ArrayList<Profession>();
        
        Cursor c = db.query(TABLE_PROFESSION_LIST, null, null, null, null, null, null);
        if(c != null && c.getCount() > 0){
            c.moveToFirst();
            while(!c.isAfterLast()){
                Long id = c.getLong(c.getColumnIndex(ID));
                String type = c.getString(c.getColumnIndex(TYPE));
                
                Profession profession = new Profession(id, type);
                professionList.add(profession);
                
                c.moveToNext();
            }
        }
        return professionList;
        
    }
    
    
    public static long update(SQLiteDatabase db, Profession profession) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(TYPE, profession.getType());
        
        return db.update(TABLE_PROFESSION_LIST, cv, ID + "=" + profession.getId(), null); 
    }
    
    
    public static boolean isExist(SQLiteDatabase db, long id) throws SQLException {
        boolean itemExist = false;

        Cursor c = db.query(TABLE_PROFESSION_LIST, null, ID + "=" + id, null, null, null, null);

        if ((c != null) && (c.getCount() > 0)) {
            itemExist = true;
        }
        return itemExist;
    }
    
    
    public static void insertOrupdate(SQLiteDatabase db, Profession profession){
        if(isExist(db, profession.getId())){
            update(db, profession);
        }
        else{
            insert(db, profession);
        }
    }

}
