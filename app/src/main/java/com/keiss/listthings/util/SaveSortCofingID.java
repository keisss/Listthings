package com.keiss.listthings.util;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by hekai on 16/5/23.
 */
public class SaveSortCofingID {
    public static void save(String id){
        FileOutputStream outputStream = null;
        BufferedWriter writer = null;
        try{
            outputStream = ContextGetter.getInstance().openFileOutput("SortCofingId", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(id);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (writer !=null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
