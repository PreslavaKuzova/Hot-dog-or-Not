package model;

import model.nutrients.Calories;
import model.nutrients.Carbohydrates;
import model.nutrients.Fat;
import model.nutrients.Protein;

public class NutritionalValues {
    private Fat fat;
    private Carbohydrates carbohydrates;
    private Protein protein;
    private Calories calories;

    public double getFat() {
        return this.fat.getValue();
    }

    public void setFat(double fat) {
        this.fat.setValue(fat);
    }

    public double getCarbohydrates() {
        return this.carbohydrates.getValue();
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates.setValue(carbohydrates);
    }

    public double getProtein() {
        return this.protein.getValue();
    }

    public void setProtein(double protein) {
        this.protein.setValue(protein);
    }

    public double getCalories() {
        return this.calories.getValue();
    }

    public void setCalories(double calories) {
        this.calories.setValue(calories);
    }

    @Override
    public String toString() {
        return "Calories: " + calories + System.lineSeparator() + System.lineSeparator()
                + "Fat: " + fat + System.lineSeparator()
                + "Carbohydrates: " + carbohydrates + System.lineSeparator()
                + "Protein: " + protein + System.lineSeparator();
    }
}
