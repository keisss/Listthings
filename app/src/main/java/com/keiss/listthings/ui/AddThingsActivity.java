package com.keiss.listthings.ui;

import android.app.AlertDialog;
import android.content.ContentValues;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.keiss.listthings.R;
import com.keiss.listthings.cofing.SetSortThings;
import com.keiss.listthings.db.ThingsData;
import com.keiss.listthings.util.ContextGetter;
import com.keiss.listthings.util.DeleteFiles;
import com.keiss.listthings.util.LoadSortCofingID;
import com.keiss.listthings.util.ScareImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddThingsActivity extends AppCompatActivity {

    private FloatingActionButton fab_button_done;
    private TextInputEditText editText_things_title;
    private EditText editText_things_content;
    SQLiteDatabase db= ThingsData.getInstance(ContextGetter.getInstance()).getWritableDatabase();
    private ContentValues values;
    private RadioGroup things_group;
    private  int things_grade_num;
    private Spinner things_class;
    private RecyclerView recyclerView;
    private long ThingsCreateDateLong;
    private String ThingsCreateDateText;
    private String getSortId;
    private Spinner SetThingsClassSpinner;
    private int things_class_num;
    private Toolbar toolbar;
    private String ShareContentText;
    private String ShareClass;
    private String ShareGrade;
    private ImageButton ViewSavedPic;
    private Button DismissPic;
    private boolean IfWriteThingsToDB =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_add_things);

        //toolbar以及toolbar上按钮
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        //关闭按钮
        final Drawable cross = getResources().getDrawable(R.drawable.ic_clear_white_24px);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(cross );
        }


        //获取id
        fab_button_done = (FloatingActionButton) findViewById(R.id.fab_button_done);
        editText_things_content= (EditText) findViewById(R.id.add_things_content);
        editText_things_title = (TextInputEditText) findViewById(R.id.add_things_title);
        things_group = (RadioGroup) findViewById(R.id.things_group);
        SetThingsClassSpinner  = (Spinner) findViewById(R.id.set_things_class_spinner);
        ViewSavedPic = (ImageButton) findViewById(R.id.viewSavedPic);



        //获取创建时间
        SimpleDateFormat formatDateInt = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        ThingsCreateDateLong = Long.parseLong(formatDateInt.format(curDate));
        SimpleDateFormat formatDateText = new SimpleDateFormat("MM-dd");
        ThingsCreateDateText= formatDateText.format(curDate);
        values = new ContentValues();


        //图片查看
        if (new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + ".jpg"
        ).exists()||new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + "gallery.jpg"
        ).exists() )  {
                ViewSavedPic.setVisibility(View.VISIBLE);}
        ViewSavedPic.setOnClickListener(OnclickLis);






        //radioGroup相关实现代码
        things_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.radioButton_high)
                    things_grade_num=3;
                else if(checkedId == R.id.radioButton_normal)
                    things_grade_num = 2;
                else if (checkedId == R.id.radioButton_low)
                    things_grade_num = 1;
            }
        });

        //spinner实现
        String[] thingsClassCon = {"工作","生活","学习","其他"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,thingsClassCon);
        SetThingsClassSpinner.setAdapter(adapter);
        SetThingsClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        things_class_num = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //添加标题
        final TextInputLayout inputLayout = (TextInputLayout) findViewById(R.id.titleText);
        inputLayout.setHint("标题");
        inputLayout.setHintTextAppearance(R.style.EditTextHintAppearance);


        //确认添加FAB
        fab_button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db= ThingsData.getInstance(ContextGetter.getInstance()).getWritableDatabase();

                Cursor cursor = db.rawQuery("select * from things",null);
                int count =cursor.getCount();
                recyclerView = (RecyclerView) findViewById(R.id.listthings_mode_thingsList);

                if (! TextUtils.isEmpty(editText_things_title.getText().toString().trim())){
                    values.put( "things_title",editText_things_title.getText().toString());
                    values.put("things_content",editText_things_content.getText().toString());
                    values.put("things_grade",things_grade_num);
                    values.put("position",count );
                    values.put("things_create_date_int",ThingsCreateDateLong);
                    values.put("things_create_date_text",ThingsCreateDateText);

                    values.put("things_modify_date_int","0");
                    values.put("things_modify_date_text","0");
                    values.put("things_class",things_class_num);
                db.insert("things",null,values);
                    IfWriteThingsToDB = true;
                    getSortId= LoadSortCofingID.load();
                    Log.d("add",getSortId);
                    if (getSortId.equals("1")){
                        SetSortThings.initDataSOrtInCreateTime();
                    }else if (getSortId.equals("2")){
                        SetSortThings.initDataSOrtInModifyTime();
                    }else if (getSortId.equals("3")){

                        SetSortThings.initDataSOrtInThingsGrade();
                    }

                Intent intent = new Intent(AddThingsActivity.this,MainActivity.class);
                startActivity(intent);}
                else{
                    Toast.makeText(AddThingsActivity.this, "请输入标题" , Toast.LENGTH_SHORT).show();
                }
            cursor.close();
            onStop();}
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSortId= LoadSortCofingID.load();

        if (getSortId.equals("1")){
            SetSortThings.initDataSOrtInCreateTime();
        }else if (getSortId.equals("2")){
            SetSortThings.initDataSOrtInModifyTime();
        }else if (getSortId.equals("3")){

            SetSortThings.initDataSOrtInThingsGrade();
        }
        if (!IfWriteThingsToDB){
            if (new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"),ThingsCreateDateLong+".jpg"
            ).exists()){
            DeleteFiles.deleteFile(new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"),ThingsCreateDateLong+".jpg"
            ));}
            if (new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + "gallery.jpg").exists()){
                DeleteFiles.deleteFile(new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + "gallery.jpg"));
            }
            if (new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + "draw.jpg").exists()){
                DeleteFiles.deleteFile(new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + "draw.jpg"));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: onBackPressed();
                break;


        }return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == 1 ||requestCode ==3) {

                    ViewSavedPic.setVisibility(View.VISIBLE);

                }

                if (requestCode == 2) {
                    ViewSavedPic.setVisibility(View.VISIBLE);
                    Uri uri = data.getData();

                    File saveFromAlbum = new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + "gallery.jpg");
                    Bitmap image;
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(saveFromAlbum);
                        image = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                        if (image !=null){
                            image.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        }


    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.action_edit:
                    AlertDialog.Builder builderedit = new AlertDialog.Builder(AddThingsActivity.this);
                    builderedit.setTitle("添加图片来自于");
                    final String[] EditWith = {"图库","拍照","随手画"};
                    builderedit.setItems(EditWith,new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which){
                            switch (which){
                                case 0 :
                                    Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                                    getAlbum.setType("image/*");
                                    getAlbum.putExtra("return-data", true);
                                    startActivityForResult(getAlbum, 2);

                                    break;
                                case 1 :
                                    File saveFromCamera = new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"),ThingsCreateDateLong+".jpg"
                                    );
                                    Intent cameraIntent = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);

                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(saveFromCamera));
                                    startActivityForResult(cameraIntent,
                                            1);

                                    break;
                                case 2 :
                                   Intent intent = new Intent(AddThingsActivity.this,DrawYourPic.class);
                                    intent.putExtra("ThingsCreateOrModifyDate",ThingsCreateDateLong);
                                    startActivityForResult(intent,3);


                                    break;
                            }
                        }
                    });
                    builderedit.show();
                    break;
                case R.id.action_share:
                {switch (things_grade_num){
                    case 0 :ShareGrade = "!";break;
                    case 1 : ShareGrade= "!";break;
                    case 2 : ShareGrade= "!!";break;
                    case 3 : ShareGrade = "!!!";break;
                }
                    switch (things_class_num){
                        case 0 : ShareClass = "工作";break;
                        case 1 : ShareClass = "生活";break;
                        case 2 : ShareClass = "学习";break;
                        case 3 : ShareClass = "其他";break;
                    }
                    ShareContentText = "标题:"+ editText_things_title.getText().toString().trim()+"\n优先级:"+ShareGrade+"\n类型:"+ShareClass+"\n内容:"+ editText_things_content.getText().toString();

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddThingsActivity.this);
                    builder.setTitle("分享为");
                    final String[] ShareWith = {"文字","生成图片"};
                    builder.setItems(ShareWith,new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface,int which){
                            switch (which){
                                case 0 :
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("text/plain");
                                    intent.putExtra(Intent.EXTRA_TEXT, ShareContentText);

                                    Intent chooserIntent = Intent.createChooser(intent, "选择要分享到的应用");
                                    startActivity(chooserIntent);
                                    break;
                                case 1 :          break;

                            }
                        }
                    });builder.show();
                }break;
            }
            return true;
        }
    };

   private View.OnClickListener OnclickLis = new View.OnClickListener(){
       @Override
       public void onClick(View v) {
           AlertDialog.Builder builder = new AlertDialog.Builder(AddThingsActivity.this);
           final String[] SortWith = {"来自图库","来自相机","来自随手画"};
           builder.setItems(SortWith,new DialogInterface.OnClickListener(){

               @Override
               public void onClick(DialogInterface dialogInterface,int which){
                   switch (which){
                       case 0 :
                           if (!new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"),ThingsCreateDateLong+"gallery.jpg"
                           ).exists()){
                               LayoutInflater inflaterFromGalley = LayoutInflater.from(AddThingsActivity.this);
                               final View imgViewFromGalley = inflaterFromGalley.inflate(R.layout.none_of_pic, null);
                               final AlertDialog dialogGalley = new AlertDialog.Builder(AddThingsActivity.this).create();
                               dialogGalley.setView(imgViewFromGalley);
                               dialogGalley.show();
                           }else {
                               LayoutInflater inflaterFromGalley = LayoutInflater.from(AddThingsActivity.this);
                               final View imgViewFromGalley = inflaterFromGalley.inflate(R.layout.view_saved_pic_big, null);
                               final AlertDialog dialogGalley = new AlertDialog.Builder(AddThingsActivity.this).create();
                               final ImageView imgFromGalley = (ImageView) imgViewFromGalley.findViewById(R.id.large_image);
                               imgFromGalley.setImageDrawable(ScareImage.setScareImage(String.valueOf(ContextGetter.getInstance().getExternalFilesDir("things_pic"))+
                                       "/"+ThingsCreateDateLong+"gallery.jpg"));
                               dialogGalley.setView(imgViewFromGalley);
                               dialogGalley.show();
                               imgViewFromGalley.setOnClickListener(new View.OnClickListener() {
                                   public void onClick(View paramView) {
                                       dialogGalley.cancel();}
                               });
                               imgViewFromGalley.setLongClickable(true);
                               imgViewFromGalley.setOnLongClickListener(new View.OnLongClickListener() {
                                   @Override
                                   public boolean onLongClick(View v) {
                                       imgViewFromGalley.setAlpha(0.6f);
                                       DismissPic = (Button) imgViewFromGalley.findViewById(R.id.dismiss_pic);
                                       DismissPic.setVisibility(View.VISIBLE);
                                       DismissPic.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               if (new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"),ThingsCreateDateLong+"gallery.jpg"
                                               ).exists()){
                                                   DeleteFiles.deleteFile(new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + "gallery.jpg"
                                                   ));}
                                               if (!new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + ".jpg"
                                               ).exists()&&!new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + "draw.jpg"
                                               ).exists()){
                                                   ViewSavedPic.setVisibility(View.INVISIBLE);}
                                               dialogGalley.cancel();}});
                                       return true;}});}
                           break;
                       case 1 :
                           if (!new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"),ThingsCreateDateLong+".jpg"
                           ).exists()){
                               LayoutInflater inflaterFromCamera = LayoutInflater.from(AddThingsActivity.this);
                               final View imgViewFromCamera = inflaterFromCamera.inflate(R.layout.none_of_pic, null);
                               final AlertDialog dialogGalley = new AlertDialog.Builder(AddThingsActivity.this).create();
                               dialogGalley.setView(imgViewFromCamera);
                               dialogGalley.show();
                           }else {
                               LayoutInflater inflaterFromCamera = LayoutInflater.from(AddThingsActivity.this);
                               final View imgViewFromCamra = inflaterFromCamera.inflate(R.layout.view_saved_pic_big, null);
                               final AlertDialog dialogGalley = new AlertDialog.Builder(AddThingsActivity.this).create();
                               final ImageView imgFromCamera = (ImageView) imgViewFromCamra.findViewById(R.id.large_image);

                               imgFromCamera.setImageDrawable(ScareImage.setScareImage(String.valueOf(ContextGetter.getInstance().getExternalFilesDir("things_pic"))+
                                       "/"+ThingsCreateDateLong+".jpg"));
                               dialogGalley.setView(imgViewFromCamra);
                               dialogGalley.show();
                               imgViewFromCamra.setOnClickListener(new View.OnClickListener() {
                                   public void onClick(View paramView) {
                                       dialogGalley.cancel();
                                   }
                               });
                               imgViewFromCamra.setLongClickable(true);
                               imgViewFromCamra.setOnLongClickListener(new View.OnLongClickListener() {
                                   @Override
                                   public boolean onLongClick(View v) {
                                       imgViewFromCamra.setAlpha(0.6f);
                                       DismissPic = (Button) imgViewFromCamra.findViewById(R.id.dismiss_pic);
                                       DismissPic.setVisibility(View.VISIBLE);
                                       DismissPic.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               if (new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"),ThingsCreateDateLong+".jpg"
                                               ).exists()) {
                                                   DeleteFiles.deleteFile(new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + ".jpg"
                                                   ));
                                               }


                                                   if (!new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + "gallery.jpg"
                                                   ).exists()&&!new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + "draw.jpg"
                                                   ).exists()){
                                                       ViewSavedPic.setVisibility(View.INVISIBLE);}
                                               dialogGalley.cancel();}
                                       });
                                       return true;
                                   }});}break;
                       case 2 :
                           if (!new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"),ThingsCreateDateLong+"draw.jpg"
                           ).exists()){
                               LayoutInflater inflaterFromCamera = LayoutInflater.from(AddThingsActivity.this);
                               final View imgViewFromCamera = inflaterFromCamera.inflate(R.layout.none_of_pic, null);
                               final AlertDialog dialogGalley = new AlertDialog.Builder(AddThingsActivity.this).create();
                               dialogGalley.setView(imgViewFromCamera);
                               dialogGalley.show();}
                           else {
                               LayoutInflater inflaterFromDraw = LayoutInflater.from(AddThingsActivity.this);
                               final View imgViewFromCamra = inflaterFromDraw.inflate(R.layout.view_saved_pic_big, null);
                               final AlertDialog dialogGalley = new AlertDialog.Builder(AddThingsActivity.this).create();
                               final ImageView imgFromCamera = (ImageView) imgViewFromCamra.findViewById(R.id.large_image);

                               imgFromCamera.setImageDrawable(ScareImage.setScareImage(String.valueOf(ContextGetter.getInstance().getExternalFilesDir("things_pic"))+
                                       "/"+ThingsCreateDateLong+"draw.jpg"));
                               dialogGalley.setView(imgViewFromCamra);
                               dialogGalley.show();
                               imgViewFromCamra.setOnClickListener(new View.OnClickListener() {
                                   public void onClick(View paramView) {
                                       dialogGalley.cancel();
                                   }
                               });
                               imgViewFromCamra.setLongClickable(true);
                               imgViewFromCamra.setOnLongClickListener(new View.OnLongClickListener() {
                                   @Override
                                   public boolean onLongClick(View v) {
                                       imgViewFromCamra.setAlpha(0.6f);
                                       DismissPic = (Button) imgViewFromCamra.findViewById(R.id.dismiss_pic);
                                       DismissPic.setVisibility(View.VISIBLE);
                                       DismissPic.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               if (new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"),ThingsCreateDateLong+"draw.jpg"
                                               ).exists()) {
                                                   DeleteFiles.deleteFile(new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + "draw.jpg"
                                                   ));
                                               }


                                               if (!new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + "gallery.jpg"
                                               ).exists()&&!new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"), ThingsCreateDateLong + ".jpg"
                                               ).exists()){
                                                   ViewSavedPic.setVisibility(View.INVISIBLE);}
                                               dialogGalley.cancel();}
                                       });
                                       return true;
                                   }});}



                           break;}}});
           builder.show();}};







}



