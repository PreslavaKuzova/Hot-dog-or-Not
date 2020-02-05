package cache;

import model.NutritionalValues;

public class NutrientsCache extends LFUCache<NutritionalValues> {

    private static volatile NutrientsCache instance;

    private NutrientsCache() {
        super();
    }

    public static NutrientsCache getInstance() {
        //double checked locking principle is used to avoid overhead
        if (instance == null) {
            synchronized (NutrientsCache.class) {
                if (instance == null) {
                    instance = new NutrientsCache();
                }
            }
        }
        return instance;
    }

}
