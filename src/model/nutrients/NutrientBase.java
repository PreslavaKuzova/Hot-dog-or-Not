package model.nutrients;

public abstract class NutrientBase {
    protected double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
