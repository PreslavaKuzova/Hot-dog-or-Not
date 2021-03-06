package data.model.nutrients;

public abstract class NutrientBase {
    protected double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Double.toString(value) + System.lineSeparator();
    }
}
