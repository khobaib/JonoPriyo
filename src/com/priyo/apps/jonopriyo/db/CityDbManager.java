package com.priyo.apps.jonopriyo.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.priyo.apps.jonopriyo.model.City;

public class CityDbManager {
    
    private static final String TAG = CityDbManager.class.getSimpleName();
    
    private static String TABLE_CITY_LIST = "city_list_table";
    
    public static final String TABLE_PRIMARY_KEY = "_id";
    
    private static String ID = "id";
    private static String NAME = "name";

    
    private static final String CREATE_TABLE_CITY_LIST = "create table " + TABLE_CITY_LIST + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + ID + " integer, " + NAME + " text);";
    
    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CITY_LIST);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY_LIST);
    }
    
    
    public static long insert(SQLiteDatabase db, City city) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(ID, city.getId());
        cv.put(NAME, city.getName());
        
        return db.insert(TABLE_CITY_LIST, null, cv);
    }
    
    
    public static List<City> retrieve(SQLiteDatabase db) throws SQLException {
        List<City> cityList = new ArrayList<City>();
        
        Cursor c = db.query(TABLE_CITY_LIST, null, null, null, null, null, null);
        if(c != null && c.getCount() > 0){
            c.moveToFirst();
            while(!c.isAfterLast()){
                Long id = c.getLong(c.getColumnIndex(ID));
                String name = c.getString(c.getColumnIndex(NAME));
                
                City city = new City(id, name);
                cityList.add(city);
                
                c.moveToNext();
            }
        }
        return cityList;
        
    }
    
    
    public static long update(SQLiteDatabase db, City city) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(NAME, city.getName());
        
        return db.update(TABLE_CITY_LIST, cv, ID + "=" + city.getId(), null); 
    }
    
    
    public static boolean isExist(SQLiteDatabase db, long id) throws SQLException {
        boolean itemExist = false;

        Cursor c = db.query(TABLE_CITY_LIST, null, ID + "=" + id, null, null, null, null);

        if ((c != null) && (c.getCount() > 0)) {
            itemExist = true;
        }
        return itemExist;
    }
    
    
    public static void insertOrupdate(SQLiteDatabase db, City city){
        if(isExist(db, city.getId())){
            update(db, city);
        }
        else{
            insert(db, city);
        }
    }
    
    public static void deleteAll(SQLiteDatabase db){
        db.delete(TABLE_CITY_LIST, null, null);
    }

}
