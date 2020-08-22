package com.ekek.settingmodule.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.base.BaseDialog;
import com.ekek.settingmodule.constants.CataSettingConstant;

import java.util.HashMap;
import java.util.Map;

public class ConfirmDialog extends BaseDialog implements View.OnClickListener {

    // Interfaces
    public interface OnConfirmedListener {
        void onConfirm(ConfirmDialog dialog, int action);
    }

    // Widgets
    TextView tvMessage;
    ImageButton ibOk;
    ImageButton ibCancel;
    FrameLayout flBackground;

    // Constants
    public static final int ACTION_OK = 1;
    public static final int ACTION_CANCEL = 2;
    public static final int DEFAULT_POSTION = 60;

    // Fields
    private OnConfirmedListener listener;
    private int userParam = 0;
    private Context context;
    private Map<Integer, Bitmap> bitmapMap = new HashMap<>();

    // Constructors
    public ConfirmDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    // Override functions
    @Override
    protected void initialize() {
        tvMessage = (TextView)rootView.findViewById(R.id.tvMessage);
        ibOk = (ImageButton)rootView.findViewById(R.id.ibOk);
        ibOk.setOnClickListener(this);
        ibCancel = (ImageButton)rootView.findViewById(R.id.ibCancel);
        ibCancel.setOnClickListener(this);
        flBackground=rootView.findViewById(R.id.fl_background);
        tvMessage.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
    }
    @Override
    protected int getLayoutResource() {
        return R.layout.settingmodule_dialog_confirm;
    }
    @Override
    protected int getWidth() {
        return 1280;
    }
    @Override
    protected int getHeight() {
        return 720;
    }

    // Event handlers
    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id== R.id.ibOk){
            onConfirm(ACTION_OK);
        }else if(id== R.id.ibCancel){
            onConfirm(ACTION_CANCEL);
        }
    }

    // Public functions
    public void setMessage(int message) {
        tvMessage.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        tvMessage.setText(message);
    }
    public void setMessage(String message) {
        tvMessage.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        tvMessage.setText(message);
    }
    public void setMessagePlace(int place, int width){
        if (width > 0) {
            ViewGroup.LayoutParams lp = tvMessage.getLayoutParams();
            lp.width = width;
            tvMessage.setLayoutParams(lp);
            tvMessage.requestLayout();
        }
        tvMessage.setPadding(place, 0, 0, 0);
        ibCancel.setPadding(place + 5, 0, 0, 0);
        tvMessage.setTextSize(40.0f);
    }
    public void setMessageTextSize(float value) {
        tvMessage.setTextSize(value);
    }
    public void setDialogBackground(int id){
        flBackground.setBackgroundResource(id);
    }
    public void setOKImage(int id){
        ibOk.setImageBitmap(getBitmap(id));
    }
    public void setCancelImage(int id){
        ibCancel.setImageBitmap(getBitmap(id));
    }
    public void showCancelIcom(boolean flag){
        if(flag){
            ibCancel.setVisibility(View.VISIBLE);
        }else {
            ibCancel.setVisibility(View.INVISIBLE);
        }
    }

    // Private functions
    private void onConfirm(int action) {
        if (listener != null) {
            listener.onConfirm(this, action);
        }
    }
    private Bitmap getBitmap(int source) {
        if (bitmapMap.containsKey(source)) {
            return bitmapMap.get(source);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), source);
        bitmapMap.put(source, bitmap);
        return bitmap;
    }

    // Properties
    public void setListener(OnConfirmedListener listener) {
        this.listener = listener;
    }
    public int getUserParam() {
        return userParam;
    }
    public void setUserParam(int userParam) {
        this.userParam = userParam;
    }
}
