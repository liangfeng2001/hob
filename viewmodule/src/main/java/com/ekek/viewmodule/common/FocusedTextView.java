package com.ekek.viewmodule.common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ekek.commonmodule.GlobalVars;

public class FocusedTextView extends AppCompatTextView {

    public FocusedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        return result;
    }

    @Override
    public boolean performClick() {
        boolean result = super.performClick();
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        switch (GlobalVars.getInstance().getCurrentLocale().toString().toLowerCase()) {
            case "hu":
                setMeasuredDimension(width + 10, height);
                break;
        }
    }
}
