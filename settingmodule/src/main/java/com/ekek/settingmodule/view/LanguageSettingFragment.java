package com.ekek.settingmodule.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalCons;
import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.events.DateOrTimeSetEvent;
import com.ekek.settingmodule.events.SetLanguage;
import com.ekek.settingmodule.events.SettingsChangedEvent;
import com.ekek.settingmodule.model.SendOrderForFirstSwitchOn;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Samhung on 2018/2/2.
 */

public class LanguageSettingFragment extends Fragment {

    @BindView(R2.id.lv_language)
    ListView lvLanguage;
    Unbinder unbinder;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.right_back)
    ImageView rightBack;

    private static final String KEY_LANGUAGE = "Language";
    private static final String KEY_LANGUAGE_STR = "LanguageStr";

    private Typeface typeface;
    private List<Map<String, Object>> contentList = new ArrayList<>();
    private ListViewAdpter adapter;

    private boolean mType =false;

    public LanguageSettingFragment(){

    }
    @SuppressLint("ValidFragment")
    public LanguageSettingFragment(boolean type){
        this .mType=type;
    }

    public void setType(boolean type) {
        this.mType = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settingmodule_fragment_setting_language, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        tvTitle.setText(R.string.settingmodule_setting_title_language);


    }

    private void init() {
        contentList.clear();
        int[] languages = CataSettingConstant.LANGUAGES;
        String[] languageStrs = CataSettingConstant.LANGUAGE_STRS;

        for (int i = 0; i < languages.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put(KEY_LANGUAGE, languages[i]);
            map.put(KEY_LANGUAGE_STR, languageStrs[i]);
            contentList.add(map);
        }
        adapter = new ListViewAdpter(getActivity());
        lvLanguage.setAdapter(adapter);
        SetFont();
        SetUI();

        if (GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START_NONE) {// 不是第一次开机
            rightBack.setVisibility(View.INVISIBLE);
        } else if(GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START_FIRST_TIME){  // 是第一次开机
            if(mType){
                rightBack.setVisibility(View.INVISIBLE);
            }else {
                rightBack.setVisibility(View.VISIBLE);
              //  rightBack.setVisibility(View.INVISIBLE);
            }
         //   EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.SetLanguage));
            LogUtil.d("language fragment is the first time~~");
            //   SettingPreferencesUtil.saveTheFirstTimeSwitchOnHob(getActivity(),CataSettingConstant.THE_FIRST_TIME_SWITCH_ON_HOB);
        }else if (GlobalVars.getInstance().getAppStartTag() == GlobalCons.APP_START){  // 关机后，再开机
            if(mType){
                rightBack.setVisibility(View.INVISIBLE);
            }else {
                rightBack.setVisibility(View.VISIBLE);
              //  rightBack.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void SetUI(){
        if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_language_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }

    @OnClick({R2.id.tv_title,R2.id.right_back})
    public void onClick(View view) {
        if(view .getId() ==R.id.tv_title){

            EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
            EventBus.getDefault().post(new SetLanguage(CataSettingConstant.SET_LANGUAGE_SUCCESSFUL));

        }else if(view.getId()==R.id.right_back){

            EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.SetDate));
            EventBus.getDefault().post(new SetLanguage(CataSettingConstant.SET_LANGUAGE_SUCCESSFUL));
        }
    }



    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class ListViewAdpter extends BaseAdapter {

        private Context context;

        public ListViewAdpter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return contentList.size();
        }

        @Override
        public Map<String, Object> getItem(int position) {
            return contentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.settingmodule_layout_language_listview_item, null);
                holder.ctLanguage = (CheckedTextView) convertView.findViewById(R.id.ct_language);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final String content = (String) contentList.get(position).get(KEY_LANGUAGE_STR);
            final int lang = (int)contentList.get(position).get(KEY_LANGUAGE);
            int currentLang = SettingPreferencesUtil.getDefaultLanguage2(getActivity());
            if (currentLang == CataSettingConstant.LANGUAGE_UNKNOWN) {
                currentLang = CataSettingConstant.DEFAULT_LANGUAGE_2;
            }
            if (lang == currentLang) {
                holder.ctLanguage.setChecked(true);
            } else {
                holder.ctLanguage.setChecked(false);
            }
            holder.ctLanguage.setText(content);
            typeface = GlobalVars.getInstance().getHelveticaFontRegular();
            String locale = CataSettingConstant.LANGUAGE_LOCALES[position];
            if ("el".equals(locale) || "vi".equals(locale) || "ru".equals(locale)) {
                typeface = GlobalVars.getInstance().getRobotoFontRegular();
            }
            holder.ctLanguage.setTypeface(typeface);

            holder.ctLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position <= 17) {
                        CheckedTextView checkedTextView = (CheckedTextView) view;
                        checkedTextView.setChecked(true);
                        SettingPreferencesUtil.saveDefaultLanguage2(getActivity(),CataSettingConstant.LANGUAGES[position]);
                        SettingPreferencesUtil.saveDefaultLanguage(getActivity(), content);
                        adapter.notifyDataSetChanged();
                        EventBus.getDefault().post(new SettingsChangedEvent(SettingsChangedEvent.SETTING_LANGUAGE));
                        //change keyboard
                        setKeyboardForLanguageChange(CataSettingConstant.LANGUAGE_LOCALES[position]);
                    } else {
                        //ToastyUtils.error(getActivity(),getString(R.string.settingmodule_fragment_setting_language_warning_message_unavailable));
                    }

                }
            });


            return convertView;
        }

        class ViewHolder {
            private CheckedTextView ctLanguage;

        }
    }

    private void setKeyboardForLanguageChange(String locale) {
        Intent intent = new Intent("ACTION_SET_SYSTEM_LOCALE");
        intent.putExtra("locale",locale);
        getActivity().sendBroadcast(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DateOrTimeSetEvent event) {
        EventBus.getDefault().post(new SendOrderForFirstSwitchOn(CataSettingConstant.SetDate));
        EventBus.getDefault().post(new SetLanguage(CataSettingConstant.SET_LANGUAGE_SUCCESSFUL));
    }
}
