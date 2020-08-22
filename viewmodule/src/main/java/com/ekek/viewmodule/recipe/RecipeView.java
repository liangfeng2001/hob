package com.ekek.viewmodule.recipe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ekek.commonmodule.GlobalVars;
import com.ekek.viewmodule.R;
import com.ekek.viewmodule.common.RoundedImageView;
import com.ekek.viewmodule.recipe.recipes.BaseRecipe;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeAttribute;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeAttributeLen;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStep;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStepAction;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStepGroup;

import java.util.HashMap;
import java.util.Map;

public class RecipeView extends LinearLayout {

    // Widgets
    protected ScrollView svRecipes;
    protected LinearLayout llContainer;
    protected ConstraintLayout clImage;
    protected RoundedImageView ivRecipe;
    protected TextView tvRecipeName;

    // Constants
    private static final float DEFAULT_TITLE_FONT_SIZE = 48.0f;
    private static final float DEFAULT_ATTR_FONT_SIZE = 32.0f;
    private static final float DEFAULT_STEP_FONT_SIZE = 32.0f;

    // Fields
    protected Context context;
    protected float titleFontSize;
    protected int titleFontColor;
    protected float attrFontSize;
    protected int attrFontColor;
    protected float stepFontSize;
    protected int stepFontColor;
    protected float blockPadding;
    protected float innerPadding;
    protected boolean showImage;
    protected boolean showTitle;

    protected BaseRecipe recipe;
    protected Typeface fontNormal;
    protected Typeface fontBold;
    private RecipeStepActionClickListener listener;
    private Map<Integer, Bitmap> bitmapMap = new HashMap<>();

    // Constructors
    public RecipeView(
            Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initFields(attrs);
        initLayout(attrs);
    }

    // Interfaces
    public interface RecipeStepActionClickListener {
        void onRecipeStepActionClick(View view, RecipeStep step, RecipeStepAction action);
    }

