package com.keiss.listthings.ui;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.keiss.listthings.R;
import com.keiss.listthings.adapter.RecycleViewAdapter;
import com.keiss.listthings.adapter.RecycleViewAdapterForToday;
import com.keiss.listthings.cofing.SetSortThings;
import com.keiss.listthings.util.FabRecyclerScrollViewListener;
import com.keiss.listthings.util.InitTodayThingsData;
import com.keiss.listthings.util.ItemTouchHelperCallback;
import com.keiss.listthings.util.InitThingsData;
import com.keiss.listthings.util.SaveSortCofingID;
import com.keiss.listthings.util.SortCofingIdExists;
import com.keiss.listthings.util.SpaceItemDecoration;
import com.keiss.listthings.util.ThingsItemClickListener;
import com.keiss.listthings.util.ThingsItemLongClickListener;
import com.keiss.listthings.util.onSwipedListener;

import java.io.BufferedWriter;
import java.io.FileOutputStream;

import static com.keiss.listthings.R.color.*;


/**
 * Created by hekai on 16/4/29.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ThingsItemClickListener, ThingsItemLongClickListener  {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    public RecyclerView thingsListView;
    private RecycleViewAdapter thingsAdapter;
    private String[] title =new String[100];
    private String[] content = new String[100];
    private int[] things_grade = new int[100];
    private int[] things_class = new int[100];
    private String[] things_create_time = new String[100];
    private String[] things_modify_time = new String[100];
    private  ItemTouchHelper mItemTouchHelper;
    private RecyclerView.OnScrollListener FabRecyclerScrollViewListener;
    private FloatingActionButton addFab;
    private static String SortCofingId ;
    private FileOutputStream outputStream = null;
    private BufferedWriter writer = null;
    private boolean IfSetTodayThingsView = false;
    private Toolbar toolbar;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_navigation_view);




        //toolbar和右侧菜单
          toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        toolbar.setTitle("所有日程");
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);


        if (!SortCofingIdExists.filesExists()){
            SaveSortCofingID.save(String.valueOf(2));
        }




        //recyclerview实现

        InitThingsData.initData(title,content,things_grade,things_class,things_create_time,things_modify_time);
        thingsListView = (RecyclerView) findViewById(R.id.listthings_mode_thingsList);
        thingsListView.setLayoutManager(new LinearLayoutManager(this));
        thingsAdapter = new RecycleViewAdapter(title,content,things_grade,things_class,things_create_time,things_modify_time);
        thingsListView.setAdapter(thingsAdapter);
        thingsAdapter.registerAdapterDataObserver(observer);
        observer.onChanged();
        thingsListView.setItemAnimator(new DefaultItemAnimator());
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        thingsListView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        thingsAdapter.setOnItemClickListener(this);
        thingsAdapter.setOnItemLongClickListener(this);
        FabRecyclerScrollViewListener = new FabRecyclerScrollViewListener() {
            @Override
            public void show() {
                addFab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                CoordinatorLayout.LayoutParams hd = (CoordinatorLayout.LayoutParams)addFab.getLayoutParams();
                int fabMar = hd.bottomMargin;
                addFab.animate().translationY(addFab.getHeight()+fabMar).setInterpolator(new AccelerateInterpolator(2.0f)).start();
            }
        };
        thingsListView.addOnScrollListener( FabRecyclerScrollViewListener);
if (thingsAdapter.getItemCount()!=0){
    thingsListView.setVisibility(View.VISIBLE);
}





        //滑动删除实现
        ItemTouchHelper.Callback callback =new ItemTouchHelperCallback(thingsAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(thingsListView);

        //添加FAB实现
         addFab = (FloatingActionButton)findViewById(R.id.fab_add_button);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddThingsActivity.class);
                startActivity(intent);
            }
        });

        //侧滑菜单实现navigationView
        navigationView = (NavigationView) findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        drawerLayout= (DrawerLayout)findViewById(R.id.naviationview_layout);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int optionId =menuItem.getItemId();
        if (optionId== R.id.right_about)
        {
            Intent intent = new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);
        }
        else if (optionId == R.id.right_sort){

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("选择排序方式");
            final String[] SortWith = {"创建时间","修改时间","优先级"};
            builder.setItems(SortWith,new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialogInterface,int which){
                    MainActivity.this.onPause();
                    switch (which){
                        case 0 :
                            SetSortThings.initDataSOrtInCreateTime();
                            SortCofingId = String.valueOf(1);break;
                        case 1 :
                            SetSortThings.initDataSOrtInModifyTime();
                            SortCofingId= String.valueOf(2);break;
                        case 2 :
                            SetSortThings.initDataSOrtInThingsGrade();
                            SortCofingId= String.valueOf(3);break;
                    }SaveSortCofingID.save(String.valueOf(SortCofingId));
                    MainActivity.this.onResume();
                }
            });builder.show();

            SaveSortCofingID.save(String.valueOf(SortCofingId));
        }
        return super.onOptionsItemSelected(menuItem);
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.today_things :
                    IfSetTodayThingsView = true;
                onResume();


                break;
            case R.id.all_things :
                IfSetTodayThingsView = false;
                onResume();
            case R.id.calendar_mode :

                break;

            case R.id.menu_about :
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);break;
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.naviationview_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onRestart(){
        super.onRestart();
        if (!IfSetTodayThingsView){
            toolbar.setTitle("所有日程");
        InitThingsData.initData(title,content,things_grade,things_class,things_create_time,things_modify_time);
        thingsListView.setAdapter(thingsAdapter);
        showEmptyView();
    }else if (IfSetTodayThingsView){
            toolbar.setTitle("今日日程");
           RecycleViewAdapterForToday thingsAdapterFromToday = new RecycleViewAdapterForToday(title,content,things_grade,things_class,things_create_time,things_modify_time);
            InitTodayThingsData.initData(title,content,things_grade,things_class,things_create_time,things_modify_time);
            thingsListView.setAdapter(thingsAdapterFromToday);
           showEmptyView();
            thingsAdapterFromToday.setOnItemClickListener(this);
            thingsAdapterFromToday.setOnItemLongClickListener(this);
        }
    }

    @Override
        public void onDestroy(){
            super.onDestroy();
            thingsListView.removeOnScrollListener(FabRecyclerScrollViewListener);
            SaveSortCofingID.save(String.valueOf(SortCofingId));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.naviationview_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public  void onResume(){
        super.onResume();
        if (!IfSetTodayThingsView) {
            toolbar.setTitle("所有日程");
            InitThingsData.initData(title, content, things_grade, things_class, things_create_time, things_modify_time);
            thingsListView.setAdapter(thingsAdapter);
            showEmptyView();
        }else if (IfSetTodayThingsView){
            toolbar.setTitle("今日日程");
            RecycleViewAdapterForToday thingsAdapterFromToday = new RecycleViewAdapterForToday(title,content,things_grade,things_class,things_create_time,things_modify_time);
            InitTodayThingsData.initData(title,content,things_grade,things_class,things_create_time,things_modify_time);
            thingsListView.setAdapter(thingsAdapterFromToday);
            showEmptyView();
            thingsAdapterFromToday.setOnItemClickListener(this);
            thingsAdapterFromToday.setOnItemLongClickListener(this);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this,UpdateThingsActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }


    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
        }
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            showEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            showEmptyView();
        }
    };
   public void showEmptyView(){
       emptyView =  findViewById(R.id.toDoEmptyView);
       RecyclerView.Adapter<?> adapter = thingsListView.getAdapter();
       if(adapter!=null && emptyView!=null){
           if(adapter.getItemCount()==0){
               emptyView.setVisibility(View.VISIBLE);
               thingsListView.setVisibility(View.GONE);
           }
           else{
               emptyView.setVisibility(View.GONE);
               thingsListView.setVisibility(View.VISIBLE);
           }
       }
   }
}








