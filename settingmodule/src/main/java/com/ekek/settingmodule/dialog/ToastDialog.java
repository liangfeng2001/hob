package com.ekek.settingmodule.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.utils.ViewUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ToastDialog extends Dialog {

    // Constants
    public static final int WIDTH_LONG = 800;
    public static final int WIDTH_MEDIUM = 700;
    public static final int WIDTH_SHORT = 600;
    public static final int WIDTH_EXTRA_LONG = 1000;
    public static final int ANCHOR_DIRECTION_BELOW_START = 0;
    public static final int ANCHOR_DIRECTION_BELOW_END = 1;
    public static final int ANCHOR_DIRECTION_ABOVE_START = 2;
    public static final int ANCHOR_DIRECTION_ABOVE_END = 3;

    // Widgets
    protected View rootView;
    private TextView tvToastContent;
    Context context;
    private static ToastDialog toastDialog = null; // 为了能主动控制toast的消失

    // Constructors
    public ToastDialog(
            @NonNull Context context,
            int toastContent,
            int width,
            int gravity,
            int x,
            int y) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.settingmodule_dialog_toast, null);
        setContentView(rootView);
        setCanceledOnTouchOutside(true);

        initAllViews();
        initToastContent(toastContent, width, gravity, x, y);
    }


    // Override functions
    @Override
    protected void onStart() {
        super.onStart();
        hideBottomUIMenu();
    }

    // Public functions
    public static void showDialog(
            Context context,
            int toastContent,
            int width,
            View anchorView,
            int anchorDirection,
            int duration) {

        showDialog(context, toastContent, width, anchorView, anchorDirection,0 ,0, duration);
    }
    public static void showDialog(
            Context context,
            int toastContent,
            int width,
            View anchorView,
            int anchorDirection,
            int offsetX,
            int offsetY,
            int duration) {

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth  = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        int gravity = Gravity.TOP | Gravity.START;

        switch (anchorDirection) {
            case ANCHOR_DIRECTION_BELOW_START:
                gravity = Gravity.TOP | Gravity.START;
                y = y + anchorView.getHeight();
                break;
            case ANCHOR_DIRECTION_BELOW_END:
                gravity = Gravity.TOP | Gravity.END;
                x = screenWidth - x - anchorView.getWidth();
                y = y + anchorView.getHeight();
                break;
            case ANCHOR_DIRECTION_ABOVE_START:
                gravity = Gravity.BOTTOM | Gravity.START;
                y = screenHeight - y - anchorView.getHeight();
                break;
            case ANCHOR_DIRECTION_ABOVE_END:
                gravity = Gravity.BOTTOM | Gravity.END;
                x = screenWidth - x - anchorView.getWidth();
                y = screenHeight - y - anchorView.getHeight();
                break;
            default:
                break;
        }

        x += offsetX;
        y += offsetY;
        ToastDialog.showDialog(
                context,
                toastContent,
                width,
                gravity,
                x,
                y,
                duration);
    }

    public static void showDialog(
            Context context,
            int toastContent,
            int width,
            int gravity,
            int x,
            int y,
            int duration) {
        toastDialog = new ToastDialog(
                context,
                toastContent,
                width,
                gravity,
                x,
                y);
        toastDialog.tvToastContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastDialog.dismiss();
            }
        });
        toastDialog.show();
        if (duration > 0) {
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    if (toastDialog != null && toastDialog.isShowing()) {
                        toastDialog.dismiss();
                        toastDialog.cancel();
                        toastDialog = null;
                    }
                    t.cancel();

                }
            }, duration);
        }
    }

    public static void dimissDialog() {
        if (toastDialog != null && toastDialog.isShowing()) {
            toastDialog.dismiss();
            toastDialog.cancel();
            toastDialog = null;
        }
    }

    public static void showDialog(
            Context context,
            int toastContent,
            int width,
            int gravity,
            int x,
            int y,
            int duration,
            final OnToastDialogListener callback
            ) {
        final ToastDialog dialog = new ToastDialog(
                context,
                toastContent,
                width,
                gravity,
                x,
                y);
        dialog.tvToastContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                callback.onToastDialogDismiss();
            }
        });
        dialog.show();
        if (duration > 0) {
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    dialog.dismiss();
                    t.cancel();
                }
            }, duration);
        }
    }

    // Private functions
    private void initToastContent(
            int toastContent,
            int width,
            int gravity,
            int x,
            int y) {

        tvToastContent = (TextView)findViewById(R.id.tv_toast_content);
        int textWidth = (int)tvToastContent.getPaint().measureText(
                context.getResources().getText(toastContent).toString());
        textWidth += 50;
        textWidth = textWidth > width ? width : textWidth;

        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = textWidth;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = gravity;
        lp.x = x;
        lp.y = y;
        lp.dimAmount = 0;
        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvToastContent.setText(toastContent);
    }
    private void hideBottomUIMenu() {

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
    private void initAllViews() {
        List<View> allChildrenViews = ViewUtils.getAllChildrenViews(rootView);
        for (View v: allChildrenViews) {
            v.setSoundEffectsEnabled(false);
            if (v instanceof TextView) {
                ((TextView)v).setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
            }
        }
    }

    public interface OnToastDialogListener{
        void onToastDialogDismiss();
    }
}
