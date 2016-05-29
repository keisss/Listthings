package com.keiss.listthings.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.keiss.listthings.db.ThingsData;

import java.util.Map;

/**
 * Created by hekai on 16/5/18.
 */
public class DropPositionAndReBuild {
   static ContentValues  values;


    public static void DropAndRebuild(){
        Cursor cursor;
        SQLiteDatabase db=ThingsData.getInstance(ContextGetter.getInstance()).getWritableDatabase();

        int k=0;
        int id;

        ContentValues values =new ContentValues();

        cursor = db.rawQuery("select * from things",null);

        while (cursor.moveToNext()){
        id = cursor.getInt(0);
            values.put("position",k);
            db.update("things",values,"id= ?",new String[]{String.valueOf(id)});
            k++;
        }




cursor.close();


    }
}
