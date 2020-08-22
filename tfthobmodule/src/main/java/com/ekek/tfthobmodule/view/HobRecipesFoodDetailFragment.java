package com.ekek.tfthobmodule.view;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.commonmodule.base.BaseFragment;
import com.ekek.tfthobmodule.R;
import com.ekek.tfthobmodule.data.CookerSettingData;
import com.ekek.tfthobmodule.database.SettingDbHelper;
import com.ekek.tfthobmodule.event.DrawerEvent;
import com.ekek.viewmodule.recipe.RecipeView;
import com.ekek.viewmodule.recipe.recipes.RecipeAlcachofas;
import com.ekek.viewmodule.recipe.recipes.RecipeBacalao;
import com.ekek.viewmodule.recipe.recipes.RecipeCoastillar;
import com.ekek.viewmodule.recipe.recipes.RecipeEntrecotte;
import com.ekek.viewmodule.recipe.recipes.RecipeEsparragos;
import com.ekek.viewmodule.recipe.recipes.RecipeFlande;
import com.ekek.viewmodule.recipe.recipes.RecipeGelee;
import com.ekek.viewmodule.recipe.recipes.RecipeHuevos;
import com.ekek.viewmodule.recipe.recipes.RecipeLubina;
import com.ekek.viewmodule.recipe.recipes.RecipeMerluza;
import com.ekek.viewmodule.recipe.recipes.RecipePanceta;
import com.ekek.viewmodule.recipe.recipes.RecipeParada;
import com.ekek.viewmodule.recipe.recipes.RecipePechuga;
import com.ekek.viewmodule.recipe.recipes.RecipeQuinoa;
import com.ekek.viewmodule.recipe.recipes.RecipeSalmonColiflor;
import com.ekek.viewmodule.recipe.recipes.RecipeSetas;
import com.ekek.viewmodule.recipe.recipes.RecipeSolomillo;
import com.ekek.viewmodule.recipe.recipes.RecipeSopade;
import com.ekek.viewmodule.recipe.recipes.RecipeTostada;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStep;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStepAction;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class HobRecipesFoodDetailFragment extends BaseFragment implements RecipeView.RecipeStepActionClickListener {

    // Widgets
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.rvRecipe)
    RecipeView rvRecipe;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;

    // Fields
    private int recipeImageId;
    private String foodTypeName;
    private Map<Integer, Bitmap> bitmapMap = new ConcurrentHashMap<>();

    // Constructors
    public HobRecipesFoodDetailFragment() {
        this.recipeImageId = R.mipmap.food_type_my_recipe;
    }
    @SuppressLint("ValidFragment")
    public HobRecipesFoodDetailFragment(int recipeImageId, String foodTypeName) {
        this.recipeImageId = recipeImageId;
        this.foodTypeName = foodTypeName;
    }

    // Interfaces
    public interface OnHobRecipesFoodDetailFragmentListener extends OnFragmentListener {
        void onHobRecipesFoodDetailFragmentFinish();
        void OnHobRecipesFoodDetailFragmentRequestStartToCook(CookerSettingData data);
    }

    // Override functions
    @Override
    public int initLyaout() {
        return R.layout.tfthobmodule_fragment_hob_recipes_food_detail;
    }
    @Override
    public void initListener() {

    }
    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        rvRecipe.setListener(this);
        tvBack.setTypeface(GlobalVars.getInstance().getDefaultFontBold());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbindDrawables(getView());
        recycleBitmaps();
        System.gc();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            registerEventBus();
            switch (recipeImageId) {
                case R.mipmap.food_recipe_tostada:
                    rvRecipe.setRecipe(new RecipeTostada());
                    break;
                case R.mipmap.food_recipe_huevos:
                    rvRecipe.setRecipe(new RecipeHuevos());
                    break;
                case R.mipmap.food_recipe_salmon_coliflor:
                    rvRecipe.setRecipe(new RecipeSalmonColiflor());
                    break;
                case R.mipmap.food_recipe_lubina:
                    rvRecipe.setRecipe(new RecipeLubina());
                    break;
                case R.mipmap.food_recipe_quinoa:
                    rvRecipe.setRecipe(new RecipeQuinoa());
                    break;
                case R.mipmap.food_recipe_gelee:
                    rvRecipe.setRecipe(new RecipeGelee());
                    break;
                case R.mipmap.food_recipe_setas:
                    rvRecipe.setRecipe(new RecipeSetas());
                    break;
                case R.mipmap.food_recipe_alcachofas:
                    rvRecipe.setRecipe(new RecipeAlcachofas());
                    break;
                case R.mipmap.food_recipe_coastillar:
                    rvRecipe.setRecipe(new RecipeCoastillar());
                    break;
                case R.mipmap.food_recipe_parada:
                    rvRecipe.setRecipe(new RecipeParada());
                    break;
                case R.mipmap.food_recipe_esparragos:
                    rvRecipe.setRecipe(new RecipeEsparragos());
                    break;
                case R.mipmap.food_recipe_pechuga:
                    rvRecipe.setRecipe(new RecipePechuga());
                    break;
                case R.mipmap.food_recipe_sopa_de:
                    rvRecipe.setRecipe(new RecipeSopade());
                    break;
                case R.mipmap.food_recipe_solomillo:
                    rvRecipe.setRecipe(new RecipeSolomillo());
                    break;
                case R.mipmap.food_recipe_flan_de:
                    rvRecipe.setRecipe(new RecipeFlande());
                    break;
                case R.mipmap.food_recipe_entrecotte:
                    rvRecipe.setRecipe(new RecipeEntrecotte());
                    break;
                case R.mipmap.food_recipe_panceta:
                    rvRecipe.setRecipe(new RecipePanceta());
                    break;
                case R.mipmap.food_recipe_bacalao:
                    rvRecipe.setRecipe(new RecipeBacalao());
                    break;
                case R.mipmap.food_recipe_merluza:
                    rvRecipe.setRecipe(new RecipeMerluza());
                    break;
            }
        } else {
            unregisterEventBus();
        }
    }

    // Event handlers
    @OnClick({R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                ((OnHobRecipesFoodDetailFragmentListener) mListener).onHobRecipesFoodDetailFragmentFinish();
                break;
        }
    }
    @Override
    public void onRecipeStepActionClick(View view, RecipeStep step, RecipeStepAction action) {
        startCook(action);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DrawerEvent event){
        if (!isVisible()) return;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)llContainer.getLayoutParams();
        int pSmall = getActivity().getResources().getDimensionPixelSize(R.dimen.tfthobmodule_recipe_margin_end_small);
        int pLarge = getActivity().getResources().getDimensionPixelSize(R.dimen.tfthobmodule_recipe_margin_end_big);
        switch (event.getEvent()) {
            case DrawerEvent.EVENT_DRAWER_CLOSED:
                layoutParams.setMarginEnd(pSmall);
                llContainer.setLayoutParams(layoutParams);
                break;
            case DrawerEvent.EVENT_DRAWER_OPENED:
                layoutParams.setMarginEnd(pLarge);
                llContainer.setLayoutParams(layoutParams);
                break;
        }
    }

    // Private functions
    private void startCook(RecipeStepAction action) {
        CookerSettingData data =  new CookerSettingData();
        data.setCookerSettingMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR_WITHOUT_SENSOR);

        data.setTempValue(action.getTemperature());
        data.setTimerMinuteValue(action.getMinute());
        data.setTimerHourValue(action.getHour());
        data.setTimerSecondValue(59);//60秒
        data.setTempMode(CookerSettingData.SETTING_MODE_FAST_TEMP_GEAR);
        data.setTempShowOrder(CookerSettingData.TEMP_RECIPES_SHOW_ORDER_RECIPES_FIRST);
        data.setTempRecipesResID(recipeImageId);
        data.setCookerID(SettingDbHelper.getRecipesCookerID());
        ((OnHobRecipesFoodDetailFragmentListener) mListener).OnHobRecipesFoodDetailFragmentRequestStartToCook(data);
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

    // Properties
    public void setRecipeImageId(int recipeImageId) {
        this.recipeImageId = recipeImageId;
    }
    public void setFoodTypeName(String foodTypeName) {
        this.foodTypeName = foodTypeName;
    }
}
