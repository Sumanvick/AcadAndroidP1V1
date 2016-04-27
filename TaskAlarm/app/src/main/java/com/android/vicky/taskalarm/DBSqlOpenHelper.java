package com.android.vicky.taskalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vicky on 3/13/2016.
 */
class DBSqlOpenHelper extends SQLiteOpenHelper {

    String DatabaseCreateQuery;

    public DBSqlOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, String DATABASE_CREATE) {
        super(context, name, factory, version);
        this.DatabaseCreateQuery = DATABASE_CREATE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(this.DatabaseCreateQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade the existing database to conform to the new version. Multiple
        // previous versions can be handled by comparing _oldVersion and _newVersion values.

        // The simplest case is to drop the old table and create a new one.
        db.execSQL("DROP TABLE IF EXISTS " + "TaskData");

        // Create a new one.
        onCreate(db);
    }
}
