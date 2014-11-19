package com.elight.teaching.custom.xgrid;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by dawn on 2014/10/22.
 */
public class DragGrid extends GridView{

    private int dragPosition;
    private int dropPosition;

    private ImageView dragImageView;

    private WindowManager windowManager;
    private WindowManager.LayoutParams windowParams;

    private int itemHeight, itemWidth;

    public DragGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragGrid(Context context) {
        super(context);
    }

    boolean flag = false;

    public void setLongFlag(boolean temp) {
        flag = temp;
    }

    public boolean setOnItemLongClickListener(final MotionEvent ev) {
        this.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {

                int x = (int) ev.getX();
                int y = (int) ev.getY();
                dragPosition = dropPosition = arg2;
                System.out.println(dragPosition + "--" + arg2);
                if (dragPosition == AdapterView.INVALID_POSITION) {

                }
                ViewGroup itemView = (ViewGroup) getChildAt(dragPosition
                        - getFirstVisiblePosition());
                itemHeight = itemView.getHeight();
                itemWidth = itemView.getWidth();
                itemView.destroyDrawingCache();
                itemView.setDrawingCacheEnabled(true);
                Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
                startDrag(bm, x, y);
                return true;
            };
        });
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            return setOnItemLongClickListener(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void startDrag(Bitmap bm, int x, int y) {
        stopDrag();
        windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP | Gravity.LEFT;
        windowParams.x = x - itemWidth / 2;
        windowParams.y = y - itemHeight / 2;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;

        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(bm);
        windowManager = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);// "window"
        windowManager.addView(iv, windowParams);
        dragImageView = iv;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (dragImageView != null
                && dragPosition != AdapterView.INVALID_POSITION) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    onDrag(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    stopDrag();
                    onDrop(x, y);
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    private void onDrag(int x, int y) {
        if (dragImageView != null) {
            windowParams.alpha = 0.5f;
            windowParams.x = x - itemWidth / 2;
            windowParams.y = y - itemHeight / 2;
            windowManager.updateViewLayout(dragImageView, windowParams);
        }
    }

    private void onDrop(int x, int y) {
        int tempPosition = pointToPosition(x, y);
        if (tempPosition != AdapterView.INVALID_POSITION) {
            dropPosition = tempPosition;
        }
        if (dropPosition != dragPosition) {
            System.out.println("dragPosition: " + dragPosition
                    + " dropPosition: " + dropPosition);
            DateAdapter adapter = (DateAdapter) this.getAdapter();
            adapter.exchange(dragPosition, dropPosition);
        }
    }

    private void stopDrag() {
        if (dragImageView != null) {
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }
    }

}
