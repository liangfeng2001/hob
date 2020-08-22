package com.ekek.tfthobmodule.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.commonmodule.utils.LogUtil;
import com.ekek.settingmodule.constants.CataSettingConstant;
import com.ekek.settingmodule.model.ShowBannerInformation;
import com.ekek.tfthobmodule.CataTFTHobApplication;
import com.ekek.tfthobmodule.R;
import com.ekek.tfthobmodule.entity.MyRecipesTable;
import com.ekek.tfthobmodule.entity.MyRecipesTableDao;
import com.ekek.tfthobmodule.utils.SpaceItemDecoration;
import com.ekek.viewmodule.common.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class HobMyRecipesFragment extends BaseFragment {
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.iv_new_recipes)
    ImageView ivNewRecipes;
    @BindView(R.id.ll_new_recipes)
    LinearLayout llNewRecipes;
    @BindView(R.id.rv_food_type)
    RecyclerView rvFoodType;
    private List<MyRecipesTable> myRecipesTableList = new ArrayList<>();
    private FoodTypeRecyclerAdapter adapter;
    private MyRecipesTable mMyRecipesTable;

    @Override
    public int initLyaout() {
        return R.layout.tfthobmodule_fragment_hob_my_recipes;
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvFoodType.setLayoutManager(linearLayoutManager);
        rvFoodType.addItemDecoration(new SpaceItemDecoration(30, 0));
        adapter = new FoodTypeRecyclerAdapter(getActivity());
        rvFoodType.setAdapter(adapter);
        Typeface bold = GlobalVars.getInstance().getDefaultFontBold();
        tvBack.setTypeface(bold);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initData();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        tvBack.setTypeface(GlobalVars.getInstance().getDefaultFontBold());
        tvBack.setText(R.string.tfthobmodule_title_back);
    }

    private void initData() {
        if (mMyRecipesTable == null) {
            mMyRecipesTable = new MyRecipesTable(null,"New",0,"",0,0,0,"");
        }
        myRecipesTableList.clear();
        myRecipesTableList.add(mMyRecipesTable);
        MyRecipesTableDao dao = CataTFTHobApplication.getDaoSession().getMyRecipesTableDao();
        myRecipesTableList.addAll(dao.loadAll());
        //myRecipesTableList = dao.loadAll();
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.tv_back, R.id.ll_new_recipes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                ((OnHobMyRecipesFragmentListener) mListener).onHobMyRecipesFragmentFinish();
                break;
            case R.id.ll_new_recipes:
                ((OnHobMyRecipesFragmentListener) mListener).onHobMyRecipesFragmentNewRecipes();
                break;
        }
    }

    public class FoodTypeRecyclerAdapter extends RecyclerView.Adapter<FoodTypeRecyclerAdapter.ViewHolder> {

        private LayoutInflater layoutInflater;
        private Map<Integer, Bitmap> bitmapMap = new HashMap<>();

        public FoodTypeRecyclerAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public FoodTypeRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.tfthobmodule_layout_food_type_ex, parent, false);
            return new FoodTypeRecyclerAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FoodTypeRecyclerAdapter.ViewHolder holder, int position) {

            MyRecipesTable recipesTable = myRecipesTableList.get(position);
            holder.foodTypeName.setText(recipesTable.getName());
            holder.foodTemp.setText(
                    String.format(
                            Locale.ENGLISH,
                            "%d°",
                            recipesTable.getTempValue()));
            holder.foodTimer.setText(
                    String.format(
                            Locale.ENGLISH,
                            "%d:%02d",
                            recipesTable.getHourValue(),
                            recipesTable.getMinuteValue()));
            if (recipesTable.getName().equals("New")) {
                holder.foodTypeImage.setImageBitmap(getBitmap(R.mipmap.image_new_my_recipes));
                holder.foodTypeImage.setRadiusX(0);
                holder.foodTypeImage.setRadiusY(0);
                holder.foodTypeImage.setStrokeWidth(0);
                holder.foodTypeName.setVisibility(View.INVISIBLE);
                holder.foodTemp.setVisibility(View.INVISIBLE);
                holder.foodTimer.setVisibility(View.INVISIBLE);
                holder.ivClose.setVisibility(View.INVISIBLE);
            }else {
                holder.foodTypeImage.setImageBitmap(getBitmap(R.mipmap.food_type_my_recipes));
            }
        }


        @Override
        public int getItemCount() {
            return myRecipesTableList.size();
        }

        private Bitmap getBitmap(int source) {
            if (bitmapMap.containsKey(source)) {
                return bitmapMap.get(source);
            }

            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), source);
            bitmapMap.put(source, bitmap);
            return bitmap;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private RoundedImageView foodTypeImage;
            private ImageView ivClose;

            private TextView foodTypeName;
            private TextView foodTemp;
            private TextView foodTimer;

            ViewHolder(View itemView) {
                super(itemView);
                foodTypeImage = (RoundedImageView) itemView.findViewById(R.id.iv_food_type);
                foodTypeImage.setOnClickListener(this);
                ivClose = (ImageView) itemView.findViewById(R.id.ivClose);
                ivClose.setOnClickListener(this);
                foodTypeName = (TextView) itemView.findViewById(R.id.tv_food_type);
                foodTemp = (TextView) itemView.findViewById(R.id.tv_food_temp);
                foodTimer = (TextView) itemView.findViewById(R.id.tv_food_timer);
                Typeface timeTypeface = GlobalVars.getInstance().getDefaultFontBold();
                foodTypeName.setTypeface(timeTypeface);
                foodTemp.setTypeface(timeTypeface);
                foodTimer.setTypeface(timeTypeface);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                switch (view.getId()) {
                    case R.id.iv_food_type:
                        processClickEvent(position);
                        break;
                    case R.id.ivClose:
                        EventBus.getDefault().post(new ShowBannerInformation(
                                CataSettingConstant.DELETE_MY_RECIPE,
                                position,
                                0));
                        break;
                }
            }
        }
    }

    private void processClickEvent(int position) {
        LogUtil.d("Enter:: processClickEvent------position" + position);
        if (position == 0) {
            ((OnHobMyRecipesFragmentListener)mListener).onHobMyRecipesFragmentNewRecipes();
        }else {
            ((OnHobMyRecipesFragmentListener)mListener).onHobMyRecipesFragmentRequestToCheckDetail(myRecipesTableList.get(position));
        }
    }

    public void deleteRecipe(int position) {
        MyRecipesTable recipesTable = myRecipesTableList.get(position);
        CataTFTHobApplication.getDaoSession().getMyRecipesTableDao().delete(recipesTable);
        myRecipesTableList.remove(recipesTable);
        adapter.notifyDataSetChanged();
    }

    public interface OnHobMyRecipesFragmentListener extends OnFragmentListener {
        void onHobMyRecipesFragmentNewRecipes();
        void onHobMyRecipesFragmentFinish();
        void onHobMyRecipesFragmentRequestToCheckDetail(MyRecipesTable myRecipesTable);

    }
}
