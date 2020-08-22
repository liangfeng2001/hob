package com.ekek.viewmodule.recipe.recipes;

import com.ekek.viewmodule.R;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeAttribute;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeAttributeLen;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStep;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStepAction;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStepGroup;

public class RecipeEsparragos extends BaseRecipe {

    @Override
    protected int getNameResource() {
        return R.string.viewmodule_food_recipe_esparragos;
    }

    @Override
    protected int getTitleResource() {
        return R.string.viewmodule_recipe_esparragos_title;
    }

    @Override
    protected int getImageResource() {
        return R.mipmap.food_recipe_esparragos;
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
                R.string.viewmodule_recipe_esparragos_difficulty,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_cooking_time,
                R.string.viewmodule_recipe_esparragos_cooking_time,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_type,
                R.string.viewmodule_recipe_esparragos_type,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_num_of_people,
                R.string.viewmodule_recipe_esparragos_num_of_people,
                RecipeAttributeLen.Short));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_allergens,
                R.string.viewmodule_recipe_esparragos_allergens,
                RecipeAttributeLen.Long));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_ingredients,
                R.string.viewmodule_recipe_esparragos_ingredients,
                RecipeAttributeLen.Long));
        attributes.add(new RecipeAttribute(
                R.string.viewmodule_recipe_attr_what_you_need,
                R.string.viewmodule_recipe_esparragos_what_you_need,
                RecipeAttributeLen.Long));
    }
    @Override
    protected void initSteps() {
        stepGroups.clear();
        RecipeStepGroup group = new RecipeStepGroup();
        group.setGroupName(R.string.viewmodule_recipe_step_group_esparragos_1);
        int loop = 1;
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_1_1,
                R.string.viewmodule_recipe_step_esparragos_1_1,
                null));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_1_2,
                R.string.viewmodule_recipe_step_esparragos_1_2,
                null));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_1_3,
                R.string.viewmodule_recipe_step_esparragos_1_3,
                null));
        stepGroups.add(group);

        group = new RecipeStepGroup();
        group.setGroupName(R.string.viewmodule_recipe_step_group_esparragos_2);
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_2_1,
                R.string.viewmodule_recipe_step_esparragos_2_1,
                null));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_2_2,
                R.string.viewmodule_recipe_step_esparragos_2_2,
                null));
        RecipeStepAction action = new RecipeStepAction(
                stepActions.size() + 1,
                loop,
                R.string.viewmodule_recipe_step_action_name_general_2_3,
                R.mipmap.recipe_step_action_start,
                85,
                0,
                45,
                0);
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_2_3,
                R.string.viewmodule_recipe_step_esparragos_2_3,
                action));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_2_4,
                R.string.viewmodule_recipe_step_esparragos_2_4,
                null));
        stepGroups.add(group);

        group = new RecipeStepGroup();
        group.setGroupName(R.string.viewmodule_recipe_step_group_esparragos_3);
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_3_1,
                R.string.viewmodule_recipe_step_esparragos_3_1,
                null));
        group.getSteps().add(new RecipeStep(
                loop++,
                R.string.viewmodule_recipe_step_name_general_3_2,
                R.string.viewmodule_recipe_step_esparragos_3_2,
                null));
        stepGroups.add(group);
    }
}

