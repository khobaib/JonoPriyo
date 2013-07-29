package com.priyo.apps.jonopriyo.db;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.priyo.apps.jonopriyo.model.Area;
import com.priyo.apps.jonopriyo.model.City;
import com.priyo.apps.jonopriyo.model.Country;
import com.priyo.apps.jonopriyo.model.Education;
import com.priyo.apps.jonopriyo.model.Profession;

public class JonopriyoDatabase {

    private static final String TAG = JonopriyoDatabase.class.getSimpleName();

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context mContext;

    private static final String DATABASE_NAME = "starfish_db";
    private static final int DATABASE_VERSION = 3;


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            EducationDbManager.createTable(db);
            ProfessionDbManager.createTable(db);
            CountryDbManager.createTable(db);
            CityDbManager.createTable(db);
            AreaDbManager.createTable(db);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            EducationDbManager.dropTable(db);
            ProfessionDbManager.dropTable(db);
            CountryDbManager.dropTable(db);
            CityDbManager.dropTable(db);
            AreaDbManager.dropTable(db);

            onCreate(db);
        }
    }

    /** Constructor */
    public JonopriyoDatabase(Context ctx) {
        mContext = ctx;
    }

    public JonopriyoDatabase open() throws SQLException {
        dbHelper = new DatabaseHelper(mContext);
        db = dbHelper.getWritableDatabase();
        return this;
    }


    public void close() {
        dbHelper.close();
    }
    
    
    public void insertOrUpdateEducation(Education education) {
        EducationDbManager.insertOrupdate(this.db, education);
    }
    
    public void insertOrUpdateProfession(Profession profession) {
        ProfessionDbManager.insertOrupdate(this.db, profession);
    }
    
    public void insertOrUpdateCountry(Country country) {
        CountryDbManager.insertOrupdate(this.db, country);
    }
    
    public void insertOrUpdateCity(City city) {
        CityDbManager.insertOrupdate(this.db, city);
    }
    
    public void insertOrUpdateArea(Area area) {
        AreaDbManager.insertOrupdate(this.db, area);
    }
    
    
    
    public List<Education> retrieveEducationList() {
        return EducationDbManager.retrieve(this.db);
    }
    
    public List<Profession> retrieveProfessionList() {
        return ProfessionDbManager.retrieve(this.db);
    }
    
    public List<Country> retrieveCountryList() {
        return CountryDbManager.retrieve(this.db);
    }
    
    public List<City> retrieveCityList() {
        return CityDbManager.retrieve(this.db);
    }
    
    public List<Area> retrieveAreaList() {
        return AreaDbManager.retrieve(this.db);
    }
    
    
    
    public void deleteAllCities() {
        CityDbManager.deleteAll(this.db);
    }
    
    public void deleteAllArea() {
        AreaDbManager.deleteAll(this.db);
    }
}
