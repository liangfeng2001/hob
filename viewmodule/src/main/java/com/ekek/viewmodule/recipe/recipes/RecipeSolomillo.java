package com.ekek.viewmodule.recipe.recipes;

import com.ekek.viewmodule.R;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeAttribute;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeAttributeLen;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStep;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStepAction;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStepGroup;

public class RecipeSolomillo extends BaseRecipe {

    @Override
    protected int getNameResource() {
        return R.string.viewmodule_food_recipe_solomillo;
    }

    @Override
    protected int getTitleResource() {
        return R.string.viewmodule_recipe_solomillo_title;
    }

    @Override
    protected int getImageResource() {
        return R.mipmap.food_recipe_solomillo;
    }

    @Override
    protected int getNoteResource() {
        return R.string.viewmodule_recipe_solomillo_note;
    }

    @Override
    protected void initAttributes() {
        attributes.clear();
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_difficulty,
                R.string.viewmodule_recipe_solomillo_difficulty,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_cooking_time,
                R.string.viewmodule_recipe_solomillo_cooking_time,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_type,
                R.string.viewmodule_recipe_solomillo_type,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_num_of_people,
                R.string.viewmodule_recipe_solomillo_num_of_people,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_allergens,
                R.string.viewmodule_recipe_solomillo_allergens,
                RecipeAttributeLen.Long));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_ingredients,
                R.string.viewmodule_recipe_solomillo_ingredients,
                RecipeAttributeLen.Long));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_what_you_need,
                R.string.viewmodule_recipe_solomillo_what_you_need,
                RecipeAttributeLen.Long));
    }
    @Override
    protected void initSteps() {
        stepGroups.clear();
        RecipeStepGroup group = new RecipeStepGroup();
        group.setGroupName(R.string.viewmodule_recipe_step_group_solomillo_1);
        int loop = 1;
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_1_1,
                R.string.viewmodule_recipe_step_solomillo_1_1,
                null));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_1_2,
                R.string.viewmodule_recipe_step_solomillo_1_2,
                null));
        RecipeStepAction action = new RecipeStepAction(
                stepActions.size() + 1,
                loop,
                R.string.viewmodule_recipe_step_action_name_general_1_3,
                R.mipmap.recipe_step_action_start,
                55,
                0,
                20,
                0);
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_1_3,
                R.string.viewmodule_recipe_step_solomillo_1_3,
                action));
        stepGroups.add(group);

        group = new RecipeStepGroup();
        group.setGroupName(R.string.viewmodule_recipe_step_group_solomillo_2);
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_2_1,
                R.string.viewmodule_recipe_step_solomillo_2_1,
                null));
        stepGroups.add(group);

        group = new RecipeStepGroup();
        group.setGroupName(R.string.viewmodule_recipe_step_group_solomillo_3);
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_3_1,
                R.string.viewmodule_recipe_step_solomillo_3_1,
                null));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_3_2,
                R.string.viewmodule_recipe_step_solomillo_3_2,
                null));
        stepGroups.add(group);

        group = new RecipeStepGroup();
        group.setGroupName(R.string.viewmodule_recipe_step_group_solomillo_4);
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_4_1,
                R.string.viewmodule_recipe_step_solomillo_4_1,
                null));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_4_2,
                R.string.viewmodule_recipe_step_solomillo_4_2,
                null));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_4_3,
                R.string.viewmodule_recipe_step_solomillo_4_3,
                null));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_4_4,
                R.string.viewmodule_recipe_step_solomillo_4_4,
                null));
        stepGroups.add(group);
    }
}
