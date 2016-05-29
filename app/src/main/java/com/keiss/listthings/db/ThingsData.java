package com.keiss.listthings.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hekai on 16/5/8.
 */
public class ThingsData extends SQLiteOpenHelper {
    private static ThingsData mInstance = null;

    public static  final  String CREATE_THINGS = "create table things ("
            +"id integer primary key autoincrement,"
            +"position integer  ,"
            +"things_title text,"
            +"things_content text,"
            + "things_class text,"
            +"things_grade integer,"
            +"things_create_date_int integer,"
            +"things_create_date_text text,"
            +"things_modify_date_int integer,"
            +"things_modify_date_text text,"
            +"things_remind_boolean,"
            +"things_remind_time)";
    private Context mContext;

    public synchronized static ThingsData getInstance(Context context)  {


        if (mInstance == null) {
            mInstance = new ThingsData(context.getApplicationContext());
        }
        return mInstance;
    }

    public ThingsData(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext=context;
    }


    private ThingsData(Context context){
        super(context, "Things_Store.db", null, 1);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }


    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_THINGS);

    }
}
