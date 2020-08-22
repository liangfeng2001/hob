package com.ekek.viewmodule.recipe.recipes.builtin;

import com.ekek.viewmodule.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepGroup {

    // Fields
    private int groupName;
    private List<RecipeStep> steps;

    // Constructors
    public RecipeStepGroup() {
        groupName = R.string.viewmodule_recipe_step_group_name_empty;
        steps = new ArrayList<>();
    }

    // Properties
    public int getGroupName() {
        return groupName;
    }
    public void setGroupName(int groupName) {
        this.groupName = groupName;
    }
    public List<RecipeStep> getSteps() {
        return steps;
    }
    public void setSteps(List<RecipeStep> steps) {
        this.steps = steps;
    }
}
