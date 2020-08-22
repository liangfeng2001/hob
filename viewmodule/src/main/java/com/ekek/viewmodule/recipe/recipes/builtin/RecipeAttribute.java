package com.ekek.viewmodule.recipe.recipes.builtin;

public class RecipeAttribute {

    // Fields
    private int title;
    private int content;
    private RecipeAttributeLen recipeAttributeLen;

    // Constructors
    public RecipeAttribute(int title, int content, RecipeAttributeLen recipeAttributeLen) {
        this.title = title;
        this.content = content;
        this.recipeAttributeLen = recipeAttributeLen;
    }

    // Properties
    public int getTitle() {
        return title;
    }
    public void setTitle(int title) {
        this.title = title;
    }
    public int getContent() {
        return content;
    }
    public void setContent(int content) {
        this.content = content;
    }
    public RecipeAttributeLen getRecipeAttributeLen() {
        return recipeAttributeLen;
    }
    public void setRecipeAttributeLen(RecipeAttributeLen recipeAttributeLen) {
        this.recipeAttributeLen = recipeAttributeLen;
    }
}
