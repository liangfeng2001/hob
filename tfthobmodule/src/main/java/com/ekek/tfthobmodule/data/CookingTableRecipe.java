package com.ekek.tfthobmodule.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CookingTableRecipe {

    private String nameStr;
    private String weightStr;
    private List<HashMap<Integer,Integer>> cookTypes = new ArrayList<>();

    public CookingTableRecipe(String nameStr, String weightStr, List<HashMap<Integer, Integer>> cookTypes) {
        this.nameStr = nameStr;
        this.weightStr = weightStr;
        this.cookTypes = cookTypes;
    }

    public String getNameStr() {
        return nameStr;
    }

    public void setNameStr(String nameStr) {
        this.nameStr = nameStr;
    }

    public String getWeightStr() {
        return weightStr;
    }

    public void setWeightStr(String weightStr) {
        this.weightStr = weightStr;
    }

    public List<HashMap<Integer, Integer>> getCookTypes() {
        return cookTypes;
    }

    public void setCookTypes(List<HashMap<Integer, Integer>> cookTypes) {
        this.cookTypes = cookTypes;
    }

    public List<HashMap<String,String>> getCookTypesContent() {
        List<HashMap<String,String>> list = new ArrayList<>();
        for (int i = 0;i < cookTypes.size();i++) {
            //cookTypes.get(i).

        }

        return list;
    }
}
