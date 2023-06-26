package com.daedrii.bodyapp.model.fatsecret;

import java.util.List;

public class FoodDetails {
    private String foodId;
    private String foodName;
    private String brandName;
    private String foodType;
    private String foodUrl;
    private List<Serving> servings;

    @Override
    public String toString() {
        return "FoodDetails{" +
                "foodId='" + foodId + '\'' +
                ", foodName='" + foodName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", foodType='" + foodType + '\'' +
                ", foodUrl='" + foodUrl + '\'' +
                ", servings=" + servings.toString()+
                '}';
    }

    public FoodDetails(String foodId, String foodName, String brandName, String foodType, String foodUrl, List<Serving> servings) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.brandName = brandName;
        this.foodType = foodType;
        this.foodUrl = foodUrl;
        this.servings = servings;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getFoodType() {
        return foodType;
    }

    public String getFoodUrl() {
        return foodUrl;
    }

    public List<Serving> getServings() {
        return servings;
    }
}
