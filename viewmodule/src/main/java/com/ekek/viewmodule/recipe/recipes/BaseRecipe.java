package com.ekek.viewmodule.recipe.recipes;

import com.ekek.viewmodule.recipe.recipes.builtin.RecipeAttribute;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStepAction;
import com.ekek.viewmodule.recipe.recipes.builtin.RecipeStepGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecipe {
    // Fields
    protected int name;
    protected int title;
    protected int image;
    protected int note;
    protected List<RecipeAttribute> attributes;
    protected List<RecipeStepGroup> stepGroups;
    protected List<RecipeStepAction> stepActions;

    // Constructors
    public BaseRecipe() {
        attributes = new ArrayList<>();
        stepGroups = new ArrayList<>();
        stepActions = new ArrayList<>();

        name = getNameResource();
        title = getTitleResource();
        image = getImageResource();
        note = getNoteResource();

        initAttributes();
        initSteps();
    }

    // Abstract functions
    protected abstract int getNameResource();
    protected abstract int getTitleResource();
    protected abstract int getImageResource();
    protected abstract int getNoteResource();
    protected abstract void initAttributes();
    protected abstract void initSteps();

    // Public functions
    public int getName() {
        return name;
    }
    public int getTitle() {
        return title;
    }
    public int getImage() {
        return image;
    }
    public int getNote() {
        return note;
    }
    public List<RecipeAttribute> getAttributes() {
        return attributes;
    }
    public List<RecipeStepGroup> getStepGroups() {
        return stepGroups;
    }
    public List<RecipeStepAction> getStepActions() {
        return stepActions;
    }
}
