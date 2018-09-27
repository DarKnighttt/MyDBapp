package com.projects.darknigh.mydbapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHandler {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "myIMDB";
    private static final String TABLE_FILMS = "films";
    private static final String FILM_ID = "id";
    private static final String FILM_NAME = "name";
    private static final String FILM_DIR_NAME = "director";
    private static final String FILM_BUDGET = "budget";
    private static final String FILM_DATE = "date";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FILMS + "("
                + FILM_ID + " INTEGER PRIMARY KEY," + FILM_NAME + " TEXT,"
                + FILM_DIR_NAME + " TEXT," + FILM_BUDGET + " INTEGER," + FILM_DATE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILMS);
        onCreate(db);
    }

    @Override
    public void addFilm(Film film) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FILM_NAME, film.getName());
        values.put(FILM_DIR_NAME, film.getDirector());
        values.put(FILM_BUDGET, film.getBudget());
        values.put(FILM_DATE, film.getDate());

        db.insert(TABLE_FILMS, null, values);
        db.close();
    }

    @Override
    public Film getFilm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Film film;
        Cursor cursor = db.query(TABLE_FILMS, new String[] { FILM_ID,
                        FILM_DIR_NAME, FILM_BUDGET, FILM_DATE }, FILM_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }


        film = new Film(Integer.valueOf(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            Integer.valueOf(cursor.getString(3)),
                            cursor.getString(4));
        cursor.close();
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> filmList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_FILMS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Film film = new Film();
                film.setID(Integer.parseInt(cursor.getString(0)));
                film.setName(cursor.getString(1));
                film.setDirector(cursor.getString(2));
                film.setBudget(cursor.getInt(3));
                film.setDate(cursor.getString(4));
                filmList.add(film);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return filmList;
    }

    @Override
    public void updateFilm(Film film) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FILM_NAME, film.getName());
        values.put(FILM_DIR_NAME, film.getDirector());
        values.put(FILM_BUDGET, film.getBudget());
        values.put(FILM_DATE, film.getDate());
        db.update(TABLE_FILMS, values, FILM_ID + " = ?",
                new String[] { String.valueOf(film.getID()) });
    }

    @Override
    public void deleteFilm(Film film) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FILMS, FILM_ID + " = ?", new String[] { String.valueOf(film.getID()) });
        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FILMS, null, null);
        db.close();
    }
}
