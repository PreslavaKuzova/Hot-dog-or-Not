package model;

public class Food {
    private String fdcId;
    private String description;
    private String gtinUpc;
    private String ingredients;

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

    @Override
    public String toString() {
        return "Unique identifier: " + fdcId + System.lineSeparator()
                + "Description: " + description + System.lineSeparator()
                + "Ingredients: " + ingredients;
    }
}
