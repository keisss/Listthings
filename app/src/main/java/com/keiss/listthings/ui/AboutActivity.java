package com.keiss.listthings.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.keiss.listthings.R;

/**
 * Created by hekai on 16/5/29.
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        toolbar.setTitle("关于");
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);




    }

}
