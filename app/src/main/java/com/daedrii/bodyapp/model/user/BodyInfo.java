package com.daedrii.bodyapp.model.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BodyInfo {

    private Integer age;

    private Integer weight;

    private Integer height;

    private Sex gender;

    private Boolean transgender;

    private DietGoal goal;

    private ActLevel actLevel;

    private DietType diet;

    private Double IMC;

    private Double IDR;

    public void setGender(Sex gender) {
        this.gender = gender;
    }

    public Double getIMC() {
        return IMC;
    }

    public void setIMC(Double IMC) {
        this.IMC = IMC;
    }

    public Double getIDR() {
        return IDR;
    }

    public void setIDR(Double IDR) {
        this.IDR = IDR;
    }

    public BodyInfo() {
        this.diet = null;
        this.gender = null;
        this.actLevel = null;
        this.age = 0;
    }

    public enum DietType{
        LowCarb,
        MidCarb,
        HighCarb
    }

    public enum ActLevel{

        SEDENTARY(1.2),
        LOW_ACTIVE(1.375),
        ACTIVE(1.55),
        HIGH_ACTIVE(1.725),
        EXTREME_ACTIVE(1.9);

        public Double getMetBasal() {
            return metBasal;
        }


        private Double metBasal;

        ActLevel(Double metBasal){
            this.metBasal = metBasal;
        }
    }

    public enum DietGoal{
        LOSS("Perder Peso"),
        KEEP("Manter Peso"),
        GAIN("Ganhar Peso");

        private String stringGoal;
        public String getStringGoal() {
            return stringGoal;
        }

        DietGoal(String stringGoal) {this.stringGoal = stringGoal;}


    }

    public enum Sex{
        MASCULINO, FEMININO, NULO;
    }

    @Override
    public String toString() {
        return "BodyInfo{" +
                "age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                ", gender=" + gender +
                ", goal=" + goal +
                ", actLevel=" + actLevel +
                ", IMC=" + IMC +
                ", IDR=" + IDR +
                '}';
    }

    public ActLevel getActLevel() {
        return actLevel;
    }

    public DietGoal getGoal() {
        return goal;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWeight() {
        return weight;
    }

    public Sex getGender() {
        return gender;
    }

    public void setActLevel(ActLevel actLevel) {
        this.actLevel = actLevel;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGoal(DietGoal goal) {
        this.goal = goal;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSex(Sex gender) {
        this.gender = gender;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public DietType getDiet() {
        return diet;
    }

    public void setDiet(DietType diet) {
        this.diet = diet;
    }

    public Boolean getTransgender() {
        return transgender;
    }

    public void setTransgender(Boolean transgender) {
        this.transgender = transgender;
    }

    public static List<DietGoal> getDietGoalValues() {
        return Arrays.asList(DietGoal.values());
    }
}
