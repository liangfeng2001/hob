package com.ekek.tfthobmodule.entity;

import com.google.gson.annotations.Expose;

import java.util.List;

public class CookingTable {
    @Expose
    private String type;
    @Expose
    private String description;
    @Expose
    private List<Item> item;

    public CookingTable() {

    }
    public CookingTable(String type, String description, List<Item> items) {
        this.type = type;
        this.description = description;
        this.item = items;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Item> getItems() {
        return item;
    }

    public void setItems(List<Item> items) {
        this.item = items;
    }

    public class Item {
        @Expose
        private String name;
        @Expose
        private String weight;
        @Expose
        private List<CookDetail> cookDetail;

        public Item() {

        }
        public Item(String name, String weight, List<CookDetail> cookDetails) {
            this.name = name;
            this.weight = weight;
            this.cookDetail = cookDetails;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public List<CookDetail> getCookDetail() {
            return cookDetail;
        }

        public void setCookDetail(List<CookDetail> cookDetail) {
            this.cookDetail = cookDetail;
        }

        public class CookDetail {
            @Expose
            private String cookTemp;
            @Expose
            private String cookTime;

            public CookDetail() {

            }
            public CookDetail(String cookTemp, String cookTime) {
                this.cookTemp = cookTemp;
                this.cookTime = cookTime;
            }

            public String getCookTemp() {
                return cookTemp;
            }

            public void setCookTemp(String cookTemp) {
                this.cookTemp = cookTemp;
            }

            public String getCookTime() {
                return cookTime;
            }

            public void setCookTime(String cookTime) {
                this.cookTime = cookTime;
            }
        }
    }
}
