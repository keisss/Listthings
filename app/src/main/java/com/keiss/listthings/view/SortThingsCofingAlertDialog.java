package com.keiss.listthings.view;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.keiss.listthings.cofing.SetSortThings;
import com.keiss.listthings.util.ContextGetter;

/**
 * Created by hekai on 16/5/22.
 */
public class SortThingsCofingAlertDialog {
    private static   int SortCofingId;


    public static void SetAlertDialog(){
         SortCofingId = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(ContextGetter.getInstance());
        builder.setTitle("选择排序方式");
        final String[] SortWith = {"创建时间","修改时间","优先级"};
        builder.setItems(SortWith,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface,int which){
                switch (which){
                    case 0 :
                        SetSortThings.initDataSOrtInCreateTime();
                        SortCofingId = 1;break;
                    case 1 :
                        SetSortThings.initDataSOrtInModifyTime();
                        SortCofingId=2;break;
                    case 2 :
                        SetSortThings.initDataSOrtInThingsGrade();
                        SortCofingId=3;break;
                }
            }
        });
        builder.show();
    }
}
