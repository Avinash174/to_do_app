package com.example.todoapp.Model.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todoapp.Model.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static  final String DATABASE_NAME ="TODO_DATABASE";
    private static  final int DATABASE_VERSION=1;
    private static  final String TABLE_NAME="TODO_TABLE";
    private static  final String COL_1="ID";
    private static  final String COL_2="TASK";
    private static  final String COL_3="STATUS";
    public DatabaseHelper( Context context) {
        super(context, DATABASE_NAME, null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT ,TASK TEXT,STATUS INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
    public void inserTask(TodoModel model){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,model.getTask());
        contentValues.put(COL_3,0 );

        db.insert(TABLE_NAME ,null  ,contentValues);

    }
    public void updateTask(int id, String task){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,id);
        contentValues.put(COL_2,task);

        db.update(TABLE_NAME,contentValues,"ID= ?",new String[]{String.valueOf(id)} );

    }
    public void updateStatus(int id,int status){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_3,status);
        db.update(TABLE_NAME,contentValues,"ID=?",new String[]{String.valueOf(id)});
    }
    public  void deleteTask(int id){
        SQLiteDatabase db =this.getWritableDatabase();
       db.delete(TABLE_NAME,"ID=?",new String[]{String.valueOf(id)});

    }

    public List<TodoModel> getAllTask() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        List<TodoModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    TodoModel todoModel = new TodoModel();
                    todoModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_1)));
                    todoModel.setTask(cursor.getString(cursor.getColumnIndexOrThrow(COL_2)));
                    todoModel.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COL_3)));
                    modelList.add(todoModel);
                } while (cursor.moveToNext());
            }
            db.setTransactionSuccessful(); // ✅ good practice
        } finally {
            db.endTransaction();
            if (cursor != null && !cursor.isClosed()) {
                cursor.close(); // ✅ prevent crash on null or closed cursor
            }
        }
        return modelList;
    }


}
