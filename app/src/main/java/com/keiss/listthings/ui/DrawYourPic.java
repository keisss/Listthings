package com.keiss.listthings.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.keiss.listthings.R;
import com.keiss.listthings.util.ContextGetter;
import com.keiss.listthings.util.InitThingsData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DrawYourPic extends AppCompatActivity  {
    private ImageView image;

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private float downX;
    private float downY;
    private Toolbar toolbar;
    private long ThingsCreateOrModifyTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_draw_pic);
        initView();


        Intent intent = getIntent();

        ThingsCreateOrModifyTime = intent.getLongExtra("ThingsCreateOrModifyDate",0);

        //toolbar以及toolbar上按钮
        toolbar = (Toolbar) findViewById(R.id.base_toolbar);

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        toolbar.setTitle("随手画");

    }



    private void initView() {
        image=(ImageView) findViewById(R.id.image);

        image.setOnTouchListener(new myOnTouchListener());

    }





    class myOnTouchListener implements OnTouchListener{


        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            int action = arg1.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:

                    initBitmap();
                    downX=arg1.getX();
                    downY=arg1.getY();
                    break;
                case MotionEvent.ACTION_MOVE:

                    float moveX= arg1.getX();
                    float moveY=arg1.getY();
                    canvas.drawLine(downX, downY, moveX, moveY, paint);
                    image.setImageBitmap(bitmap);
                    downX=moveX;
                    downY=moveY;
                    break;
                case MotionEvent.ACTION_UP:

                    break;
                default:
                    break;
            }
            return true;
        }


        private void  initBitmap() {
            if(bitmap==null){
                bitmap=Bitmap.createBitmap(image.getWidth(), image.getHeight(), Config.ARGB_8888);

                canvas=new Canvas(bitmap);

                paint=new Paint();
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(5);
                canvas.drawColor(Color.YELLOW);
                image.setImageBitmap(bitmap);
            }
        }
    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.draw_your_pic_menu, menu);

        return true;
    }


    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.save_your_pic :
                    BitmapDrawable bmpDrawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bmp = bmpDrawable.getBitmap();

                    File saveFromDraw = new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateOrModifyTime + "draw.jpg");

                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(saveFromDraw);

                        if (bmp !=null){
                            bmp.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                    }Intent intent = new Intent();
                    DrawYourPic.this.setResult(RESULT_OK,intent);
                    DrawYourPic.this.finish();
                    break;
                case R.id.back_and_unsaved :
                    onBackPressed(); break;



            }

         return true;
        }
    };
}