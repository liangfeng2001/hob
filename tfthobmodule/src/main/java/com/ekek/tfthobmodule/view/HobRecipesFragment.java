package com.ekek.tfthobmodule.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.tfthobmodule.R;
import com.ekek.tfthobmodule.entity.FoodType;
import com.ekek.tfthobmodule.utils.SpaceItemDecoration;
import com.ekek.viewmodule.common.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class HobRecipesFragment extends BaseFragment {

    // Widgets
    @BindView(R.id.rv_food_type)
    RecyclerView rvFoodType;
    @BindView(R.id.tv_back)
    TextView tvBack;

    // Fields
    private List<FoodType> foodTypes = new ArrayList<>();
    private FoodTypeRecyclerAdapter adapter;
    private Map<Integer, Bitmap> bitmapMap = new ConcurrentHashMap<>();

    // Interfaces
    public interface OnHobRecipesFragmentListener extends OnFragmentListener {
        void onHobRecipesFragmentSelect(int image, String foodTypeName);
        void onHobRecipesFragmentFinish();
    }

    // Override functions
    @Override
    public int initLyaout() {
        return R.layout.tfthobmodule_fragment_hob_recipes;
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
        tvBack.setTypeface(GlobalVars.getInstance().getDefaultFontBold());
        reloadData();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        tvBack.setTypeface(GlobalVars.getInstance().getDefaultFontBold());
        tvBack.setText(R.string.tfthobmodule_title_back);
        reloadData();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbindDrawables(getView());
        recycleBitmaps();
        System.gc();
    }

    // Event handlers
    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        ((OnHobRecipesFragmentListener) mListener).onHobRecipesFragmentFinish();
    }

    // Private functions
    private void reloadData() {
        String[] foodTypeNames = getResources().getStringArray(R.array.tfthobmodule_foodtype_names);
        int[] foodTypeImages = new int[foodTypeNames.length];
        TypedArray mTypedArray = getResources().obtainTypedArray(R.array.tfthobmodule_foodtype_images);
        for (int i = 0; i < getResources().getIntArray(R.array.tfthobmodule_foodtype_images).length; i++) {
            int imageId = mTypedArray.getResourceId(i, R.mipmap.food_type_meat);
            foodTypeImages[i] = imageId;

        }
        mTypedArray.recycle();

        foodTypes.clear();
        for (int i = 0; i < foodTypeNames.length; i++) {
            FoodType foodType = new FoodType(foodTypeImages[i], foodTypeNames[i]);
            foodTypes.add(foodType);
        }
        adapter = new FoodTypeRecyclerAdapter(getActivity());
        rvFoodType.setAdapter(adapter);
    }
    private void processClickEvent(int position) {
        doSelect(
                foodTypes.get(position).getImageResourceID(),
                foodTypes.get(position).getName());
    }
    private void doSelect(int image, String foodTypeName) {
        ((OnHobRecipesFragmentListener) mListener).onHobRecipesFragmentSelect(image, foodTypeName);
    }
    private Bitmap getBitmap(int source) {
        if (bitmapMap.containsKey(source)) {
            return bitmapMap.get(source);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), source);
        bitmapMap.put(source, bitmap);
        return bitmap;
    }
    private void recycleBitmaps() {
        for (int r: bitmapMap.keySet()) {
            bitmapMap.remove(r).recycle();
        }
    }

    // Classes
    public class FoodTypeRecyclerAdapter extends RecyclerView.Adapter<FoodTypeRecyclerAdapter.ViewHolder> {

        private LayoutInflater layoutInflater;

        public FoodTypeRecyclerAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.tfthobmodule_layout_food_type, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.foodTypeImage.setImageBitmap(getBitmap(foodTypes.get(position).getImageResourceID()));

            String name = foodTypes.get(position).getName();
            if (name.startsWith("Ruoanlaittotaulukot")) {
                holder.foodTypeName.setTextSize(30.0f);
            } else {
                holder.foodTypeName.setTextSize(32.0f);
            }
            holder.foodTypeName.setText(name);
        }

        @Override
        public int getItemCount() {
            return foodTypes.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private RoundedImageView foodTypeImage;

            private TextView foodTypeName;

            ViewHolder(View itemView) {
                super(itemView);
                foodTypeImage = (RoundedImageView) itemView.findViewById(R.id.iv_food_type);
                foodTypeImage.setOnClickListener(this);
                foodTypeName = (TextView) itemView.findViewById(R.id.tv_food_type);
                foodTypeName.setTypeface(GlobalVars.getInstance().getDefaultFontBold());
            }

            @Override
            public void onClick(View view) {
                processClickEvent(getAdapterPosition());
            }
        }

    }
}
