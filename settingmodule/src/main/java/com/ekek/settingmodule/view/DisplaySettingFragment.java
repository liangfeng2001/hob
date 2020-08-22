package com.ekek.settingmodule.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.settingmodule.R;
import com.ekek.settingmodule.R2;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.database.SettingPreferencesUtil;
import com.ekek.settingmodule.model.SettingDoBack;
import com.ekek.settingmodule.utils.BrightnessUtil;
import com.ekek.viewmodule.product.ProductManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import solid.ren.skinlibrary.SkinLoaderListener;
import solid.ren.skinlibrary.loader.SkinManager;

public class DisplaySettingFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener {
    @BindView(R2.id.sb_brightness)
    SeekBar sbBrightness;
    @BindView(R2.id.gv_wallpaper)
    GridView gvWallpaper;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.title_brightness)
    TextView titleBrightness;
    @BindView(R2.id.title_wallpaper)
    TextView titleWallpaper;
    Unbinder unbinder;
    private int[] wallpaperIDs;
    private String[] themeArray;
    private List<Map<String, Object>> themeCotentList = new ArrayList<>();
    private GridViewAdpter adpter;
    private Typeface typeface;
    @Override
    public int initLyaout() {
        return R.layout.settingmodule_fragment_display_setting;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        TypedArray mTypedArray = getResources().obtainTypedArray(R.array.settingmodule_wallpaper_preview);
        int len = getResources().getIntArray(R.array.settingmodule_wallpaper_preview).length;
        wallpaperIDs = new int[len];
        for (int i = 0; i < len; i++) {
            int imageId = mTypedArray.getResourceId(i, R.mipmap.wallpaper_black_preview);
            wallpaperIDs[i] = imageId;

        }
        mTypedArray.recycle();

        SetFont();
        SetUI();
    }

    private void SetUI(){
        if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_display_haier);
            drawable.setBounds(0, 0, 53, 53);
            tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        initBrightness();
        Resources res = getResources();
        themeArray = res.getStringArray(R.array.settingmodule_wallpaper);
        themeCotentList.clear();
        for (int i = 0; i < themeArray.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("WallpaperID", wallpaperIDs[i]);
            map.put("ThemeName", themeArray[i]);
            themeCotentList.add(map);
        }
        adpter = new GridViewAdpter(getActivity());
        gvWallpaper.setAdapter(adpter);


    }

    private void initBrightness() {

        //设置最大刻度
        sbBrightness.setMax(255);
        //设置初始的Progress
        String brightnessStr = SettingPreferencesUtil.getDefaultBrightness(getActivity());
        int brightness = Integer.valueOf(brightnessStr);
        if (brightness == -1)
            sbBrightness.setProgress(BrightnessUtil.getSystemBrightness(getActivity()));
        else sbBrightness.setProgress(brightness);
        sbBrightness.setOnSeekBarChangeListener(this);

    }

    private void changeTheme(final String skinName) {
        SkinManager.getInstance().loadSkin(skinName,
                new SkinLoaderListener() {
                    @Override
                    public void onStart() {
                        Log.i("SkinLoaderListener", "正在切换中");
                        //dialog.show();
                    }

                    @Override
                    public void onSuccess() {
                        Log.i("SkinLoaderListener", "切换成功");
                        SettingPreferencesUtil.saveDefaultTheme(getActivity(), skinName);
                        adpter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(String errMsg) {
                        Log.i("SkinLoaderListener", "切换失败:" + errMsg);
                    }

                    @Override
                    public void onProgress(int progress) {
                        Log.i("SkinLoaderListener", "皮肤文件下载中:" + progress);

                    }
                }

        );
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        //改变亮度
        BrightnessUtil.changeAppBrightness(getActivity(), progress);
        SettingPreferencesUtil.saveDefaultBrightness(getActivity(), String.valueOf(progress));
        Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, progress);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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


    public class GridViewAdpter extends BaseAdapter {

        private Context context;
        private Map<Integer, Bitmap> bitmapMap = new HashMap<>();

        public GridViewAdpter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return themeCotentList.size();
        }

        @Override
        public Map<String, Object> getItem(int position) {
            return themeCotentList.get(position);
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
                convertView = View.inflate(context, R.layout.settingmodule_layout_gridview_wallpaper, null);
                holder.flSelect = (FrameLayout) convertView.findViewById(R.id.fl_wallpaper_backgound);
                holder.ivWallpaper = (ImageView) convertView.findViewById(R.id.iv_wallpaper);
                holder.ivSelect = (ImageView) convertView.findViewById(R.id.iv_select);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (themeArray[position].equals(SettingPreferencesUtil.getDefaultTheme(getActivity()))) {
                holder.ivSelect.setVisibility(View.VISIBLE);

            } else {
                holder.ivSelect.setVisibility(View.INVISIBLE);
            }
            holder.ivWallpaper.setImageBitmap(getBitmap((Integer) themeCotentList.get(position).get("WallpaperID")));
            holder.flSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LogUtil.d("Enter:: onClick----->" + themeArray[position]);
                    changeTheme(themeArray[position]);
                }
            });


            return convertView;
        }

        private Bitmap getBitmap(int source) {
            if (bitmapMap.containsKey(source)) {
                return bitmapMap.get(source);
            }

            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), source);
            bitmapMap.put(source, bitmap);
            return bitmap;
        }

        class ViewHolder {
            private FrameLayout flSelect;
            private ImageView ivWallpaper;
            private ImageView ivSelect;

        }
    }

    @OnClick(R2.id.tv_title)
    public void onClick() {
        EventBus.getDefault().post(new SettingDoBack(CataSettingConstant.do_back));
    }



    private void SetFont() {

        typeface = GlobalVars.getInstance().getDefaultFontRegular();
        tvTitle.setTypeface(typeface);
        titleBrightness.setTypeface(typeface);
        titleWallpaper.setTypeface(typeface);
    }
}
