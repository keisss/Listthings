package com.keiss.listthings.adapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.keiss.listthings.R;
import android.widget.ImageView;
import android.widget.TextView;
import com.keiss.listthings.db.ThingsData;
import com.keiss.listthings.util.ContextGetter;
import com.keiss.listthings.util.DeleteFiles;
import com.keiss.listthings.util.ThingsItemClickListener;
import com.keiss.listthings.util.ThingsItemLongClickListener;
import com.keiss.listthings.util.onSwipedListener;
import com.keiss.listthings.util.DropPositionAndReBuild;

import org.w3c.dom.Text;

import java.io.File;


/**
 * Created by hekai on 16/5/11.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>   implements onSwipedListener {
    private String[] title ;
    private String[] content = null;
    private int[] things_grade;
    private int[] things_class;
    private String[] things_create_time;
    private String[] things_modify_time;
    SQLiteDatabase db=ThingsData.getInstance(ContextGetter.getInstance()).getWritableDatabase();
    private ThingsItemClickListener thingsItemClickListener;
    private ThingsItemLongClickListener thingsItemLongClickListener;
    private Long ThingsCreateTime;
    private Long ThingsModifyTime;
    private File ThingsCreateTimePic;
    private File THingsModifyTImePic;
    private File ThingsCreateTimeGalleryPic;
    private File ThingsModifyTimeGalleryPic;
    private File ThingsCreateTimeDrawPic;
    private File ThingsModifyTimeDrawPic;

    @Override
    public void onItemDismiss(int position){
        Cursor cursor = db.rawQuery("select * from things where position = ?",new String[]{String.valueOf(position)});
        while (cursor.moveToNext()){
           ThingsCreateTime = cursor.getLong(6);
            ThingsModifyTime = cursor.getLong(8);
            ThingsCreateTimePic = new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"),ThingsCreateTime+".jpg");
            THingsModifyTImePic = new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"),ThingsModifyTime+".jpg");
            ThingsCreateTimeGalleryPic = new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"),ThingsCreateTime+"gallery.jpg");
            ThingsModifyTimeGalleryPic = new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"),ThingsModifyTime+"gallery.jpg");
            ThingsCreateTimeDrawPic = new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"),ThingsCreateTime+"draw.jpg");
            ThingsModifyTimeDrawPic = new File(ContextGetter.getInstance().getExternalFilesDir("things_pic"),ThingsModifyTime+"draw.jpg");
            if (ThingsCreateTimePic.exists())
            {DeleteFiles.deleteFile(ThingsCreateTimePic);}
            if (THingsModifyTImePic.exists()){
                DeleteFiles.deleteFile(THingsModifyTImePic);
            }
            if (ThingsModifyTimeGalleryPic.exists()){
                DeleteFiles.deleteFile(ThingsModifyTimeGalleryPic);
            }
            if (ThingsCreateTimeGalleryPic.exists()){
                DeleteFiles.deleteFile(ThingsCreateTimeGalleryPic);
            }
            if (ThingsCreateTimeDrawPic.exists()){
                DeleteFiles.deleteFile(ThingsCreateTimeDrawPic);
            }
            if (ThingsModifyTimeDrawPic.exists()){
                DeleteFiles.deleteFile(ThingsModifyTimeDrawPic);

            }

        }

        db.delete("things","position == ?",new String[]{String.valueOf(position)});
        DropPositionAndReBuild.DropAndRebuild();
        notifyItemRemoved(position);

    }


    public RecycleViewAdapter(String[] title,String[] content,int[] things_grade,int[] things_class,String[] things_create_time,String[] things_modify_time){

        this.content = content;
        this.title = title;
        this.things_grade = things_grade;
        this.things_class = things_class;
        this.things_create_time = things_create_time;
        this.things_modify_time = things_modify_time;
    }



    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_style,parent,false);

        MyViewHolder viewHolder = new MyViewHolder(view,thingsItemClickListener,thingsItemLongClickListener);
        return viewHolder;
    }

    public void setOnItemClickListener(ThingsItemClickListener listener){
        this.thingsItemClickListener = listener;
    }


    public void setOnItemLongClickListener(ThingsItemLongClickListener listener){
        this.thingsItemLongClickListener = listener;
    }



     @Override
    public void onBindViewHolder( MyViewHolder holder,int position){
         holder.contentView.setText(content[position]);
         holder.titleView.setText(title[position]);
         switch (things_class[position]){
             case 0 : holder.classView.setText("工作");break;
             case 1 : holder.classView.setText("生活");break;
             case 2 : holder.classView.setText("学习");break;
             case 3 : holder.classView.setText("其他");break;
         }
       switch (things_grade[position]){
           case 3 : holder.thingsGradeView.setImageResource(R.drawable.radiobutton_item_grade_high);break;
           case 2 : holder.thingsGradeView.setImageResource(R.drawable.radiobutton_item_grade_normal);break;
           case 1: holder.thingsGradeView.setImageResource(R.drawable.radiobutton_item_grade_low);break;
           case 0 : holder.thingsGradeView.setImageResource(R.drawable.radiobutton_item_grade_low);break;
       }
         if (things_modify_time[position].equals("0")){
             holder.thingsTime.setText(things_create_time[position]);
         }else {
             holder.thingsTime.setText(things_modify_time[position]);
         }

     }

    @Override
    public int getItemCount() {

        Cursor cursor = db.rawQuery("select * from things",null);
        int count =cursor.getCount();
        cursor.close();
        return count;


    }



     public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView titleView;
        TextView contentView;
         TextView classView;
         ImageView thingsGradeView;
         TextView thingsTime;
        private ThingsItemClickListener thingsItemClickListener;
         private ThingsItemLongClickListener thingsItemLongClickListener;

        public  MyViewHolder(View itemView,ThingsItemClickListener listener,ThingsItemLongClickListener longClickListener){

            super(itemView);

            titleView =(TextView) itemView.findViewById(R.id.item_title);
            contentView = (TextView)itemView.findViewById(R.id.item_content);
            thingsGradeView =(ImageView) itemView.findViewById(R.id.item_grade);
            classView = (TextView) itemView.findViewById(R.id.item_class);
            thingsTime = (TextView)itemView.findViewById(R.id.item_time);
            this.thingsItemClickListener= listener;
            this.thingsItemLongClickListener = longClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

         //点击的监听
         @Override
         public void onClick(View V){
             if (thingsItemClickListener !=null){
                 thingsItemClickListener.onItemClick(V,getPosition());
             }
         }


         //长按的监听
         @Override
         public boolean onLongClick(View arg0){
             if (thingsItemLongClickListener !=null){
                 thingsItemLongClickListener.onItemLongClick(arg0,getPosition());
             }return true;
         }





    }






}