package com.priyo.apps.jonopriyo.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.priyo.apps.jonopriyo.model.Education;


public class EducationDbManager {
    
    private static final String TAG = EducationDbManager.class.getSimpleName();
    
    private static String TABLE_EDUCATION_LIST = "education_list_table";
    
    public static final String TABLE_PRIMARY_KEY = "_id";
    
    private static String ID = "id";
    private static String TYPE = "type";

    
    private static final String CREATE_TABLE_EDUCATION_LIST = "create table " + TABLE_EDUCATION_LIST + " ( "
            + TABLE_PRIMARY_KEY + " integer primary key autoincrement, " + ID + " integer, " + TYPE + " text);";
    
    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EDUCATION_LIST);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDUCATION_LIST);
    }
    
    
    public static long insert(SQLiteDatabase db, Education education) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(ID, education.getId());
        cv.put(TYPE, education.getType());
        
        return db.insert(TABLE_EDUCATION_LIST, null, cv);
    }
    
    
    public static List<Education> retrieve(SQLiteDatabase db) throws SQLException {
        List<Education> educationList = new ArrayList<Education>();
        
        Cursor c = db.query(TABLE_EDUCATION_LIST, null, null, null, null, null, null);
        if(c != null && c.getCount() > 0){
            c.moveToFirst();
            while(!c.isAfterLast()){
                Long id = c.getLong(c.getColumnIndex(ID));
                String type = c.getString(c.getColumnIndex(TYPE));
                
                Education education = new Education(id, type);
                educationList.add(education);
                
                c.moveToNext();
            }
        }
        return educationList;
        
    }
    
    
    public static long update(SQLiteDatabase db, Education education) throws SQLException {

        ContentValues cv = new ContentValues();

        cv.put(TYPE, education.getType());
        
        return db.update(TABLE_EDUCATION_LIST, cv, ID + "=" + education.getId(), null); 
    }
    
    
    public static boolean isExist(SQLiteDatabase db, long id) throws SQLException {
        boolean itemExist = false;

        Cursor c = db.query(TABLE_EDUCATION_LIST, null, ID + "=" + id, null, null, null, null);

        if ((c != null) && (c.getCount() > 0)) {
            itemExist = true;
        }
        return itemExist;
    }
    
    
    public static void insertOrupdate(SQLiteDatabase db, Education education){
        if(isExist(db, education.getId())){
            update(db, education);
        }
        else{
            insert(db, education);
        }
    }

}
