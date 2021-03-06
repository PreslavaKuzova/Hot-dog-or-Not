package data.model;

import data.model.nutrients.Calories;
import data.model.nutrients.Carbohydrates;
import data.model.nutrients.Fat;
import data.model.nutrients.Protein;

public class NutritionalValues {
    private Fat fat;
    private Carbohydrates carbohydrates;
    private Protein protein;
    private Calories calories;
    private String ingredients;

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

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
        return "Ingredients: " + ingredients + System.lineSeparator()
                + "Calories: " + calories
                + "Fat: " + fat
                + "Carbohydrates: " + carbohydrates
                + "Protein: " + protein;
    }
}
