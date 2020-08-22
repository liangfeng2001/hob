package com.ekek.commonmodule.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.contrarywind.view.WheelView;

public class WheelViewEx extends WheelView {

    private float lastTotalScrollY = 0;
    private ItemScrollListener itemScrollListener;
    private CentralItemClickListener centralItemClickListener;
    private int scaledTouchSlop;
    private boolean flagMayPress = false;
    private float downY = 0;
    private float currY = 0;

    public WheelViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public interface ItemScrollListener {
        void OnItemScroll(float itemHeight, float totalScrollY);
    }
    public interface CentralItemClickListener {
        void OnCentralItemClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        currY = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = currY;
                flagMayPress = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float spanY = Math.abs(downY - currY);
                if (flagMayPress && spanY >= scaledTouchSlop) {
                    flagMayPress = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (flagMayPress) {
                    float itemHeight = getItemHeight();
                    int centerY = getHeight() / 2;
                    float spanToCenter = Math.abs(currY - centerY);
                    if (spanToCenter <= itemHeight / 2.0) {
                        OnCentralItemClick();
                        return false;
                    }
                }
                break;
        }

        super.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float itemHeight = getItemHeight();
        float totalScrollY = getTotalScrollY();

        float amount = Math.abs(totalScrollY - lastTotalScrollY);
        if (amount >= itemHeight) {
            int x = (int)(amount / itemHeight);
            if (totalScrollY > lastTotalScrollY) {
                lastTotalScrollY += itemHeight * x;
            } else {
                lastTotalScrollY -= itemHeight * x;
            }
            OnItemScroll(itemHeight, totalScrollY);
        }
    }

    private void OnItemScroll(float itemHeight, float totalScrollY) {
        if (itemScrollListener != null) {
            itemScrollListener.OnItemScroll(itemHeight, totalScrollY);
        }
    }
    private void OnCentralItemClick() {
        if (centralItemClickListener != null) {
            centralItemClickListener.OnCentralItemClick();
        }
    }

    public void setItemScrollListener(ItemScrollListener itemScrollListener) {
        this.itemScrollListener = itemScrollListener;
    }
    public void setCentralItemClickListener(CentralItemClickListener centralItemClickListener) {
        this.centralItemClickListener = centralItemClickListener;
    }
}