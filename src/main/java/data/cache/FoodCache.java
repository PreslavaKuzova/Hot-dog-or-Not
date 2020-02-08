package data.cache;

import data.model.Food;

import java.util.List;

public class FoodCache extends LFUCache<Food> {
    private static volatile FoodCache instance;

    private FoodCache() {
        super();
    }

    public static FoodCache getInstance() {
        //double checked locking principle is used to avoid overhead
        if (instance == null) {
            synchronized (FoodCache.class) {
                if (instance == null) {
                    instance = new FoodCache();
                }
            }
        }
        return instance;
    }

    public void addFoods(String name, List<Food> foods) {
        super.add(name, foods);
    }

    public List<Food> getFoodListByName(String name) {
        return super.retrieveInformation(name);
    }
}
