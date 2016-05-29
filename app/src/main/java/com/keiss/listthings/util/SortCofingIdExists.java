package com.keiss.listthings.util;

import java.io.File;

/**
 * Created by hekai on 16/5/24.
 */
public class SortCofingIdExists {
    public static boolean filesExists() {
        try {
            File f = new File("/data/user/0/com.keiss.listthings/files/SortCofingId");
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }
}