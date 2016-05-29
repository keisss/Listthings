package com.keiss.listthings.util;

import android.media.browse.MediaBrowser;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.keiss.listthings.adapter.RecycleViewAdapter;

/**
 * Created by hekai on 16/5/17.
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
   //设置侧滑方向
    @Override
    public int getMovementFlags(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder){
        final  int dragFlags = 0;
        final int swipeFlags = ItemTouchHelper.START|ItemTouchHelper.END;
        return makeMovementFlags(dragFlags,swipeFlags);
    }



    //拖动item
    @Override
    public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target){
        return false;
    }

    //侧滑item
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder,int direction){
        Adapter.onItemDismiss(viewHolder.getAdapterPosition());


    }

    private  onSwipedListener Adapter;
    public  ItemTouchHelperCallback(onSwipedListener listener){
        Adapter =listener;
    }
}
