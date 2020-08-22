package com.ekek.viewmodule.recipe.recipes.builtin;

public class RecipeStepAction {

    // Fields
    private int actionId;
    private int stepId;
    private int actionName;
    private int actionImage;
    private int temperature;
    private int hour;
    private int minute;
    private int seconds;

    // Constructors
    public RecipeStepAction(
            int actionId,
            int stepId,
            int actionName,
            int actionImage,
            int temperature,
            int hour,
            int minute,
            int seconds) {
        this.actionId = actionId;
        this.stepId = stepId;
        this.actionName = actionName;
        this.actionImage = actionImage;
        this.temperature = temperature;
        this.hour = hour;
        this.minute = minute;
        this.seconds = seconds;
    }

    // Properties
    public int getActionId() {
        return actionId;
    }
    public void setActionId(int actionId) {
        this.actionId = actionId;
    }
    public int getStepId() {
        return stepId;
    }
    public void setStepId(int stepId) {
        this.stepId = stepId;
    }
    public int getActionName() {
        return actionName;
    }
    public void setActionName(int actionName) {
        this.actionName = actionName;
    }
    public int getActionImage() {
        return actionImage;
    }
    public void setActionImage(int actionImage) {
        this.actionImage = actionImage;
    }
    public int getTemperature() {
        return temperature;
    }
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getMinute() {
        return minute;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }
    public int getSeconds() {
        return seconds;
    }
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
