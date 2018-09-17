package com.tmall.wireless.tangram.example.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * Created by Bill56 on 2017/12/20.
 */

public class MyRecyclerView extends RecyclerView {

    private ViewGroup parent;
    private boolean disallowInterceptTouchEvent = false;

    private int downX;

    private int downY;

    private int mTouchSlop;

    public MyRecyclerView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Log.i("lhp", "MyGridView+dispatchTouchEvent:" + disallowIntercept);
        disallowInterceptTouchEvent = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    public void setNestParent(ViewGroup parent) {
        this.parent = parent;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowInterceptTouchEvent);
        }
        Log.i("lhp", "MyGridView+onTouchEvent:");
        return super.onTouchEvent(ev);
    }

    @Override

    public boolean onInterceptTouchEvent(MotionEvent e) {

        int action = e.getAction();

        switch (action) {

            case MotionEvent.ACTION_DOWN:

                downX = (int) e.getRawX();

                downY = (int) e.getRawY();

                break;

            case MotionEvent.ACTION_MOVE:

                int moveY = (int) e.getRawY();

                if (Math.abs(moveY - downY) > mTouchSlop) {

                    return true;

                }

        }
        disallowInterceptTouchEvent = false;
        return super.onInterceptTouchEvent(e);

    }
}
