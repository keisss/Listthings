package com.keiss.listthings.util;

import java.io.File;

/**
 * Created by hekai on 16/5/26.
 */
public class FileIfExist {

   public static boolean FileExists(String file){
    try{
        File f=new File(file);
        if(!f.exists()){
            return false;
        }

    }catch (Exception e) {
        // TODO: handle exception
        return false;
    }
    return true;
}}
