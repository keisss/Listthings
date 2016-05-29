package com.keiss.listthings.util;

import android.support.v7.widget.RecyclerView;

/**
 * Created by hekai on 16/5/20.
 */
public abstract class FabRecyclerScrollViewListener extends RecyclerView.OnScrollListener {
    boolean isVisible = true;
    int scrollLen = 0;
    static final float MIN = 20;

    @Override
    public void onScrolled(RecyclerView recyclerView,int dx,int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (!isVisible && recyclerView.getChildCount() < 10) {
            show();
        } else {
            if (isVisible && scrollLen > MIN) {
                hide();
                scrollLen = 0;
                isVisible = false;
            } else if ((!isVisible && scrollLen < -MIN)) {
                show();
                scrollLen = 0;
                isVisible = true;
            }
            if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {

                scrollLen += dy;
            }
        }
    }

    public  abstract void show();
    public  abstract void hide();
}
