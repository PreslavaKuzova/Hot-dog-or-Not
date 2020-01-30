package model;

public class Food {
    private String fdcId;
    private String description;
    private String gtinUpc;
    private String ingredients;
    private NutritionalValues nutrients;

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

    public NutritionalValues getNutritionalValues() {
        return nutrients;
    }

    public void setNutritionalValues(NutritionalValues nutrients) {
        this.nutrients = nutrients;
    }
}
