package com.ekek.viewmodule.common;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.viewmodule.hob.BaseCircleCookerView;

public class CataDrawLayout extends DrawerLayout {

    // Fields
    private boolean startFlag = false;

    // Constructors
    public CataDrawLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Override functions
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                final float x = ev.getX();
                final float y = ev.getY();
                final View touchedView = findTopChildUnder((int) x, (int) y);
                if (touchedView != null && isContentView(touchedView)
                        && this.isDrawerOpen(GravityCompat.END) && startFlag) {
                    // 原生的 DrawerLayout 在侧边栏弹出时，要求用户必须首先处理侧边栏事宜
                    // 因而造成内容区域无法操作，只有当用户点击内容区域导致侧边栏收回后，才
                    // 可以对内容区域进行操作
                    // 这里的代码修改了这种行为，允许在弹出侧边栏时，仍然可以对内容区域进行
                    // 操作
                    return false;
                }
                break;
        }
        startFlag = true;
        return super.onInterceptTouchEvent(ev);
    }

    // Private functions
    /**
     * 判断点击位置是否位于相应的View内
     * @param x
     * @param y
     * @return
     */
    private View findTopChildUnder(int x, int y) {
        final int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (x >= child.getLeft() && x < child.getRight() &&
                    y >= child.getTop() && y < child.getBottom()) {
                return child;
            }
        }
        return null;
    }
    /**
     * 判断点击触摸点的View是否是ContentView(即是主页面的View)
     * @param child
     * @return
     */
    private boolean isContentView(View child) {
        return ((LayoutParams) child.getLayoutParams()).gravity == Gravity.NO_GRAVITY;
    }

    private void processClickSound(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            float x = ev.getX();
            float y = ev.getY();
            final int childCount = getChildCount();
            LogUtil.d("Enter:: processClickSound-->" + childCount);
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = getChildAt(i);
                int count = ((ViewGroup) child).getChildCount();
                LogUtil.d("Enter:: processClickSound---count-->" + count);
                for (int j = 0; j < count; j++) {
                    View view = ((ViewGroup) child).getChildAt(i);
                    Object object = view.getTag();
                    LogUtil.d("Enter:: processClickSound---->" + view.isSoundEffectsEnabled());
                //    LogUtil.d("Enter:: processClickSound---tag--->" + object.toString() + "----effect--->" + view.isSoundEffectsEnabled());
                    //LogUtil.d("Enter:: processClickSound---tag--->" + object.toString());
                }

                if (child instanceof BaseCircleCookerView) {
                    LogUtil.d("Enter:: processClickSound--->X-Y-->( " + child.getX() + "--" + child.getY());
                }
            }

        }

    }
}
