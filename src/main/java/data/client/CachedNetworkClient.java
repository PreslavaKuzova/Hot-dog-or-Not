package data.client;

import data.cache.BarcodeCache;
import data.cache.FoodCache;
import data.cache.NutrientsCache;
import data.model.Food;
import data.model.NutritionalValues;

import java.net.http.HttpClient;
import java.util.List;

public class CachedNetworkClient extends NetworkClient {

    private final FoodCache foodCache;
    private final NutrientsCache nutrientsCache;
    private final BarcodeCache barcodeCache;

    public CachedNetworkClient(HttpClient httpClient) {
        super(httpClient);
        foodCache = FoodCache.getInstance();
        nutrientsCache = NutrientsCache.getInstance();
        barcodeCache = BarcodeCache.getInstance();
    }

    @Override
    public NutritionalValues getNutrients(String fdcId) {
        NutritionalValues cacheResult = nutrientsCache.getNutrientsByIdentifier(fdcId);
        if(cacheResult != null) {
            return cacheResult;
        }

        NutritionalValues nutrients = super.getNutrients(fdcId);
        nutrientsCache.addNutrient(fdcId, nutrients);

        return  nutrients;
    }

    @Override
    public List<Food> getFoodDetails(String foodName) {
        List<Food> cacheResult = foodCache.getFoodListByName(foodName);
        if (cacheResult != null) {
            return cacheResult;
        }

        List<Food> foodList = super.getFoodDetails(foodName);
        foodCache.addFoods(foodName, foodList);

        foodList.forEach(this::addToBarcodeCache);

        return foodList;
    }

    private void addToBarcodeCache(Food food) {
        if (food.getGtinUpc() != null) {
            barcodeCache.addBarcode(food.getGtinUpc(), food);
        }
    }

}
