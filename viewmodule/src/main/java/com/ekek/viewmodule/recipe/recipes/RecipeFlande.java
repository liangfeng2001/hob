package com.ekek.viewmodule.recipe.recipes;

import com.ekek.viewmodule.R;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeAttribute;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeAttributeLen;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStep;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStepAction;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStepGroup;

public class RecipeFlande extends BaseRecipe {

    @Override
    protected int getNameResource() {
        return R.string.viewmodule_food_recipe_flan_de;
    }

    @Override
    protected int getTitleResource() {
        return R.string.viewmodule_recipe_flande_title;
    }

    @Override
    protected int getImageResource() {
        return R.mipmap.food_recipe_flan_de;
    }

    @Override
    protected int getNoteResource() {
        return R.string.viewmodule_recipe_note_empty;
    }

    @Override
    protected void initAttributes() {
        attributes.clear();
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_difficulty,
                R.string.viewmodule_recipe_flande_difficulty,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_cooking_time,
                R.string.viewmodule_recipe_flande_cooking_time,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_type,
                R.string.viewmodule_recipe_flande_type,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_num_of_people,
                R.string.viewmodule_recipe_flande_num_of_people,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_allergens,
                R.string.viewmodule_recipe_flande_allergens,
                RecipeAttributeLen.Long));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_ingredients,
                R.string.viewmodule_recipe_flande_ingredients,
                RecipeAttributeLen.Long));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_what_you_need,
                R.string.viewmodule_recipe_flande_what_you_need,
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
                R.string.viewmodule_recipe_step_flande_1,
                null));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_2,
                R.string.viewmodule_recipe_step_flande_2,
                null));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_3,
                R.string.viewmodule_recipe_step_flande_3,
                null));
        RecipeStepAction action = new RecipeStepAction(
                stepActions.size() + 1,
                loop,
                R.string.viewmodule_recipe_step_action_name_general_4,
                R.mipmap.recipe_step_action_start,
                85,
                1,
                0,
                0);
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_4,
                R.string.viewmodule_recipe_step_flande_4,
                action));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_5,
                R.string.viewmodule_recipe_step_flande_5,
                null));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_6,
                R.string.viewmodule_recipe_step_flande_6,
                null));
        stepGroups.add(group);
    }
}
