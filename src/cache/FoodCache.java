package cache;

import model.Food;

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

}
