package com.keiss.listthings.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.keiss.listthings.db.ThingsData;

/**
 * Created by hekai on 16/5/20.
 */
public abstract class InitThingsData {


    public static  void initData(String[] title, String[] content, int[] things_grade,int[] things_class,String[] things_create_time,String[] things_modify_time){

        SQLiteDatabase db= ThingsData.getInstance(ContextGetter.getInstance()).getWritableDatabase();
        Cursor cursor  = db.rawQuery("select * from things ",null);
        int i=0;

        while (cursor.moveToNext()){

            title[i] = cursor.getString(2);
            content[i] = cursor.getString(3);
            things_grade[i] =cursor.getInt(5);
            things_class[i] = cursor.getInt(4);
            things_create_time[i] = cursor.getString(7);
            things_modify_time[i] = cursor.getString(9);
            i++;
        }
        cursor.close();

    }
}


