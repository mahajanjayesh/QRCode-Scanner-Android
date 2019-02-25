package com.jm.android.qrcodescanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static final String DATABASE_NAME = "QRcodeDb";
    public static final String TABLE_NAME = "QRcodeData";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "Data";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT , Data TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

    public boolean insertData(String Data){
        Log.i("DBENTRY",Data);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,Data);
        long result = db.insert(TABLE_NAME,null,contentValues);
        db.close();
        if(result == -1){
            return false;
        }else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * FROM " + TABLE_NAME,null);
        return res;
    }

}
