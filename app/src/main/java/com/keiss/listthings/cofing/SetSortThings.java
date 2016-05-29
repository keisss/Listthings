package com.keiss.listthings.cofing;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.keiss.listthings.db.ThingsData;
import com.keiss.listthings.util.ContextGetter;


/**
 * Created by hekai on 16/5/21.
 */
public class SetSortThings {
    static ContentValues  values;

    public static void initDataSOrtInModifyTime(){
        ContentValues values =new ContentValues();
        SQLiteDatabase db= ThingsData.getInstance(ContextGetter.getInstance()).getWritableDatabase();
        Cursor cursorNormal = db.rawQuery("select *from things",null);
        Cursor cursor  = db.rawQuery("select *from things ORDER BY things_modify_date_int desc",null);

        while (cursor.moveToNext()&&cursorNormal.moveToNext()){

            values.put("things_title",cursor.getString(2));
            values.put("things_content",cursor.getString(3));
            values.put("things_class",cursor.getInt(4));
            values.put("things_grade",cursor.getInt(5));
            values.put("things_create_date_int",cursor.getLong(6));
            values.put("things_create_date_text",cursor.getString(7));
            values.put("things_modify_date_int",cursor.getLong(8));
            values.put("things_modify_date_text",cursor.getString(9));
            values.put("things_class",cursor.getInt(4));
            db.update("things",values,"id == ?",new String[]{String.valueOf(cursorNormal.getInt(0))});
        }

    }


    public static void initDataSOrtInCreateTime(){
        ContentValues values =new ContentValues();
        SQLiteDatabase db= ThingsData.getInstance(ContextGetter.getInstance()).getWritableDatabase();
        Cursor cursorNormal = db.rawQuery("select *from things",null);
        Cursor cursor  = db.rawQuery("select *from things ORDER BY things_create_date_int desc",null);

        while (cursor.moveToNext()&&cursorNormal.moveToNext()){
            values.put("things_class",cursor.getInt(4));
            values.put("things_title",cursor.getString(2));
            values.put("things_content",cursor.getString(3));
            values.put("things_class",cursor.getInt(4));
            values.put("things_grade",cursor.getInt(5));
            values.put("things_create_date_int",cursor.getLong(6));
            values.put("things_create_date_text",cursor.getString(7));
            values.put("things_modify_date_int",cursor.getLong(8));
            values.put("things_modify_date_text",cursor.getString(9));
            db.update("things",values,"id == ?",new String[]{String.valueOf(cursorNormal.getInt(0))});
        }

    }

    public static void initDataSOrtInThingsGrade(){
        ContentValues values =new ContentValues();
        SQLiteDatabase db= ThingsData.getInstance(ContextGetter.getInstance()).getWritableDatabase();
        Cursor cursorNormal = db.rawQuery("select *from things",null);
        Cursor cursor  = db.rawQuery("select *from things ORDER BY things_grade desc",null);

        while (cursor.moveToNext()&&cursorNormal.moveToNext()){
            values.put("things_class",cursor.getInt(4));
            values.put("things_title",cursor.getString(2));
            values.put("things_content",cursor.getString(3));
            values.put("things_class",cursor.getInt(4));
            values.put("things_grade",cursor.getInt(5));
            values.put("things_create_date_int",cursor.getLong(6));
            values.put("things_create_date_text",cursor.getString(7));
            values.put("things_modify_date_int",cursor.getLong(8));
            values.put("things_modify_date_text",cursor.getString(9));
            db.update("things",values,"id == ?",new String[]{String.valueOf(cursorNormal.getInt(0))});
        }

    }

    }

