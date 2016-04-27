package com.android.vicky.taskalarm;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Vicky on 3/12/2016.
 */

public class DBTaskToDo {

    static final String DATABASE_NAME = "TaskToDo.db";
    static final int DATABASE_VERSION = 3;
//    public static final int NAME_COLUMN = 1;
    static final String DB_TABLE_NAME = "TaskData";
    static  final String KEY_ROWID = "ID";
    private String[] productsString;

    static final String DATABASE_CREATE = "create table "+DB_TABLE_NAME+
            "( " +"ID"+" integer primary key autoincrement,"+"TaskTitle text,"+
                "TaskType text,"+"TaskDescription Text,"+"TaskDate text,"+"TaskTime Text,"+
                    "TaskPriority text,"+"TaskStatus text,"+"TaskSendNo text"+");";

    // Variable to hold the database instance
    public SQLiteDatabase db;

    // Context of the application using the database.
    private final Context context;

    // Database open/upgrade helper
    private DBSqlOpenHelper dbHelper;

    public DBTaskToDo(Context _context){
        context = _context;
        dbHelper = new DBSqlOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION, DATABASE_CREATE);
    }

    public DBTaskToDo open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public long insertTask(String TaskTitle,String TaskType,String TaskDescription,String TaskDate,String TaskTime,String TaskPriority,String TaskStatus,String TaskSendNo) {

        ContentValues contentValues = new ContentValues();

        contentValues.put("TaskTitle", TaskTitle);
        contentValues.put("TaskType", TaskType);
        contentValues.put("TaskDescription", TaskDescription);
        contentValues.put("TaskDate", TaskDate);
        contentValues.put("TaskTime", TaskTime);
        contentValues.put("TaskPriority", TaskPriority);
        contentValues.put("TaskStatus", TaskStatus);
        contentValues.put("TaskSendNo", TaskSendNo);

        return db.insert(DB_TABLE_NAME, null, contentValues);

        /*long rowID = db.insert(DB_TABLE_NAME, null, contentValues);
        if(rowID < 0 ){
            return false;
        }
        return true;*/
    }

    public boolean updateTask(long rowId, String TaskTitle,String TaskType,String TaskDescription,String TaskDate,String TaskTime,String TaskPriority,String TaskStatus, String TaskSendNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("TaskTitle", TaskTitle);
        contentValues.put("TaskType", TaskType);
        contentValues.put("TaskDescription", TaskDescription);
        contentValues.put("TaskDate", TaskDate);
        contentValues.put("TaskTime", TaskTime);
        contentValues.put("TaskPriority", TaskPriority);
        contentValues.put("TaskStatus", TaskStatus);
        contentValues.put("TaskSendNo", TaskSendNo);
        return db.update(DB_TABLE_NAME, contentValues, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateTaskStatus(long rowId,String TaskStatus) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("TaskStatus", TaskStatus);
        return db.update(DB_TABLE_NAME, contentValues, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteTask(long rowId) {
        return db.delete(DB_TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
    }


    public Cursor  getTodaysTasks() {
        Cursor cursor2 = db.rawQuery("SELECT *  FROM "+DB_TABLE_NAME+" WHERE TaskDate = '"+MakeDateTimePattern.getCurrentDate()+"' ORDER BY TaskTime ASC", null);
        return cursor2;
    }

    public Cursor  getPreviousTasks() {
        Cursor cursor = db.rawQuery("SELECT *  FROM "+DB_TABLE_NAME+" WHERE TaskDate < '"+MakeDateTimePattern.getCurrentDate()+"' ORDER BY TaskDate, TaskTime DESC", null);
        return cursor;
    }

    public Cursor  getUpcomingTasks() {
        Cursor cursor = db.rawQuery("SELECT *  FROM "+DB_TABLE_NAME+" WHERE TaskDate > '"+MakeDateTimePattern.getCurrentDate()+"' ORDER BY TaskDate, TaskTime ASC", null);
        return cursor;
    }

    public Cursor getSingleTask(long rowID){
        Cursor cursor = db.query(DB_TABLE_NAME, new String[] { "ID",
                        "TaskTitle", "TaskDescription","TaskType","TaskDate","TaskTime","TaskStatus","TaskSendNo","TaskPriority"}, "ID =?",
                new String[] { String.valueOf(rowID) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }
}