package data.cache;

import data.model.NutritionalValues;

import java.util.Arrays;
import java.util.List;

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

    public void addNutrient(String fdcId, NutritionalValues nutrients) {
        super.add(fdcId, Arrays.asList(nutrients));
    }

    public NutritionalValues getNutrientsByIdentifier(String fdcId) {
        List<NutritionalValues> nutriets = super.retrieveInformation(fdcId);

        if (nutriets != null) {
            return nutriets.iterator().next();
        }

        return null;
    }

}
