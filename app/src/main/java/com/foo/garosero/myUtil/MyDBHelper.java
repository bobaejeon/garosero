package com.foo.garosero.myUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.foo.garosero.data.DiaryData;

import java.util.ArrayList;

public class MyDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "d231";
    private static final String SQL_DELETE_DIARY = "DROP TABLE IF EXISTS " + DATABASE_NAME;
    private static final String SQL_CREATE_DIARY =
            "CREATE TABLE " + DATABASE_NAME +
                    "(\n" +
                    "diaryID   INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    "schedule  TEXT, \n" +
                    "persons   INTEGER, \n" +
                    "content   TEXT, \n" +
                    "memo      TEXT, \n" +
                    "picture   TEXT)";

    private static String COL_DIARYID = "diaryID";
    private static String COL_SCHEDULE = "schedule";
    private static String COL_PERSONS = "persons";
    private static String COL_CONTENT = "content";
    private static String COL_MEMO = "memo";
    private static String COL_PICTURE = "picture";

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public MyDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public MyDBHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    // CREATE TABLE
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DIARY);
    }

    // UPGRADE TABLE
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_DIARY);
        onCreate(db);
    }

    // CREATE
    public void insert(DiaryData data) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_SCHEDULE, data.getSchedule());
        values.put(COL_MEMO, data.getMemo());
        values.put(COL_CONTENT, data.getContent());
        values.put(COL_PERSONS, data.getPersons());
        values.put(COL_PICTURE, data.getPicture());

        long newRowId = db.insert(DATABASE_NAME, null, values);
    }

    // READ
    public ArrayList<DiaryData> getAll() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<DiaryData> result = new ArrayList<DiaryData>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_NAME, null);
        while (cursor.moveToNext()) {
            DiaryData data = new DiaryData();
            data.setDiaryID(cursor.getInt(0));
            data.setSchedule(cursor.getString(1));
            data.setPersons(cursor.getInt(2));
            data.setContent(cursor.getString(3));
            data.setMemo(cursor.getString(4));
            data.setPicture(cursor.getString(5));
            result.add(data);
        }

        db.close();
        return result;
    }

    // DELETE (ID)
    public void delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DATABASE_NAME, "diaryID=?", new String[]{String.valueOf(id)});
    }

    // UPDATE (ID)
    public void update(DiaryData data){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SCHEDULE, data.getSchedule());
        values.put(COL_MEMO, data.getMemo());
        values.put(COL_CONTENT, data.getContent());
        values.put(COL_PERSONS, data.getPersons());
        values.put(COL_PICTURE, data.getPicture());

        db.update(DATABASE_NAME, values, "diaryID=?", new String[]{String.valueOf(data.getDiaryID())});
    }
}
