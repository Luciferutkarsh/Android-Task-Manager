package com.bachelorshelter.tanvir.cutmytask.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bachelorshelter.tanvir.cutmytask.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tanvir on 2017-05-31.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "cutMyTask";

    // Task table name
    private static final String TABLE_TASK = "task";

    // Task Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ACTION = "action";//Finished(1) Unfinished(0)
    private static final String KEY_TASK_DATE = "task_date";
    private static final String KEY_TASK_DESC = "task_desc";
    private static final String KEY_BACK_COLOR = "back_color";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TASK + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ACTION + " INTEGER,"
                + KEY_TASK_DATE + " DATETIME,"
                + KEY_BACK_COLOR + " TEXT,"
                + KEY_TASK_DESC + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);

        // Create tables again
        onCreate(db);

    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    // Adding new task
    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACTION, task.getAction()); // Task Action Done(1) Undone(0)
        values.put(KEY_TASK_DATE, task.getDate()); // Task date
        values.put(KEY_TASK_DESC, task.getTaskDesc()); // Task Description
        values.put(KEY_BACK_COLOR,task.getBackColor());

        // Inserting Row
        return db.insert(TABLE_TASK, null, values);// Return task_id
    }

    /**
    * get single Task by id
    */

    public Task getTask(long task_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TASK + " WHERE "
                + KEY_ID + " = " + task_id;

        //Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Task task = new Task();
        task.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        task.setAction(c.getInt(c.getColumnIndex(KEY_ACTION)));
        task.setTaskDesc((c.getString(c.getColumnIndex(KEY_TASK_DESC))));
        task.setDate(c.getString(c.getColumnIndex(KEY_TASK_DATE)));
        task.setBackColor(c.getString(c.getColumnIndex(KEY_BACK_COLOR)));
        return task;
    }


    /**
    * getting all task
    */
    public List<Task> getAllTask() {
        List<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASK;

        //Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Task task = new Task();

                task.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                task.setAction(c.getInt(c.getColumnIndex(KEY_ACTION)));
                task.setTaskDesc((c.getString(c.getColumnIndex(KEY_TASK_DESC))));
                task.setDate(c.getString(c.getColumnIndex(KEY_TASK_DATE)));
                task.setBackColor(c.getString(c.getColumnIndex(KEY_BACK_COLOR)));
                // adding to task list
                tasks.add(task);
            } while (c.moveToNext());
        }

        return tasks;
    }

    /**
     * getting all task
     */
    public List<Task> getAllTaskByDate(String date) {

        String startDate = "'"+date+" 00:00:00'";
        String endDate = "'"+date+" 23:59:59'";

        List<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASK+" WHERE "+KEY_TASK_DATE+" BETWEEN "+startDate+" AND "+endDate+" ORDER BY "+KEY_TASK_DATE+" ASC";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Task task = new Task();

                task.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                task.setAction(c.getInt(c.getColumnIndex(KEY_ACTION)));
                task.setTaskDesc((c.getString(c.getColumnIndex(KEY_TASK_DESC))));
                task.setDate(c.getString(c.getColumnIndex(KEY_TASK_DATE)));
                task.setBackColor(c.getString(c.getColumnIndex(KEY_BACK_COLOR)));
                // adding to task list
                tasks.add(task);
            } while (c.moveToNext());
        }

        return tasks;
    }


    //Update Task action
    public void updateAction(int id,int action){

        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE "+TABLE_TASK+" SET "+KEY_ACTION+" = "+action+" WHERE "+KEY_ID+" = "+ id;
        db.execSQL(strSQL);

    }

    //Update Task's everything
    public void updateTask(Task task,int id){

        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE "+TABLE_TASK+" SET "+KEY_ACTION+" = "+task.getAction()+","
                +KEY_TASK_DESC+" = '"+task.getTaskDesc()+"',"
                +KEY_TASK_DATE+" = '"+task.getDate()+"',"
                +KEY_BACK_COLOR+" = '"+task.getBackColor()
                +"' WHERE "+KEY_ID+" = "+ id;
        db.execSQL(strSQL);


    }

    //Update Task's everything
    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        //DELETE FROM tutorials_tbl WHERE tutorial_id=3
        String strSQL = "DELETE FROM "+TABLE_TASK+" WHERE "+KEY_ID+" = "+ id;
        db.execSQL(strSQL);


    }



    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }




}