    // Private functions
    private void initFields(AttributeSet attrs) {
        fontNormal = GlobalVars.getInstance().getDefaultFontRegular();
        fontBold = GlobalVars.getInstance().getDefaultFontBold();

        @SuppressLint("CustomViewStyleable")
        TypedArray array = context.obtainStyledAttributes(
                attrs, R.styleable.viewmodule_RecipeView);

        titleFontSize = array.getDimension(R.styleable.viewmodule_RecipeView_viewmodule_title_size, DEFAULT_TITLE_FONT_SIZE);
        titleFontColor = array.getColor(R.styleable.viewmodule_RecipeView_viewmodule_title_color, Color.WHITE);
        attrFontSize = array.getDimension(R.styleable.viewmodule_RecipeView_viewmodule_attr_size, DEFAULT_ATTR_FONT_SIZE);
        attrFontColor = array.getColor(R.styleable.viewmodule_RecipeView_viewmodule_attr_color, Color.WHITE);
        stepFontSize = array.getDimension(R.styleable.viewmodule_RecipeView_viewmodule_step_size, DEFAULT_STEP_FONT_SIZE);
        stepFontColor = array.getColor(R.styleable.viewmodule_RecipeView_viewmodule_step_color, Color.WHITE);
        blockPadding = array.getDimension(R.styleable.viewmodule_RecipeView_viewmodule_block_padding, 0.0f);
        innerPadding = array.getDimension(R.styleable.viewmodule_RecipeView_viewmodule_inner_padding, 0.0f);
        showImage = array.getBoolean(R.styleable.viewmodule_RecipeView_viewmodule_show_image, true);
        showTitle = array.getBoolean(R.styleable.viewmodule_RecipeView_viewmodule_show_title, true);

        array.recycle();
    }
    private void initLayout(AttributeSet attrs) {
        View v = inflate(context,
                R.layout.viewmodule_recipe_view, null);
        v.setLayoutParams(new LayoutParams(context, attrs));
        this.addView(v, 0);
        svRecipes = (ScrollView)v.findViewById(R.id.sv_recipes);
        llContainer = (LinearLayout)v.findViewById(R.id.ll_container);
        clImage = (ConstraintLayout)v.findViewById(R.id.cl_image);
        clImage.setVisibility(showImage ? View.VISIBLE : View.GONE);
        ivRecipe = (RoundedImageView)v.findViewById(R.id.iv_recipe);
        tvRecipeName = (TextView)v.findViewById(R.id.tv_recipe_name);
    }
    private void createLayout() {
        ivRecipe.setImageBitmap(getBitmap(recipe.getImage()));
        tvRecipeName.setTypeface(fontBold);
        tvRecipeName.setText(recipe.getName());
        llContainer.removeAllViews();
        createTitleLayout();
        createAttrsLayout();
        createStepsLayout();
        createNoteLayout();
        svRecipes.scrollTo(0,0);
    }
    private void createTitleLayout() {
        if (showTitle) {
            TextView tv = new TextView(context);
            tv.setTypeface(fontBold);
            tv.setText(Html.fromHtml(context.getResources().getString(recipe.getTitle())));
            tv.setTextSize(titleFontSize);
            tv.setTextColor(titleFontColor);
            tv.setPadding(0, 0, 0, (int) blockPadding);
            llContainer.addView(tv);
        }
    }
    private void createAttrsLayout() {
        for (RecipeAttribute attr: recipe.getAttributes()) {

            TextView tvTitle = new TextView(context);
            tvTitle.setTypeface(fontBold);
            tvTitle.setText(Html.fromHtml(context.getResources().getString(attr.getTitle())));
            tvTitle.setTextSize(attrFontSize);
            tvTitle.setTextColor(attrFontColor);

            TextView tvContent = new TextView(context);
            tvContent.setTypeface(fontNormal);
            tvContent.setText(Html.fromHtml(context.getResources().getString(attr.getContent())));
            tvContent.setTextSize(attrFontSize);
            tvContent.setTextColor(attrFontColor);

            LinearLayout ll = new LinearLayout(context);
            LayoutParams layoutParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(layoutParams);
            if (attr.getRecipeAttributeLen() == RecipeAttributeLen.Long) {
                ll.setOrientation(VERTICAL);
            } else {
                ll.setOrientation(HORIZONTAL);
                tvContent.setPadding((int)innerPadding, 0, 0, 0);
            }

            ll.addView(tvTitle);
            ll.addView(tvContent);
            ll.setPadding(0,0,0, (int)blockPadding);
            llContainer.addView(ll);
        }
    }
    private void createStepsLayout() {
        for (RecipeStepGroup group: recipe.getStepGroups()) {
            if (group.getGroupName() != R.string.viewmodule_recipe_step_group_name_empty) {
                TextView tvTitle = new TextView(context);
                tvTitle.setTypeface(fontNormal);
                tvTitle.setText(Html.fromHtml(context.getResources().getString(group.getGroupName())));
                tvTitle.setTextSize(stepFontSize);
                tvTitle.setTextColor(stepFontColor);
                tvTitle.setPadding(0,0,0, (int)blockPadding);
                llContainer.addView(tvTitle);
            }
            for (final RecipeStep step: group.getSteps()) {

                LinearLayout ll = new LinearLayout(context);
                LayoutParams layoutParams = new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                ll.setLayoutParams(layoutParams);
                ll.setOrientation(VERTICAL);
                ll.setPadding(0,0,0, (int)blockPadding);

                TextView tvTitle = new TextView(context);
                tvTitle.setTypeface(fontBold);
                tvTitle.setText(Html.fromHtml(context.getResources().getString(step.getTitle())));
                tvTitle.setTextSize(stepFontSize);
                tvTitle.setTextColor(stepFontColor);
                ll.addView(tvTitle);

                TextView tvContent = new TextView(context);
                tvContent.setTypeface(fontNormal);
                tvContent.setText(Html.fromHtml(context.getResources().getString(step.getContent())));
                tvContent.setTextSize(stepFontSize);
                tvContent.setTextColor(stepFontColor);
                ll.addView(tvContent);

                if (step.getAction() != null) {
                    final RecipeStepAction action = step.getAction();
                    LinearLayout ll2 = new LinearLayout(context);
                    LayoutParams layoutParams2 = new LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    ll2.setLayoutParams(layoutParams2);
                    ll2.setOrientation(HORIZONTAL);
                    ll2.setGravity(Gravity.CENTER_HORIZONTAL);

                    TextView tvTitle2 = new TextView(context);
                    tvTitle2.setTypeface(fontBold);
                    tvTitle2.setText(Html.fromHtml(context.getResources().getString(action.getActionName())));
                    tvTitle2.setTextSize(stepFontSize);
                    tvTitle2.setTextColor(stepFontColor);
                    tvTitle2.setGravity(Gravity.CENTER_VERTICAL);
                    tvTitle2.setPadding(
                            0,
                            (int)blockPadding,
                            0,
                            (int)blockPadding
                    );
                    Drawable drawable = context.getResources().getDrawable(action.getActionImage());
                    drawable.setBounds(0, 0, 72, 72);
                    tvTitle2.setCompoundDrawables(
                            null,
                            null,
                            drawable,
                            null);
                    tvTitle2.setCompoundDrawablePadding((int)blockPadding);
                    tvTitle2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onRecipeStepActionClick(v, step, action);
                            }
                        }
                    });
                    ll2.addView(tvTitle2);

                    ll.addView(ll2);
                }

                llContainer.addView(ll);
            }
        }
    }
    private void createNoteLayout() {
        if (recipe.getNote() != R.string.viewmodule_recipe_note_empty) {
            LinearLayout ll = new LinearLayout(context);
            LayoutParams layoutParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(layoutParams);
            ll.setOrientation(VERTICAL);
            ll.setPadding(0,0,0, (int)blockPadding);

            TextView tvTitle = new TextView(context);
            tvTitle.setTypeface(fontBold);
            tvTitle.setText(Html.fromHtml(context.getResources().getString(R.string.viewmodule_recipe_note_title)));
            tvTitle.setTextSize(stepFontSize);
            tvTitle.setTextColor(stepFontColor);
            ll.addView(tvTitle);

            TextView tvContent = new TextView(context);
            tvContent.setTypeface(fontNormal);
            tvContent.setText(Html.fromHtml(context.getResources().getString(recipe.getNote())));
            tvContent.setTextSize(stepFontSize);
            tvContent.setTextColor(stepFontColor);
            ll.addView(tvContent);

            llContainer.addView(ll);
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
    public BaseRecipe getRecipe() {
        return recipe;
    }
    public void setRecipe(BaseRecipe recipe) {
        this.recipe = recipe;
        createLayout();
    }
    public void setListener(RecipeStepActionClickListener listener) {
        this.listener = listener;
    }
}
