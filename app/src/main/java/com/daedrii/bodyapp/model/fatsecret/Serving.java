package com.daedrii.bodyapp.model.fatsecret;

public class Serving {
    private String servingId;
    private String servingDescription;
    private String servingUrl;
    private double metricServingAmount;
    private String metricServingUnit;
    private double numberOfUnits;
    private String measurementDescription;
    private int calories;
    private double carbohydrate;
    private double protein;
    private double fat;
    private double saturatedFat;
    private double monounsaturatedFat;
    private double transFat;
    private int cholesterol;
    private int sodium;
    private int potassium;
    private double fiber;
    private double sugar;
    private int calcium;
    private int iron;

    public Serving(String servingId, String servingDescription, String servingUrl, double metricServingAmount, String metricServingUnit, double numberOfUnits, String measurementDescription, int calories, double carbohydrate, double protein, double fat, double saturatedFat, double monounsaturatedFat, double transFat, int cholesterol, int sodium, int potassium, double fiber, double sugar, int calcium, int iron) {
        this.servingId = servingId;
        this.servingDescription = servingDescription;
        this.servingUrl = servingUrl;
        this.metricServingAmount = metricServingAmount;
        this.metricServingUnit = metricServingUnit;
        this.numberOfUnits = numberOfUnits;
        this.measurementDescription = measurementDescription;
        this.calories = calories;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
        this.saturatedFat = saturatedFat;
        this.monounsaturatedFat = monounsaturatedFat;
        this.transFat = transFat;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.potassium = potassium;
        this.fiber = fiber;
        this.sugar = sugar;
        this.calcium = calcium;
        this.iron = iron;
    }

    public String getServingId() {
        return servingId;
    }

    public String getServingDescription() {
        return servingDescription;
    }

    public String getServingUrl() {
        return servingUrl;
    }

    public double getMetricServingAmount() {
        return metricServingAmount;
    }

    public String getMetricServingUnit() {
        return metricServingUnit;
    }

    public double getNumberOfUnits() {
        return numberOfUnits;
    }

    public String getMeasurementDescription() {
        return measurementDescription;
    }

    public int getCalories() {
        return calories;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getSaturatedFat() {
        return saturatedFat;
    }

    public double getMonounsaturatedFat() {
        return monounsaturatedFat;
    }

    public double getTransFat() {
        return transFat;
    }

    public int getCholesterol() {
        return cholesterol;
    }

    public int getSodium() {
        return sodium;
    }

    public int getPotassium() {
        return potassium;
    }

    public double getFiber() {
        return fiber;
    }

    public double getSugar() {
        return sugar;
    }

    public int getCalcium() {
        return calcium;
    }

    public int getIron() {
        return iron;
    }
}