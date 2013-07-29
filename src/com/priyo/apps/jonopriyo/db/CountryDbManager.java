package com.priyo.apps.jonopriyo.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.priyo.apps.jonopriyo.model.Country;

public class CountryDbManager {

    private static final String TAG = CountryDbManager.class.getSimpleName();
    
    private static String TABLE_COUNTRY_LIST = "country_list_table";
    
    public static final String TABLE_PRIMARY_KEY = "_id";
    
    private static String ID = "id";
    private static String NAME = "name";

    
    private static final String CREATE_TABLE_COUNTRY_LIST = "create table " + TABLE_COUNTRY_LIST + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + ID + " integer, " + NAME + " text);";
    
    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COUNTRY_LIST);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRY_LIST);
    }
    
    
    public static long insert(SQLiteDatabase db, Country country) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(ID, country.getId());
        cv.put(NAME, country.getName());
        
        return db.insert(TABLE_COUNTRY_LIST, null, cv);
    }
    
    
    public static List<Country> retrieve(SQLiteDatabase db) throws SQLException {
        List<Country> countryList = new ArrayList<Country>();
        
        Cursor c = db.query(TABLE_COUNTRY_LIST, null, null, null, null, null, null);
        if(c != null && c.getCount() > 0){
            c.moveToFirst();
            while(!c.isAfterLast()){
                Long id = c.getLong(c.getColumnIndex(ID));
                String name = c.getString(c.getColumnIndex(NAME));
                
                Country country = new Country(id, name);
                countryList.add(country);
                
                c.moveToNext();
            }
        }
        return countryList;
        
    }
    
    
    public static long update(SQLiteDatabase db, Country country) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(NAME, country.getName());
        
        return db.update(TABLE_COUNTRY_LIST, cv, ID + "=" + country.getId(), null); 
    }
    
    
    public static boolean isExist(SQLiteDatabase db, long id) throws SQLException {
        boolean itemExist = false;

        Cursor c = db.query(TABLE_COUNTRY_LIST, null, ID + "=" + id, null, null, null, null);

        if ((c != null) && (c.getCount() > 0)) {
            itemExist = true;
        }
        return itemExist;
    }
    
    
    public static void insertOrupdate(SQLiteDatabase db, Country country){
        if(isExist(db, country.getId())){
            update(db, country);
        }
        else{
            insert(db, country);
        }
    }
}
