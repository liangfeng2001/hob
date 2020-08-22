package com.ekek.tfthobmodule.view;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.tfthobmodule.R;
import com.ekek.viewmodule.product.ProductManager;

import butterknife.BindView;
import butterknife.OnClick;

public class HobCookingTablesFragment extends BaseFragment {
    public static final int HOB_COOKING_TABLES_SELECT_MEAT = 0;
    public static final int HOB_COOKING_TABLES_SELECT_LEGUMES_AND_CEREALS = 1;
    public static final int HOB_COOKING_TABLES_SELECT_FRUIT = 2;
    public static final int HOB_COOKING_TABLES_SELECT_SHELLFISH = 3;
    public static final int HOB_COOKING_TABLES_SELECT_VEGETABLES = 4;
    public static final int HOB_COOKING_TABLES_SELECT_FISH = 5;


    @BindView(R.id.tv_meat)
    TextView tvMeat;
    @BindView(R.id.tv_legumes_and_cereals)
    TextView tvLegumesAndCereals;
    @BindView(R.id.tv_fruit)
    TextView tvFruit;
    @BindView(R.id.tv_shellfish)
    TextView tvShellfish;
    @BindView(R.id.tv_vegetables)
    TextView tvVegetables;
    @BindView(R.id.tv_fish)
    TextView tvFish;
    @BindView(R.id.tv_back)
    TextView tvBack;


    @Override
    public int initLyaout() {
        return R.layout.tfthobmodule_fragment_hob_cooking_tables;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        SetUI();
        initFonts();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        initFonts();
        tvBack.setText(R.string.tfthobmodule_title_back);
        tvMeat.setText(R.string.tfthobmodule_cooking_tables_type_meat);
        tvFish.setText(R.string.tfthobmodule_cooking_tables_type_fish);
        tvFruit.setText(R.string.tfthobmodule_cooking_tables_type_fruit);
        tvLegumesAndCereals.setText(R.string.tfthobmodule_cooking_tables_type_legumes_and_cereals);
        tvShellfish.setText(R.string.tfthobmodule_cooking_tables_type_shellfish);
        tvVegetables.setText(R.string.tfthobmodule_cooking_tables_type_vegetables);
    }

    private void SetUI() {
        if (ProductManager.PRODUCT_TYPE == ProductManager.Haier) {
            Drawable drawable_Meat = getResources().getDrawable(R.mipmap.ic_meat_haier);
            drawable_Meat.setBounds(0, 0, 116, 100);
            tvMeat.setCompoundDrawables(null, drawable_Meat, null, null);
            tvMeat.setPadding(0, 65, 0, 0);

            Drawable drawable_LegumesAndCereals = getResources().getDrawable(R.mipmap.ic_legumes_and_cereals_haier);
            drawable_LegumesAndCereals.setBounds(0, 0, 120, 138);
            tvLegumesAndCereals.setCompoundDrawables(null, drawable_LegumesAndCereals, null, null);

            Drawable drawable_Fruit = getResources().getDrawable(R.mipmap.ic_fruit_haier);
            drawable_Fruit.setBounds(0, 0, 91, 98);
            tvFruit.setCompoundDrawables(null, drawable_Fruit, null, null);
            tvFruit.setPadding(0, 70, 0, 0);

            Drawable drawable_Shellfish = getResources().getDrawable(R.mipmap.ic_shellfish_haier);
            drawable_Shellfish.setBounds(0, 0, 115, 102);
            tvShellfish.setCompoundDrawables(null, drawable_Shellfish, null, null);
            tvShellfish.setPadding(0, 50, 0, 0);

            Drawable drawable_Vegetables = getResources().getDrawable(R.mipmap.ic_vegetables_haier);
            drawable_Vegetables.setBounds(0, 0, 103, 116);
            tvVegetables.setCompoundDrawables(null, drawable_Vegetables, null, null);
            tvVegetables.setPadding(0, 33, 0, 0);

            Drawable drawable_tvFish = getResources().getDrawable(R.mipmap.ic_fish_haier);
            drawable_tvFish.setBounds(0, 0, 125, 54);
            tvFish.setCompoundDrawables(null, drawable_tvFish, null, null);
            tvFish.setPadding(0, 70, 0, 0);
            tvFish.setCompoundDrawablePadding(30);
        }
    }

    private void initFonts() {
        tvBack.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        tvMeat.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        tvFish.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        tvFruit.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        tvLegumesAndCereals.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        tvShellfish.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
        tvVegetables.setTypeface(GlobalVars.getInstance().getDefaultFontRegular());
    }

    @OnClick({R.id.tv_meat, R.id.tv_legumes_and_cereals, R.id.tv_fruit, R.id.tv_shellfish, R.id.tv_vegetables, R.id.tv_fish, R.id.tv_back})
    public void onViewClicked(View view) {
        LogUtil.d("Enter:: onViewClicked");
        switch (view.getId()) {
            case R.id.tv_meat:
                doSelect(HOB_COOKING_TABLES_SELECT_MEAT);
                break;
            case R.id.tv_legumes_and_cereals:
                doSelect(HOB_COOKING_TABLES_SELECT_LEGUMES_AND_CEREALS);

                break;
            case R.id.tv_fruit:
                doSelect(HOB_COOKING_TABLES_SELECT_FRUIT);

                break;
            case R.id.tv_shellfish:
                doSelect(HOB_COOKING_TABLES_SELECT_SHELLFISH);

                break;
            case R.id.tv_vegetables:
                doSelect(HOB_COOKING_TABLES_SELECT_VEGETABLES);

                break;
            case R.id.tv_fish:
                doSelect(HOB_COOKING_TABLES_SELECT_FISH);

                break;
            case R.id.tv_back:
                ((OnHobCookingTablesFragmentListener) mListener).onHobCookingTablesFragmentFinish();
                break;
        }
    }

    private void doSelect(int what) {
        ((OnHobCookingTablesFragmentListener) mListener).onHobCookingTablesFragmentSelect(what);
    }




    public interface OnHobCookingTablesFragmentListener extends OnFragmentListener {
        void onHobCookingTablesFragmentSelect(int what);
        void onHobCookingTablesFragmentFinish();

    }
}
