package com.ekek.settingmodule.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.viewmodule.common.FocusedTextView;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingMainFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    public static final int SETTING_MAIN_SELECT_DISPLAY_SETTING = 0;
    public static final int SETTING_MAIN_SELECT_LANGUAGE_SETTING = 1;
    public static final int SETTING_MAIN_SELECT_SOUND_SETTING = 2;
    public static final int SETTING_MAIN_SELECT_SECURITY_SETTING = 3;
    public static final int SETTING_MAIN_SELECT_CONNECTIVITY_SETTING = 4;
    public static final int SETTING_MAIN_SELECT_HOOD_OPTIONS_SETTING = 5;
    public static final int SETTING_MAIN_SELECT_POWER_LIMIT_SETTING = 6;
    public static final int SETTING_MAIN_SELECT_DEFAULT_SETTINGS_SETTING = 7;
    public static final int SETTING_MAIN_SELECT_DATE_AND_TIME_SETTING = 8;
    public static final int SETTING_MAIN_SELECT_INFORMATION_SETTING = 9;
    public static final int SETTING_MAIN_SELECT_ENERGY_SETTINGS_SETTING = 10;
    public static final int SETTING_MAIN_SELECT_TEST_COOKWARE_SETTING = 11;
    @BindView(R2.id.gv_setting)
    GridView gvSetting;
    @BindView(R2.id.ib_down)
    ImageButton ibDown;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    Unbinder unbinder;
    private String[] settings;
    private int[] settingsImageIDs;
    private SettingAdapter adapter;
    private OnSettingMainFragmentListener listener;

    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_setting_main;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        settings = getResources().getStringArray(R.array.settingmodule_setting);
        settingsImageIDs = new int[settings.length];
        TypedArray mTypedArray;
        if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
            mTypedArray = getResources().obtainTypedArray(R.array.settingmodule_setting_images_haier);
            for (int i = 0; i < getResources().getIntArray(R.array.settingmodule_setting_images_haier).length; i++) {
                int imageId = mTypedArray.getResourceId(i, R.mipmap.ic_test_cookware);
                settingsImageIDs[i] = imageId;
            }
        }else {
            mTypedArray = getResources().obtainTypedArray(R.array.settingmodule_setting_images);
            for (int i = 0; i < getResources().getIntArray(R.array.settingmodule_setting_images).length; i++) {
                int imageId = mTypedArray.getResourceId(i, R.mipmap.ic_test_cookware);
                settingsImageIDs[i] = imageId;

            }
        }


        mTypedArray.recycle();

       /* adapter = new SettingAdapter(getActivity());
        gvSetting.setAdapter(adapter);
        gvSetting.setOnItemClickListener(this);*/
        tvTitle.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter==null){
            adapter = new SettingAdapter(getActivity());
        }
        gvSetting.setAdapter(adapter);
        gvSetting.setOnItemClickListener(this);
      //  LogUtil.d("liang adapter is onStart ");
    }


    public void updateUI() {  //
       /* if (adapter != null) {
            adapter.notifyDataSetChanged();
           // LogUtil.d("liang updateUI");
        }else {

        }*/

        if(adapter==null){
            adapter = new SettingAdapter(getActivity());
            LogUtil.d("liang adapter is null");
        }

        if(adapter!=null&&gvSetting!=null){
            gvSetting.setAdapter(adapter);
            gvSetting.setOnItemClickListener(this);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
       // LogUtil.d("liang adapter is onResume ");
    }

    @Override
    public void onPause() {
        super.onPause();
      //  LogUtil.d("liang Enter:: onPause ");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //    init();

          /*  if(adapter==null){
                adapter = new SettingAdapter(getActivity());
                gvSetting.setAdapter(adapter);
                gvSetting.setOnItemClickListener(this);
                LogUtil.d("liang adapter is null ");
            }else{
                adapter.notifyDataSetChanged();
                LogUtil.d("liang adapter is not null ");
            }
*/
          //  LogUtil.d("liang show");
        }else {
         //   LogUtil.d("liang hidden");
        }
    }


    @OnClick(R2.id.ib_down)
    public void onViewClicked() {
      //  LogUtil.d("Enter:: onViewClicked");
        //gvSetting.smoothScrollToPosition(0);
    }
    @OnClick(R2.id.tv_title)
    public void onClick() {
        EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
    }

    public void setOnSettingMainFragmentListener(OnSettingMainFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String clickContent = settings[position];
        int logoResID = settingsImageIDs[position];
        if (clickContent.equals(getString(R.string.settingmodule_setting_title_display))) {
            doClickEvent(SETTING_MAIN_SELECT_DISPLAY_SETTING, logoResID, clickContent);

        } else if (clickContent.equals(getString(R.string.settingmodule_setting_title_language))) {
            doClickEvent(SETTING_MAIN_SELECT_LANGUAGE_SETTING, logoResID, clickContent);

        } else if (clickContent.equals(getString(R.string.settingmodule_setting_title_sound))) {
            doClickEvent(SETTING_MAIN_SELECT_SOUND_SETTING, logoResID, clickContent);

        } else if (clickContent.equals(getString(R.string.settingmodule_setting_title_security))) {
            doClickEvent(SETTING_MAIN_SELECT_SECURITY_SETTING, logoResID, clickContent);

        } else if (clickContent.equals(getString(R.string.settingmodule_setting_title_conectivity))) {

            doClickEvent(SETTING_MAIN_SELECT_CONNECTIVITY_SETTING, logoResID, clickContent);
        } else if (clickContent.equals(getString(R.string.settingmodule_setting_title_hood_options))) {
            doClickEvent(SETTING_MAIN_SELECT_HOOD_OPTIONS_SETTING, logoResID, clickContent);

        } else if (clickContent.equals(getString(R.string.settingmodule_setting_title_power_limit))) {
            if(GlobalVars.getInstance().isAllCookersIsClose()){  // 所有炉头都关闭了
                doClickEvent(SETTING_MAIN_SELECT_POWER_LIMIT_SETTING, logoResID, clickContent);
            }else {  // 有炉头开着

            }

        } else if (clickContent.equals(getString(R.string.settingmodule_setting_title_default_settings))) {
            doClickEvent(SETTING_MAIN_SELECT_DEFAULT_SETTINGS_SETTING, logoResID, clickContent);

        } else if (clickContent.equals(getString(R.string.settingmodule_setting_title_date_and_time))) {
            doClickEvent(SETTING_MAIN_SELECT_DATE_AND_TIME_SETTING, logoResID, clickContent);

        } else if (clickContent.equals(getString(R.string.settingmodule_setting_title_information))) {
            doClickEvent(SETTING_MAIN_SELECT_INFORMATION_SETTING, logoResID, clickContent);

        } else if (clickContent.equals(getString(R.string.settingmodule_setting_title_energy_settings))) {
            doClickEvent(SETTING_MAIN_SELECT_ENERGY_SETTINGS_SETTING, logoResID, clickContent);

        } else if (clickContent.equals(getString(R.string.settingmodule_setting_title_test_cookware))) {
            doClickEvent(SETTING_MAIN_SELECT_TEST_COOKWARE_SETTING, logoResID, clickContent);
        }

    }

    private void doClickEvent(int what, int logoResID, String title) {
        if (listener != null) listener.onSettingMainFragmentSelect(what, logoResID, title);
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

    public class SettingAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private Context context;

        public SettingAdapter(Context context) {
            this.context = context;
            mInflater = LayoutInflater.from(context);

        }

        //返回数据集的长度
        @Override
        public int getCount() {
            return settings.length;
        }

        @Override
        public Object getItem(int position) {
            return settings[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.settingmodule_layout_setting_gridview_item, parent, false); //加载布局
                holder = new ViewHolder();

                holder.tvSetting = (FocusedTextView) convertView.findViewById(R.id.tv_setting);
                holder.tvSetting.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
                convertView.setTag(holder);
            } else {   //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvSetting.setText(settings[position]);
            holder.tvSetting.setTextColor(Color.WHITE);
            Drawable drawable = context.getResources().getDrawable(settingsImageIDs[position]);
            drawable.setBounds(0, 0, 52, 52);
            holder.tvSetting.setCompoundDrawables(drawable, null, null, null);
            //LogUtil.d("liang list the data of settings ~~~~~~~~~  "+ position);
            if( holder.tvSetting.getText().equals(getString(R.string.settingmodule_setting_title_power_limit))){
                if(GlobalVars.getInstance().isAllCookersIsClose()){  // 所有炉头都关闭了
                    holder.tvSetting.setTextColor(Color.WHITE);
                    //LogUtil.d("liang the color is WHITE "+position);
                }else {  // 有炉头开着
                    holder.tvSetting.setTextColor(Color.GRAY);
                    //LogUtil.d("liang the color is GRAY "+position);
                }
            }
            return convertView;
        }

        private class ViewHolder {
            FocusedTextView tvSetting;
        }
    }

    public interface OnSettingMainFragmentListener extends OnFragmentListener {
        void onSettingMainFragmentSelect(int what, int logoResID, String title);

    }
}
