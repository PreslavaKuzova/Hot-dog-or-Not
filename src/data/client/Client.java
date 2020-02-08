package data.client;

import data.cache.BarcodeCache;
import data.cache.FoodCache;
import data.cache.NutrientsCache;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import data.model.Food;
import data.model.NutritionalValues;
import utils.constants.ClientConstants;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Client {
    private static HttpClient client;
    private static Gson gson;
    private static FoodCache foodCache;
    private static NutrientsCache nutrientsCache;
    private static BarcodeCache barcodeCache;

    public Client() {
        client = HttpClient.newHttpClient();
        gson = new Gson();

        foodCache = FoodCache.getInstance();
        nutrientsCache = NutrientsCache.getInstance();
        barcodeCache = BarcodeCache.getInstance();
    }

    //TODO: how to make it multi-thread
    public NutritionalValues getNutrients(String fdcId) {
        NutritionalValues nutrients = null;

        NutritionalValues cacheResult = nutrientsCache.getNutrientsByIdentifier(fdcId);
        if(cacheResult != null) {
            return cacheResult;
        }

        try {
            HttpRequest nutrientsRequest = createNutrientRequest(fdcId);

            CompletableFuture<String> nutrientsResponse =
                    client.sendAsync(nutrientsRequest, HttpResponse.BodyHandlers.ofString())
                            .thenApply(HttpResponse::body);

            JsonObject nutrientsJson = gson.fromJson(nutrientsResponse.get(), JsonObject.class)
                    .getAsJsonObject(ClientConstants.PARSE_NUTRIENTS);

            nutrients = gson.fromJson(nutrientsJson, NutritionalValues.class);

            String ingredients = gson.fromJson(nutrientsResponse.get(), JsonObject.class)
                    .get("ingredients").toString();
            nutrients.setIngredients(ingredients);

            nutrientsCache.addNutrient(fdcId, nutrients);

        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Something went wrong while trying to send the API request");
            e.printStackTrace();
        }

        return nutrients;
    }

    public List<Food> getFoodDetails(String food) {
        List<Food> foodList = new ArrayList<>();

        List<Food> cacheResult = foodCache.getFoodListByName(food);
        if (cacheResult != null) {
            return cacheResult;
        }

        try {
            HttpRequest foodRequest = createFoodSearchRequest(food);
            CompletableFuture<String> foodResponse = client
                    .sendAsync(foodRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body);

            JsonArray result = gson.fromJson(foodResponse.get(), JsonObject.class)
                    .getAsJsonArray(ClientConstants.PARSE_FOOD);

            for (JsonElement element : result) {
                Food currentFood = gson.fromJson(element.getAsJsonObject(), Food.class);

                if (currentFood.getGtinUpc() != null) {
                    barcodeCache.addBarcode(currentFood.getGtinUpc(), currentFood);
                }

                foodList.add(currentFood);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Something went wrong while trying to send the API request");
            e.printStackTrace();
        }

        foodCache.addFoods(food, foodList);
        return foodList;
    }

    public Food getFoodFromBarcode(String parameter) {
        if(Files.exists(new File(parameter).toPath())) {
            String barcode = getBarcodeFromFile(parameter);
            return barcodeCache.getFoodByBarcode(barcode);
        }

        return barcodeCache.getFoodByBarcode(parameter);
    }

    public Food getFoodFromBarcode(String gtinUpc, String directory) {
        return barcodeCache.getFoodByBarcode(gtinUpc);
    }

    public List<Food> recognizeFood(String directory) {
        return null;
    }

    private HttpRequest createFoodSearchRequest(String food) {
        StringBuffer sb = new StringBuffer();
        sb.append(ClientConstants.API_URL)
                .append(ClientConstants.SEARCH)
                .append(ClientConstants.KEY)
                .append(ClientConstants.GENERAL_SEARCH_INPUT)
                .append(formatRequestData(food));

        return HttpRequest.newBuilder(URI.create(sb.toString())).build();
    }

    private HttpRequest createNutrientRequest(String fdcId) {
        StringBuffer sb = new StringBuffer();
        sb.append(ClientConstants.API_URL)
                .append(fdcId)
                .append(ClientConstants.KEY);

        return HttpRequest.newBuilder(URI.create(sb.toString())).build();
    }

    private String formatRequestData(String food) {
        return food.replaceAll(" ", ClientConstants.ENCODING_SYMBOL);
    }

    private String getBarcodeFromFile(String directory) {
        try (InputStream file = new FileInputStream(directory)) {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(
                            ImageIO.read(file))));

            Result barcodeResult = new MultiFormatReader().decode(binaryBitmap);

            return barcodeResult.getText();
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
