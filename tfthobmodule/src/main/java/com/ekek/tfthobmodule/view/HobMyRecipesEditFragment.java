package com.ekek.tfthobmodule.view;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.dialog.ConfirmDialog;
import com.ekek.settingmodule.dialog.ToastDialog;
import com.ekek.tfthobmodule.CataTFTHobApplication;
import com.ekek.tfthobmodule.R;
import com.ekek.tfthobmodule.data.CookerSettingData;
import com.ekek.tfthobmodule.database.SettingDbHelper;
import com.ekek.tfthobmodule.entity.MyRecipesTable;
import com.ekek.tfthobmodule.event.CloseDialogEvent;
import com.inuker.bluetooth.library.utils.StringUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HobMyRecipesEditFragment extends BaseFragment {

    // Interfaces
    public interface OnHobMyRecipesEditFragmentListener extends OnFragmentListener {
        void onHobMyRecipesEditFragmentFinish(CookerSettingData data);
        void onHobMyRecipesEditFragmentRequestToSet(int type, MyRecipesTable initValue);
    }

    // Widgets
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_name_content)
    TextView tvNameContent;
    @BindView(R.id.tv_power)
    TextView tvPower;
    @BindView(R.id.tv_power_content)
    TextView tvPowerContent;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_time_content)
    TextView tvTimeContent;
    @BindView(R.id.tv_decription)
    TextView tvDecription;
    @BindView(R.id.tv_decription_content)
    TextView tvDecriptionContent;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.ic_info)
    ImageView icInfo;
    @BindView(R.id.tv_up_toast)
    TextView tvUpToast;
    Unbinder unbinder;

    // Constants
    public static final int RECIPES_EDIT_TYPE_NAME = 0;
    public static final int RECIPES_EDIT_TYPE_TEMPERATURE = 1;
    public static final int RECIPES_EDIT_TYPE_TIME = 2;
    public static final int RECIPES_EDIT_TYPE_DESCRIPTION = 3;
    public static final int RECIPES_EDIT_TYPE_CHOOSE_TEMPERATURE_MODE = 4;
    public static final int HANDLE_SHOW_TIME=0;

    // Fields
    private MyRecipesTable myRecipesTable = new MyRecipesTable();
    private  int mPowerVaule=0 ;  //缓存 设置好的 温度值 2019年9月10日9:15:46
    private int mMaxTime=0;      // 最长的加热时间 2019年9月10日9:29:44

    ConfirmDialog dialog ;//= new ConfirmDialog(getActivity());

    // Override functions
    @Override
    public int initLyaout() {
        return R.layout.tfthobmodule_fragment_hob_my_recipes_edit;
    }
    @Override
    public void initListener() {
    }
    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        registerEventBus();

    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            registerEventBus();
            initFonts();
            tvNameContent.setText(myRecipesTable.getName());
            tvDecriptionContent.setText(myRecipesTable.getDecription());

            String powerContent = String.format(
                    Locale.ENGLISH,
                    "%d°",
                    myRecipesTable.getTempValue());
            if (myRecipesTable.getTempValue() == 0) {
                powerContent = "";
            }
            tvPowerContent.setText(powerContent);

            String timeContent = String.format(
                    Locale.ENGLISH,
                    "%d:%02d",
                    myRecipesTable.getHourValue(),
                    myRecipesTable.getMinuteValue());
            if (myRecipesTable.getHourValue() == 0 && myRecipesTable.getMinuteValue() == 0) {
                timeContent = "";
            }
            tvTimeContent.setText(timeContent);

            if (tvNameContent.getText().toString().equals("")) {
                tvName.setVisibility(View.VISIBLE);
                tvNameContent.setVisibility(View.GONE);
            } else {
                tvNameContent.setVisibility(View.VISIBLE);
                tvName.setVisibility(View.GONE);
            }
            if (tvPowerContent.getText().toString().equals("")) {
                tvPower.setVisibility(View.VISIBLE);
                tvPowerContent.setVisibility(View.GONE);
            } else {
                tvPowerContent.setVisibility(View.VISIBLE);
                tvPower.setVisibility(View.GONE);
            }
            if (tvTimeContent.getText().toString().equals("")) {
                tvTime.setVisibility(View.VISIBLE);
                tvTimeContent.setVisibility(View.GONE);
            } else {
                tvTimeContent.setVisibility(View.VISIBLE);
                tvTime.setVisibility(View.GONE);
            }
            if (tvDecriptionContent.getText().toString().equals("")) {
                tvDecription.setVisibility(View.VISIBLE);
                tvDecriptionContent.setVisibility(View.GONE);
            } else {
                tvDecriptionContent.setVisibility(View.VISIBLE);
                tvDecription.setVisibility(View.GONE);
            }

            if (myRecipesTable.getId() == null) {
                tvSave.setText(R.string.tfthobmodule_fragment_my_recipes_edit_title_save);
                icInfo.setVisibility(View.VISIBLE);
            } else {
                tvSave.setText(R.string.tfthobmodule_fragment_my_recipes_edit_title_cook);
                icInfo.setVisibility(View.INVISIBLE);
            }
        }else {
          //  unregisterEventBus();
            LogUtil.d("liang show recipes dialog 3");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initFonts();
        tvBack.setText(R.string.tfthobmodule_fragment_my_recipes_title);
        tvName.setText(R.string.tfthobmodule_fragment_my_recipes_edit_title_name);
        tvPower.setText(R.string.tfthobmodule_fragment_my_recipes_edit_title_temp);
        tvTime.setText(R.string.tfthobmodule_fragment_my_recipes_edit_title_time);
        tvDecription.setText(R.string.tfthobmodule_fragment_my_recipes_edit_title_description);
    }

    // Event handlers
    @OnClick({
            R.id.tv_back,
            R.id.tv_name,
            R.id.tv_name_content,
            R.id.tv_power,
            R.id.tv_power_content,
            R.id.tv_time,
            R.id.tv_time_content,
            R.id.tv_decription,
            R.id.tv_decription_content,
            R.id.tv_save,
            R.id.ic_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                ((OnHobMyRecipesEditFragmentListener) mListener).onHobMyRecipesEditFragmentFinish(null);

                break;
            case R.id.tv_name:
            case R.id.tv_name_content:
                ((OnHobMyRecipesEditFragmentListener) mListener).onHobMyRecipesEditFragmentRequestToSet(
                        RECIPES_EDIT_TYPE_NAME,
                        myRecipesTable);
                break;
            case R.id.tv_power:
            case R.id.tv_power_content:
                ((OnHobMyRecipesEditFragmentListener) mListener).onHobMyRecipesEditFragmentRequestToSet(
                        RECIPES_EDIT_TYPE_CHOOSE_TEMPERATURE_MODE,
                        myRecipesTable);
                break;
            case R.id.tv_time:
            case R.id.tv_time_content:
                ((OnHobMyRecipesEditFragmentListener) mListener).onHobMyRecipesEditFragmentRequestToSet(
                        RECIPES_EDIT_TYPE_TIME,
                        myRecipesTable);
                break;
            case R.id.tv_decription:
            case R.id.tv_decription_content:
                ((OnHobMyRecipesEditFragmentListener) mListener).onHobMyRecipesEditFragmentRequestToSet(
                        RECIPES_EDIT_TYPE_DESCRIPTION,
                        myRecipesTable);
                break;
            case R.id.tv_save:
                doSave();
                break;
            case R.id.ic_info:
                ToastDialog.showDialog(
                        getActivity(),
                        R.string.tfthobmodule_my_recipes_edit_fragment_toast_content,
                        ToastDialog.WIDTH_LONG,
                        icInfo,
                        ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                        CataSettingConstant.TOAST_SHOW_DURATION);
                break;
        }
    }

    // Public functions
    public void setContent(int type, String content) {
        switch (type) {
            case RECIPES_EDIT_TYPE_NAME:
                myRecipesTable.setName(content);
                break;
            case RECIPES_EDIT_TYPE_TIME:
                parseTimeStr(content);
                break;
            case RECIPES_EDIT_TYPE_TEMPERATURE:
                parsePowerStr(content);
                break;
            case RECIPES_EDIT_TYPE_DESCRIPTION:
                myRecipesTable.setDecription(content);
                break;
            case RECIPES_EDIT_TYPE_CHOOSE_TEMPERATURE_MODE:
                // Do nothing
                break;
        }
    }
    public void setMyRecipesTable(MyRecipesTable myRecipesTable) {
        if (myRecipesTable == null) {
            this.myRecipesTable = new MyRecipesTable();
        } else {
            this.myRecipesTable = myRecipesTable;
        }
    }

    // Private functions
    private void parsePowerStr(String value) {
        String power = value.substring(0, value.length() - 1);
        myRecipesTable.setTempValue(Integer.valueOf(power));
        mPowerVaule=Integer.valueOf(power);
    }
    private void parseTimeStr(String value) {
        String time = value;
        int total_time =0;
        if (StringUtils.isBlank(time) || !time.contains(":")) {
            myRecipesTable.setHourValue(0);
            myRecipesTable.setMinuteValue(0);
            myRecipesTable.setSecondValue(0);
            return;
        }
        String[] times = time.split(":");
        int hour = Integer.valueOf(times[0]);
        int minute = Integer.valueOf(times[1]);
        //  超过最大值，则按最大值计算； 2019年9月10日9:39:33
        if(mPowerVaule>20&&mPowerVaule<=65){
            mMaxTime=48*60;
        }else if(mPowerVaule>65&&mPowerVaule<=85){
            mMaxTime=36*60;
        }else if(mPowerVaule>85&&mPowerVaule<=90){
            mMaxTime=10*60;
        }else if(mPowerVaule>90&&mPowerVaule<=100){
            mMaxTime=6*60;
        }else if(mPowerVaule>100){
            mMaxTime=1*60;
        }
        total_time=hour*60+minute;
        if(total_time>mMaxTime){
            hour=mMaxTime/60;
            minute=mMaxTime%60;
        }
        myRecipesTable.setHourValue(hour);
        myRecipesTable.setMinuteValue(minute);
        myRecipesTable.setSecondValue(60);
    }
    private void initFonts() {
        Typeface bold = GlobalVars.getInstance().getDefaultFontBold();
        tvBack.setTypeface(bold);
        tvName.setTypeface(bold);
        tvPower.setTypeface(bold);
        tvTime.setTypeface(bold);
        tvDecription.setTypeface(bold);
        tvNameContent.setTypeface(bold);
        tvPowerContent.setTypeface(bold);
        tvTimeContent.setTypeface(bold);
        tvDecriptionContent.setTypeface(bold);
    }
    private void doSave() {
        String name = tvNameContent.getText().toString();
        String power = tvPowerContent.getText().toString();
        String time = tvTimeContent.getText().toString();
        String description = tvDecriptionContent.getText().toString();

        if (name.isEmpty() || power.isEmpty() || time.isEmpty()) {
        //    ConfirmDialog dialog = new ConfirmDialog(getActivity());
            dialog = new ConfirmDialog(getActivity());
            dialog.setMessage(R.string.tfthobmodule_dialog_message_complete_recipe);
            dialog.setListener(new ConfirmDialog.OnConfirmedListener() {
                @Override
                public void onConfirm(ConfirmDialog dialog, int action) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            myRecipesTable.setName(name);
            parsePowerStr(power);
            myRecipesTable.setDecription(description);
            parseTimeStr(time);
            myRecipesTable.setReserve("");

            if (myRecipesTable.getId() == null) {
                CataTFTHobApplication.getDaoSession().getMyRecipesTableDao().insert(myRecipesTable);
                ((OnHobMyRecipesEditFragmentListener) mListener).onHobMyRecipesEditFragmentFinish(null);
            } else {
                CataTFTHobApplication.getDaoSession().getMyRecipesTableDao().save(myRecipesTable);

                CookerSettingData data =  new CookerSettingData();
                data.setCookerSettingMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR);
                data.setTempValue(myRecipesTable.getTempValue());
                data.setTimerMinuteValue(myRecipesTable.getMinuteValue());
                data.setTimerHourValue(myRecipesTable.getHourValue());
                data.setTimerSecondValue(myRecipesTable.getSecondValue());

                data.setTempMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR);
                data.setTempShowOrder(CookerSettingData.TEMP_RECIPES_SHOW_ORDER_RECIPES_FIRST);
                data.setTempRecipesResID(R.mipmap.food_type_my_recipes);
                data.setCookerID(SettingDbHelper.getRecipesCookerID());
                ((OnHobMyRecipesEditFragmentListener) mListener).onHobMyRecipesEditFragmentFinish(data);
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CloseDialogEvent event ){
        if(event .getDialogName()==0&&dialog !=null ){
            dialog.dismiss() ;
        }
        LogUtil.d("liang close dialog 2~~~");
    }
}
