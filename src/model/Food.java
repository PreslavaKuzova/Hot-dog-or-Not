package model;

import com.google.gson.Gson;

public class Food {
    private String fdcId;
    private String description;
    private String gtinUpc;
    private String ingredients;
    private Nutrients nutrients;

    public Food(String fdcId, String description, String gtinUpc, String ingredients) {
        this.fdcId = fdcId;
        this.description = description;
        this.gtinUpc = gtinUpc;
        this.ingredients = ingredients;
    }

    public Food createFoodFromJson(String json) {
        Food food = new Gson().fromJson(json, Food.class);
        return food;
    }

    public String getFdcId() {
        return fdcId;
    }

    public String getDescription() {
        return description;
    }

    public String getGtinUpc() {
        return gtinUpc;
    }

    public String getIngredients() {
        return ingredients;
    }

    public Nutrients getNutrients() {
        return nutrients;
    }

    public void setNutrients(Nutrients nutrients) {
        this.nutrients = nutrients;
    }
}
