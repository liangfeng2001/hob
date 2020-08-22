package com.ekek.settingmodule.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.settingmodule.utils.ViewUtils;

import java.util.List;

public abstract class BaseDialog extends Dialog {

    // Fields
    protected View rootView;
    Context mContext;

    // Constructors
    public BaseDialog(@NonNull Context context) {
        super(context);
        mContext=context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = LayoutInflater.from(context).inflate(getLayoutResource(), null);
        setContentView(rootView);
        initAllViews();
        setCanceledOnTouchOutside(false);
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = getWidth();
        params.height = getHeight();
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initialize();
    }

    // Override functions
    @Override
    protected void onStart() {
        super.onStart();
        hideBottomUIMenu();
    }

    // Abstract functions
    protected abstract int getLayoutResource();
    protected abstract int getWidth();
    protected abstract int getHeight();

    // Protected functions
    protected void initialize() {

    }
    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {

        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    protected void initAllViews() {
        List<View> allChildrenViews = ViewUtils.getAllChildrenViews(rootView);
        for (View v: allChildrenViews) {
            v.setSoundEffectsEnabled(false);
            if (v instanceof TextView) {
                ((TextView)v).setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
            }
        }
    }
}
