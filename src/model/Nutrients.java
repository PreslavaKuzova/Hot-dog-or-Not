package model;

import com.google.gson.Gson;

public class Nutrients {
    private double energy;
    private int protein;
    private int fat;
    private int carbohydrate;

    public Nutrients(double energy, int protein, int fat, int carbohydrate) {
        this.energy = energy;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
    }

    public Nutrients createNutrientsFromJson(String json) {
        Nutrients nutrients = new Gson().fromJson(json, Nutrients.class);
        return nutrients;
    }

    public double getEnergy() {
        return energy;
    }

    public int getProtein() {
        return protein;
    }

    public int getFat() {
        return fat;
    }

    public int getCarbohydrate() {
        return carbohydrate;
    }
}
