package cache;

import model.Food;

import java.util.List;

public class BarcodeCache extends LFUCache<Food> {
    private static volatile BarcodeCache instance;

    private BarcodeCache() {
        super();
    }

    public static BarcodeCache getInstance() {
        //double checked locking principle is used to avoid overhead
        if (instance == null) {
            synchronized (FoodCache.class) {
                if (instance == null) {
                    instance = new BarcodeCache();
                }
            }
        }
        return instance;
    }

    public Food getFoodByBarcode(String gtinUpc) {
        List<Food> result = super.retrieveInformation(gtinUpc);
        if(result != null) {
            return result.iterator().next();
        }
        return null;
    }
}
