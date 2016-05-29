package com.keiss.listthings.entity;

/**
 * Created by hekai on 16/5/12.
 */
public class item {
    private String title;
    private String content;

    public item(String title,String content){
    this.title = title;
    this.content = content;
    }
    public String  getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;

    }
}
