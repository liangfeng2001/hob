package com.ekek.viewmodule.recipe.recipes;

import com.ekek.viewmodule.R;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeAttribute;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeAttributeLen;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStep;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStepAction;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStepGroup;

public class RecipeAlcachofas extends BaseRecipe {

    @Override
    protected int getNameResource() {
        return R.string.viewmodule_food_recipe_alcachofas;
    }

    @Override
    protected int getTitleResource() {
        return R.string.viewmodule_recipe_alcachofa_title;
    }

    @Override
    protected int getImageResource() {
        return R.mipmap.food_recipe_alcachofas;
    }

    @Override
    protected int getNoteResource() {
        return R.string.viewmodule_recipe_alcachofa_note;
    }

    @Override
    protected void initAttributes() {
        attributes.clear();
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_difficulty,
                R.string.viewmodule_recipe_alcachofa_difficulty,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_cooking_time,
                R.string.viewmodule_recipe_alcachofa_cooking_time,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_type,
                R.string.viewmodule_recipe_alcachofa_type,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_num_of_people,
                R.string.viewmodule_recipe_alcachofa_num_of_people,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_allergens,
                R.string.viewmodule_recipe_alcachofa_allergens,
                RecipeAttributeLen.Long));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_ingredients,
                R.string.viewmodule_recipe_alcachofa_ingredients,
                RecipeAttributeLen.Long));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_what_you_need,
                R.string.viewmodule_recipe_alcachofa_what_you_need,
                RecipeAttributeLen.Long));
    }
    @Override
    protected void initSteps() {
        stepGroups.clear();
        RecipeStepGroup group = new RecipeStepGroup();
        int loop = 1;
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_1,
                R.string.viewmodule_recipe_step_alcachofa_1,
                null));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_2,
                R.string.viewmodule_recipe_step_alcachofa_2,
                null));
        RecipeStepAction action = new RecipeStepAction(
                stepActions.size() + 1,
                loop,
                R.string.viewmodule_recipe_step_action_name_general_3,
                R.mipmap.recipe_step_action_start,
                85,
                1,
                30,
                0);
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_3,
                R.string.viewmodule_recipe_step_alcachofa_3,
                action));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_4,
                R.string.viewmodule_recipe_step_alcachofa_4,
                null));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_5,
                R.string.viewmodule_recipe_step_alcachofa_5,
                null));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_6,
                R.string.viewmodule_recipe_step_alcachofa_6,
                null));
        stepGroups.add(group);
    }
}

