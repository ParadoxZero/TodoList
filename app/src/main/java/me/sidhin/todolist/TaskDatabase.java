package me.sidhin.todolist;/*
 * Created by Zero on 25-May-16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TaskDatabase extends SQLiteOpenHelper {

    /* Constants to bu used */

    public static final String DATABASE_NAME = "sidhin.todo";
    public static final int DATABASE_VERSION = 1 ;


    public static final String TABLE_NAME_TASK = "taskHolder";
    public static final String COLUMN_NAME_ID = "taskID";
    public static final String COLUMN_NAME_TASK = "taskDetail" ;
    public static final String COLUMN_NAME_DONE = "done" ;

    /* Some basic query to be used */

    public static final String TYPE_TEXT = " TEXT " ;
    public static final String TYPE_INT = " INTEGER " ;
    public static final String TYPE_BOOL = "";
    public static final String COMMA = "," ;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME_TASK + " (" +
                    COLUMN_NAME_ID + TYPE_INT + "PRIMARY KEY" + COMMA +
                    COLUMN_NAME_TASK + TYPE_TEXT + COMMA +
                    COLUMN_NAME_DONE + TYPE_INT +
            ")";
    public static final String DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME_TASK;


    public TaskDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, newVersion, oldVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE);
        onCreate(db);
    }

    public boolean addEntry(Task task){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ID,task.ID);
        values.put(COLUMN_NAME_TASK, task.Details);
        int i = 0 ;
        if(task.done)
            i = 1 ;
        values.put(COLUMN_NAME_DONE,i);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME_TASK, null, values);
        return true;
    }

    public void deleteEntry(int key){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_TASK + " WHERE " + COLUMN_NAME_ID + "=\"" + key + "\";");
    }

    public ArrayList<Task> getAllTask(){
        ArrayList<Task> list = new ArrayList<Task>();
        SQLiteDatabase db = getReadableDatabase();
        String [] projection = {
                COLUMN_NAME_ID,
                COLUMN_NAME_TASK,
                COLUMN_NAME_DONE
        };
        Cursor cursor = db.query(TABLE_NAME_TASK, projection, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            Task t = new Task(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TASK)));
            if(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_DONE))==1)
                t.done = true ;
            list.add(t);
            cursor.moveToNext();
        }
        return list;
    }

    public void update(Task t){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ID,t.ID);
        values.put(COLUMN_NAME_TASK,t.Details);
        int i = 0 ;
        if(t.done)
            i = 1 ;
        values.put(COLUMN_NAME_DONE,i);
        db.update(TABLE_NAME_TASK,values,COLUMN_NAME_ID +"=" + t.ID, null);
    }
}

