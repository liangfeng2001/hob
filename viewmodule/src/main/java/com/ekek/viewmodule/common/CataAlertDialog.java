package com.ekek.viewmodule.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.viewmodule.R;

public class CataAlertDialog extends Dialog implements View.OnClickListener {
    public static final int CATA_ALERT_DIALOG_STYLE_ERROR = 0;
    public static final int CATA_ALERT_DIALOG_STYLE_HOOD_CONNECTION_FAIL = 1;
    public static final int CATA_ALERT_DIALOG_STYLE_SENSOR_CONNECT_SUCCESS = 2;
    public static final int CATA_ALERT_DIALOG_STYLE_MESSAGE_NO_BUTTON = 3;

    private FrameLayout flDialog;
    private TextView msgTv;
    private ImageButton confirmBtn;
    private OnCataAlertDialogListener mListener;
    public CataAlertDialog(@NonNull Context context ,int style,String message) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.viewmodule_layout_cata_alert_dialog, null);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        flDialog = findViewById(R.id.fl_dialog);
        msgTv = findViewById(R.id.tv_message);
        msgTv.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        msgTv.setTextSize(40);
        msgTv.setTextColor(0xff757575);
        msgTv.setText(message);
        confirmBtn = findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(this);
        switch (style) {
            case CATA_ALERT_DIALOG_STYLE_ERROR:
                flDialog.setBackgroundResource(R.mipmap.cata_alert_dialog_error_bg);
                break;
            case CATA_ALERT_DIALOG_STYLE_HOOD_CONNECTION_FAIL:
                flDialog.setBackgroundResource(R.mipmap.cata_alert_dialog_hood_connect_fail_bg);
                break;
            case CATA_ALERT_DIALOG_STYLE_SENSOR_CONNECT_SUCCESS:
                flDialog.setBackgroundResource(R.mipmap.cata_alert_dialog_connect_success_bg);
                break;
            case CATA_ALERT_DIALOG_STYLE_MESSAGE_NO_BUTTON:
                flDialog.setBackgroundResource(R.mipmap.cata_alert_dialog_message_bg);
                confirmBtn.setVisibility(View.GONE);
                confirmBtn.setClickable(false);
                this.setCancelable(false);
                break;
        }
    }

    public CataAlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        View view = LayoutInflater.from(context).inflate(R.layout.viewmodule_layout_cata_alert_dialog, null);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void setMessage(String message) {
        if (msgTv != null) {
            msgTv.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
            msgTv.setText(message);
        }
    }


    @Override
    public void onClick(View view) {
        if (mListener != null) mListener.onClick();
        this.dismiss();
    }

    public void setOnCataAlertDialogListener(OnCataAlertDialogListener listener) {
        mListener = listener;
    }

    public interface OnCataAlertDialogListener{
        void onClick();


    }
}
