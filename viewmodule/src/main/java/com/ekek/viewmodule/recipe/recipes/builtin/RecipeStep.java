package com.ekek.viewmodule.recipe.recipes.builtin;

public class RecipeStep {

    // Fields
    private int id;
    private int title;
    private int content;
    private RecipeStepAction action;

    // Constructors
    public RecipeStep(
            int id,
            int title,
            int content,
            RecipeStepAction action) {
        setId(id);
        setTitle(title);
        setContent(content);
        setAction(action);
    }

    // Properties
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
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
    public RecipeStepAction getAction() {
        return action;
    }
    public void setAction(RecipeStepAction action) {
        this.action = action;
        if (action != null) {
            this.action.setStepId(id);
        }
    }
}
