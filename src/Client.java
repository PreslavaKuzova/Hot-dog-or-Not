import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Food;
import model.NutritionalValues;
import utils.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Client {
    private static HttpClient client;
    private static Gson gson;

    public static void main(String[] args) {
        client = HttpClient.newHttpClient();
        gson = new Gson();
        List<Food> all = getFoodDetails("Cheddar cheese");

        Food food = all.iterator().next();
        NutritionalValues values = getNutrients(food.getFdcId());
        System.out.println(values.getFat());

        for (Food f : all) {
            System.out.println(f.getFdcId());
        }
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

        try {
            HttpRequest foodRequest = createFoodSearchRequest(food);
            CompletableFuture<String> foodResponse = client
                    .sendAsync(foodRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body);;

            JsonArray result = gson.fromJson(foodResponse.get(), JsonObject.class)
                    .getAsJsonArray(Constants.PARSE_FOOD);

            for (JsonElement element : result) {
                Food currentFood = gson.fromJson(element.getAsJsonObject(), Food.class);
                foodList.add(currentFood);
            }

        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Something went wrong while trying to send the API request");
            e.printStackTrace();
        }

        return foodList;
    }

    private static Food getFoodByBarcode() {

        return null;
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
}
