package com.keiss.listthings.util;

import android.provider.SyncStateContract;

import java.io.File;

/**
 * Created by hekai on 16/5/26.
 */
public class DeleteFiles {
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        }
    }


}
