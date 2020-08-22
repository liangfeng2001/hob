package com.ekek.tfthobmodule.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.dialog.ToastDialog;
import com.ekek.tfthobmodule.CataTFTHobApplication;
import com.ekek.tfthobmodule.R;
import com.ekek.tfthobmodule.data.CookerSettingData;
import com.ekek.tfthobmodule.database.SettingDbHelper;
import com.ekek.tfthobmodule.entity.FishTable;
import com.ekek.tfthobmodule.entity.FruitTable;
import com.ekek.tfthobmodule.entity.LegumesAndCerealsTable;
import com.ekek.tfthobmodule.entity.MeatTable;
import com.ekek.tfthobmodule.entity.ShellfishTable;
import com.ekek.tfthobmodule.entity.VegetablesTable;
import com.ekek.viewmodule.common.CataDialog;
import com.ekek.viewmodule.product.ProductManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class HobCookingTablesDetailFragment extends BaseFragment {
    public static final int COOKING_TABLES_DETAIL_TYPE_MEAT = 0;
    public static final int COOKING_TABLES_DETAIL_TYPE_LEGUMES_AND_CEREALS = 1;
    public static final int COOKING_TABLES_DETAIL_TYPE_FRUIT = 2;
    public static final int COOKING_TABLES_DETAIL_TYPE_SHELLFISH = 3;
    public static final int COOKING_TABLES_DETAIL_TYPE_VEGETABLES = 4;
    public static final int COOKING_TABLES_DETAIL_TYPE_FISH = 5;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv_cooking_list)
    ListView lvCookingList;
    @BindView(R.id.ic_info)
    ImageView icInfo;
    Unbinder unbinder;
    private int type = COOKING_TABLES_DETAIL_TYPE_MEAT;
    private CookingAdapter adapter;
    private List<Bean> beans = new ArrayList<>();
    private Map<Integer, Bitmap> bitmapMap = new HashMap<>();

    public HobCookingTablesDetailFragment() {

    }

    @SuppressLint("ValidFragment")
    public HobCookingTablesDetailFragment(int type) {
        this.type = type;
    }

    @Override
    public int initLyaout() {
        return R.layout.tfthobmodule_fragment_hob_cooking_tables_detail;
    }

    @Override
    public void initListener() {

    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (CataTFTHobApplication.getInstance().isCookingTablesEmpty()) {
                reloadCookingTables();
            } else {
                updateBackUI();
                startLoadFoodData();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setFont();
        reloadCookingTables();
    }

    private void setFont() {
        tvTitle.setTypeface(GlobalVars.getInstance().getDefaultFontBold());
    }

    private void reloadCookingTables() {
        CataTFTHobApplication.getInstance().refreshCookingTables(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                updateBackUI();
                startLoadFoodData();
            }
        });
    }
    private Bitmap getBitmap(int source) {
        if (bitmapMap.containsKey(source)) {
            return bitmapMap.get(source);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), source);
        bitmapMap.put(source, bitmap);
        return bitmap;
    }
    private void updateBackUI() {
        switch (type) {
            case COOKING_TABLES_DETAIL_TYPE_MEAT:
                if(ProductManager.PRODUCT_TYPE== ProductManager.Haier){
                    ibBack.setImageBitmap(getBitmap(R.mipmap.ic_cooking_tables_detail_meat_haier));
                }else {
                    ibBack.setImageBitmap(getBitmap(R.mipmap.ic_cooking_tables_detail_meat));
                }

                tvTitle.setText(CataTFTHobApplication.getInstance().getMeatTablesDesciption());//Direct Cooking:Cook and serve.
                break;
            case COOKING_TABLES_DETAIL_TYPE_LEGUMES_AND_CEREALS:
                if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                    ibBack.setImageBitmap(getBitmap(R.mipmap.ic_cooking_tables_detail_legumes_and_cereals_haier));
                }else {
                    ibBack.setImageBitmap(getBitmap(R.mipmap.ic_cooking_tables_detail_legumes_and_cereals));
                }
                tvTitle.setText(CataTFTHobApplication.getInstance().getLegumesAndCerealsTablesDesciption());
                break;
            case COOKING_TABLES_DETAIL_TYPE_FRUIT:
                if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                    ibBack.setImageBitmap(getBitmap(R.mipmap.ic_cooking_tables_detail_fruit_haier));
                }else {
                    ibBack.setImageBitmap(getBitmap(R.mipmap.ic_cooking_tables_detail_fruit));
                }

                tvTitle.setText(CataTFTHobApplication.getInstance().getFruitTablesDesciption());
                break;
            case COOKING_TABLES_DETAIL_TYPE_SHELLFISH:
                if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                    ibBack.setImageBitmap(getBitmap(R.mipmap.ic_cooking_tables_detail_shellfish_haier));
                }else {
                    ibBack.setImageBitmap(getBitmap(R.mipmap.ic_cooking_tables_detail_shellfish));
                }

                tvTitle.setText(CataTFTHobApplication.getInstance().getShellfishTablesDesciption());
                break;
            case COOKING_TABLES_DETAIL_TYPE_VEGETABLES:
                if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                    ibBack.setImageBitmap(getBitmap(R.mipmap.ic_cooking_tables_detail_vegetables_haier));
                }else {
                    ibBack.setImageBitmap(getBitmap(R.mipmap.ic_cooking_tables_detail_vegetables));
                }

                tvTitle.setText(CataTFTHobApplication.getInstance().getVegetablesTablesDesciption());
                break;
            case COOKING_TABLES_DETAIL_TYPE_FISH:
                if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                    ibBack.setImageBitmap(getBitmap(R.mipmap.ic_cooking_tables_detail_fish_haier));
                }else {
                    ibBack.setImageBitmap(getBitmap(R.mipmap.ic_cooking_tables_detail_fish));
                }

                tvTitle.setText(CataTFTHobApplication.getInstance().getFishTablesDesciption());
                break;

        }

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        adapter = new CookingAdapter(getActivity());
        lvCookingList.setAdapter(adapter);
        setFont();
    }


    private void startLoadFoodData() {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                loadFoodData();
                emitter.onComplete();
            }
        }).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                // Android ListView 的 Bug，必须重新设置 adapter，
                // 否则在数据发生变化后，setSelection 不起作用
                lvCookingList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lvCookingList.setSelection(0);
            }
        });
    }

    private void loadFoodData() {
        beans.clear();
        switch (type) {
            case COOKING_TABLES_DETAIL_TYPE_MEAT:
                for (MeatTable meatTable : CataTFTHobApplication.getInstance().getMeatTables()) {
                    Bean bean = new Bean(meatTable.getName(), meatTable.getWeight(), meatTable.getCookDatas());
                    beans.add(bean);
                }

                break;
            case COOKING_TABLES_DETAIL_TYPE_LEGUMES_AND_CEREALS:
                for (LegumesAndCerealsTable legumesAndCerealsTable : CataTFTHobApplication.getInstance().getLegumesAndCerealsTables()) {
                    Bean bean = new Bean(legumesAndCerealsTable.getName(), legumesAndCerealsTable.getWeight(), legumesAndCerealsTable.getCookDatas());
                    beans.add(bean);
                }

                break;
            case COOKING_TABLES_DETAIL_TYPE_FRUIT:
                for (FruitTable fruitTable : CataTFTHobApplication.getInstance().getFruitTables()) {
                    Bean bean = new Bean(fruitTable.getName(), fruitTable.getWeight(), fruitTable.getCookDatas());
                    beans.add(bean);
                }

                break;
            case COOKING_TABLES_DETAIL_TYPE_SHELLFISH:
                for (ShellfishTable shellfishTable : CataTFTHobApplication.getInstance().getShellfishTables()) {
                    Bean bean = new Bean(shellfishTable.getName(), shellfishTable.getWeight(), shellfishTable.getCookDatas());
                    beans.add(bean);
                }

                break;
            case COOKING_TABLES_DETAIL_TYPE_VEGETABLES:
                for (VegetablesTable vegetablesTable : CataTFTHobApplication.getInstance().getVegetablesTables()) {
                    Bean bean = new Bean(vegetablesTable.getName(), vegetablesTable.getWeight(), vegetablesTable.getCookDatas());
                    beans.add(bean);
                }

                break;
            case COOKING_TABLES_DETAIL_TYPE_FISH:
                for (FishTable fishTable : CataTFTHobApplication.getInstance().getFishTables()) {
                    Bean bean = new Bean(fishTable.getName(), fishTable.getWeight(), fishTable.getCookDatas());
                    beans.add(bean);
                }
                break;
        }

    }


    @OnClick(R.id.ib_back)
    public void onViewClicked() {
        ((OnHobCookingTablesDetailFragmentListener) mListener).onHobCookingTablesDetailFragmentFinish();
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

    @OnClick(R.id.ic_info)
    public void onClick() {

        ToastDialog.showDialog(
                getActivity(),
                R.string.tfthobmodule_hob_cooking_table_detail_fragment_toast_content,
                ToastDialog.WIDTH_EXTRA_LONG,
                icInfo,
                ToastDialog.ANCHOR_DIRECTION_BELOW_END,
                CataSettingConstant.TOAST_SHOW_DURATION);
    }

    public class CookingAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private Context context;
        private View childView;

        public CookingAdapter(Context context) {
            this.context = context;
            mInflater = LayoutInflater.from(context);
            //childView = LayoutInflater.from(context).inflate(R.layout.tfthobmodule_cooking_tables_detail_view_cook_data, null);


        }

        //返回数据集的长度
        @Override
        public int getCount() {
            return beans.size();
        }

        @Override
        public Object getItem(int position) {
            return beans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.tfthobmodule_layout_cooking_tables_detail_item, parent, false); //加载布局
                holder = new ViewHolder();

                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvWeight = (TextView) convertView.findViewById(R.id.tv_weight);
                holder.llCookDataContainer = (LinearLayout) convertView.findViewById(R.id.ll_cook_data_container);
                holder.ibStartCook = (ImageButton) convertView.findViewById(R.id.ib_start_cook);
                if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                    holder.ibStartCook.setImageBitmap(getBitmap(R.mipmap.ic_start_cook_haier));
                }else {

                }

                convertView.setTag(holder);
            } else {   //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
                holder = (ViewHolder) convertView.getTag();
            }

            final Bean bean = beans.get(position);
            holder.tvName.setText(bean.getName());
            if (bean.getWeight().isEmpty()) holder.tvWeight.setVisibility(View.GONE);
            else {
                holder.tvWeight.setText(bean.getWeight());
                holder.tvWeight.setVisibility(View.VISIBLE);
            }
            holder.tvWeight.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
            holder.tvName.setTypeface(GlobalVars.getInstance().getDefaultFontBold());
            List<String> cookDatas = bean.getCookDatas();
            holder.llCookDataContainer.removeAllViews();
            if (holder.llCookDataContainer.getChildCount() == 0) {
                for (String cookData : cookDatas) {
                    View view = mInflater.inflate(R.layout.tfthobmodule_cooking_tables_detail_view_cook_data, parent, false);
                    TextView tvTemp = (TextView) view.findViewById(R.id.tv_temp);
                    TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
                    ImageView imageGap=view.findViewById(R.id.im_gap);
                    String[] strs = cookData.split("-");
                    tvTemp.setText(strs[0] + "°");
                    int minute = Integer.valueOf(strs[1]);
                    int hour = minute / 60;
                    minute = minute % 60;
                    String timeStr = ((hour == 0) ? "" : String.valueOf(hour) + "h") + "" + ((minute == 0) ? "" : String.valueOf(minute) + "min");

                    tvTime.setText(getCookTimeStr(timeStr));
                    holder.llCookDataContainer.addView(view);
                    tvTemp.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
                    tvTime.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());

                    if(ProductManager.PRODUCT_TYPE==ProductManager.Haier){
                        tvTime.setTextColor(Color.WHITE);
                        imageGap.setImageBitmap(getBitmap(R.mipmap.ic_gap_haier));
                    }else {

                    }
                }
            }
            holder.ibStartCook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* if (beans.get(position).getCookDatas().size() > 1) {

                    }*/
                    doStartCook(position);
                    LogUtil.d("Enter:: on click");
                }
            });
            return convertView;
        }

        private class ViewHolder {
            TextView tvName;
            TextView tvWeight;
            LinearLayout llCookDataContainer;
            ImageButton ibStartCook;
        }
    }

    private void doStartCook(int position) {
        Bean bean = beans.get(position);
        String cookData = bean.getCookDatas().get(0);
        String[] strs = cookData.split("-");
        int temp = Integer.valueOf(strs[0]);
        int time = Integer.valueOf(strs[1]);
        int hour = time / 60;
        int minute = time % 60;
       // ((OnHobCookingTablesDetailFragmentListener) mListener).onHobCookingTablesDetailFragmentStartToCook(temp, hour, minute);
       //--------------由于原来的菜谱按钮在左边的抽屉栏上，所以选择完毕后，要点击炉头进行烹饪，但是，现在菜谱按钮在主界面上，则不需要重新选择炉头。---------------------------------
        CookerSettingData data =  new CookerSettingData();
        data.setTempValue(temp);
        data.setTimerMinuteValue(minute);
        data.setTimerHourValue(hour);
        data.setTimerSecondValue(59);
        data.setTempMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR);
        data.setCookerID(SettingDbHelper.getRecipesCookerID());
        ((OnHobCookingTablesDetailFragmentListener) mListener).onHobCookingTablesDetailFragmentStartToCookNew(data);
    }

    private CataDialog dialog;

    private void showCookTypeChoosenDialog(int position) {
        if (dialog == null) {
            dialog = new CataDialog(getActivity());
        }
        List<String> cookDatas = beans.get(position).getCookDatas();
        RadioGroup radioGroup = (RadioGroup) LayoutInflater.from(getActivity()).inflate(R.layout.tfthobmodule_layout_dialog_cooking_table_detail_cook_type_choosen, null);
        List<HashMap<String, String>> maps = new ArrayList<>();
        for (String cookData : cookDatas) {
            HashMap<String, String> map = new HashMap<>();
            String[] strs = cookData.split("-");
            String tempStr = strs[0];
            String timeStr = strs[1];
            map.put("temp", tempStr);
            map.put("time", timeStr);
            maps.add(map);
            int minute = Integer.valueOf(strs[1]);
            int hour = minute / 60;
            minute = minute % 60;

            String time = ((hour == 0) ? "" : String.valueOf(hour) + "h") + "" + ((minute == 0) ? "" : String.valueOf(minute) + "min");
            RadioButton tempButton = new RadioButton(getActivity());
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(200, 200);
            layoutParams.setMargins(10, 10, 10, 10);
            tempButton.setLayoutParams(layoutParams);

            //tempButton.setBackgroundResource(R.drawable.xxx);	// 设置RadioButton的背景图片
            //tempButton.setButtonDrawable(R.drawable.xxx);			// 设置按钮的样式
            tempButton.setPadding(80, 0, 0, 0);                // 设置文字距离按钮四周的距离

            tempButton.setTextSize(50);
            tempButton.setTextColor(Color.RED);
            tempButton.setText(tempStr);
            // radioGroup.addView(tempButton);

     /*       tvTime.setText(getCookTimeStr(timeStr));
            holder.llCookDataContainer.addView(view);*/
        }


        dialog.setContentView(radioGroup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


    private void showDetailDialog(int position) {
        if (dialog == null) {
            dialog = new CataDialog(getActivity());
        }
        List<String> cookDatas = beans.get(position).getCookDatas();
        RadioGroup radioGroup = (RadioGroup) LayoutInflater.from(getActivity()).inflate(R.layout.tfthobmodule_layout_dialog_cooking_table_detail_cook_type_choosen, null);
        dialog.setContentView(radioGroup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        List<HashMap<String, String>> maps = new ArrayList<>();
        for (String cookData : cookDatas) {
            HashMap<String, String> map = new HashMap<>();
            String[] strs = cookData.split("-");
            String tempStr = strs[0];
            String timeStr = strs[1];
            map.put("temp", tempStr);
            map.put("time", timeStr);
            maps.add(map);
            int minute = Integer.valueOf(strs[1]);
            int hour = minute / 60;
            minute = minute % 60;

            String time = ((hour == 0) ? "" : String.valueOf(hour) + "h") + "" + ((minute == 0) ? "" : String.valueOf(minute) + "min");
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.tfthobmodule_layout_dialog_cooking_table_detail_cook_type_item, null);
            TextView tvTemp = view.findViewById(R.id.tv_temp);
            TextView tvTime = view.findViewById(R.id.tv_time);
            tvTemp.setText(tempStr);
            tvTime.setText(time);
            radioGroup.addView(view);

        }


        dialog.show();

    }


    private Spannable getCookTimeStr(String content) {
        Spannable string = new SpannableString(content);
        int indexForH = content.indexOf("h");
        if (indexForH != -1) {
            string.setSpan(new RelativeSizeSpan(0.5f), indexForH, indexForH + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        int indexForMin = content.indexOf("min");
        if (indexForMin != -1) {
            string.setSpan(new RelativeSizeSpan(0.5f), indexForMin, indexForMin + 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }


        return string;
    }

    public class Bean {
        private String name;
        private String weight;
        private List<String> cookDatas = new ArrayList<>();

        public Bean(String name, String weight, List<String> cookDatas) {
            this.name = name;
            this.weight = weight;
            this.cookDatas = cookDatas;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public List<String> getCookDatas() {
            return cookDatas;
        }

        public void setCookDatas(List<String> cookDatas) {
            this.cookDatas = cookDatas;
        }
    }

    public interface OnHobCookingTablesDetailFragmentListener extends OnFragmentListener {
        void onHobCookingTablesDetailFragmentFinish();

        void onHobCookingTablesDetailFragmentStartToCook(int temp, int hour, int minute);

        void onHobCookingTablesDetailFragmentStartToCookNew(CookerSettingData data);
    }
}
