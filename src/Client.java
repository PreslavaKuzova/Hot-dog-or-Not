import cache.BarcodeCache;
import cache.FoodCache;
import cache.NutrientsCache;
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
import model.Food;
import model.NutritionalValues;
import utils.Constants;

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

    public static void main(String[] args) throws IOException, NotFoundException {
        client = HttpClient.newHttpClient();
        gson = new Gson();

        foodCache = FoodCache.getInstance();
        nutrientsCache = NutrientsCache.getInstance();
        barcodeCache = BarcodeCache.getInstance();

        System.out.println(getBarcodeFromFile("C:\\Users\\presl\\Documents\\Preslava\\Java\\HotdogOrNot\\resources\\upc-barcode.gif"));

        List<Food> all = getFoodDetails("Cheddar cheese");
        foodCache.addFoods("Cheddar cheese", all);

        System.out.println(getFoodFromBarcode("04131858084").getFdcId());
    }

    private static NutritionalValues getNutrients(String fdcId) {
        NutritionalValues nutrients = null;

        try {
            HttpRequest nutrientsRequest = createNutrientRequest(fdcId);

            CompletableFuture<String> nutrientsResponse =
                    client
                            .sendAsync(nutrientsRequest, HttpResponse.BodyHandlers.ofString())
                            .thenApply(HttpResponse::body);

            JsonObject nutrientsJson = gson.fromJson(nutrientsResponse.get(), JsonObject.class)
                    .getAsJsonObject(Constants.PARSE_NUTRIENTS);

            nutrients = gson.fromJson(nutrientsJson, NutritionalValues.class);

        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Something went wrong while trying to send the API request");
            e.printStackTrace();
        }

        return nutrients;
    }

    private static List<Food> getFoodDetails(String food) {
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
                    .getAsJsonArray(Constants.PARSE_FOOD);

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

        return foodList;
    }

    private static Food getFoodFromBarcode(String parameter) {
        if(Files.exists(new File(parameter).toPath())) {
            String barcode = getBarcodeFromFile(parameter);
            return barcodeCache.getFoodByBarcode(barcode);
        }

        return barcodeCache.getFoodByBarcode(parameter);
    }

    private static Food getFoodFromBarcode(String gtinUpc, String directory) {
        return barcodeCache.getFoodByBarcode(gtinUpc);
    }

    //TODO: make it synchronized
    private static HttpRequest createFoodSearchRequest(String food) {
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.API_URL)
                .append(Constants.SEARCH)
                .append(Constants.KEY)
                .append(Constants.GENERAL_SEARCH_INPUT)
                .append(formatRequestData(food));

        return HttpRequest.newBuilder(URI.create(sb.toString())).build();
    }

    private static HttpRequest createNutrientRequest(String fdcId) {
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.API_URL)
                .append(fdcId)
                .append(Constants.KEY);

        return HttpRequest.newBuilder(URI.create(sb.toString())).build();
    }

    private static String formatRequestData(String food) {
        return food.replaceAll(" ", Constants.ENCODING_SYMBOL);
    }

    private static String getBarcodeFromFile(String directory) {
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
