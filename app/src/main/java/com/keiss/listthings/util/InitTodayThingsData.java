package com.keiss.listthings.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.keiss.listthings.db.ThingsData;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hekai on 16/5/28.
 */
public class InitTodayThingsData {
    private static String ThingsCreateDateText;
    private static String ThingsModifyDateText;
    private static String ThingsTodayDateText ;



    public static  void initData(String[] title, String[] content, int[] things_grade,int[] things_class,String[] things_create_time,String[] things_modify_time){



        //获取今天时间
        Date curDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatDateText = new SimpleDateFormat("MM-dd");
        ThingsTodayDateText= formatDateText.format(curDate);




        SQLiteDatabase db= ThingsData.getInstance(ContextGetter.getInstance()).getWritableDatabase();


        Cursor cursor  = db.rawQuery("select * from things ",null);
        int i=0;

        while (cursor.moveToNext()){
            ThingsCreateDateText = cursor.getString(7);
            ThingsModifyDateText = cursor.getString(9);
            if (ThingsCreateDateText.equals(ThingsTodayDateText)||ThingsModifyDateText.equals(ThingsTodayDateText)){

                    title[i] = cursor.getString(2);
                    content[i] = cursor.getString(3);
                    things_grade[i] =cursor.getInt(5);
                    things_class[i] = cursor.getInt(4);
                    things_create_time[i] = cursor.getString(7);
                    things_modify_time[i] = cursor.getString(9);
                    i++;continue;
                }



        }
        cursor.close();

    }
}
