package com.priyo.apps.jonopriyo.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.priyo.apps.jonopriyo.model.Area;

public class AreaDbManager {
    
 private static final String TAG = AreaDbManager.class.getSimpleName();
    
    private static String TABLE_AREA_LIST = "area_list_table";
    
    public static final String TABLE_PRIMARY_KEY = "_id";
    
    private static String ID = "id";
    private static String NAME = "name";

    
    private static final String CREATE_TABLE_AREA_LIST = "create table " + TABLE_AREA_LIST + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + ID + " integer, " + NAME + " text);";
    
    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_AREA_LIST);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AREA_LIST);
    }
    
    
    public static long insert(SQLiteDatabase db, Area area) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(ID, area.getId());
        cv.put(NAME, area.getName());
        
        return db.insert(TABLE_AREA_LIST, null, cv);
    }
    
    
    public static List<Area> retrieve(SQLiteDatabase db) throws SQLException {
        List<Area> areaList = new ArrayList<Area>();
        
        Cursor c = db.query(TABLE_AREA_LIST, null, null, null, null, null, null);
        if(c != null && c.getCount() > 0){
            c.moveToFirst();
            while(!c.isAfterLast()){
                Long id = c.getLong(c.getColumnIndex(ID));
                String name = c.getString(c.getColumnIndex(NAME));
                
                Area area = new Area(id, name);
                areaList.add(area);
                
                c.moveToNext();
            }
        }
        return areaList;
        
    }
    
    
    public static long update(SQLiteDatabase db, Area area) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(NAME, area.getName());
        
        return db.update(TABLE_AREA_LIST, cv, ID + "=" + area.getId(), null); 
    }
    
    
    public static boolean isExist(SQLiteDatabase db, long id) throws SQLException {
        boolean itemExist = false;

        Cursor c = db.query(TABLE_AREA_LIST, null, ID + "=" + id, null, null, null, null);

        if ((c != null) && (c.getCount() > 0)) {
            itemExist = true;
        }
        return itemExist;
    }
    
    
    public static void insertOrupdate(SQLiteDatabase db, Area area){
        if(isExist(db, area.getId())){
            update(db, area);
        }
        else{
            insert(db, area);
        }
    }
    
    public static void deleteAll(SQLiteDatabase db){
        db.delete(TABLE_AREA_LIST, null, null);
    }

}
