package data.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import data.model.Food;
import data.model.NutritionalValues;
import utils.constants.ClientConstants;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class NetworkClient {

    private final HttpClient client;
    private final Gson gson;

    public NetworkClient(HttpClient client) {
        this.client = client;
        gson = new Gson();
    }

    public NutritionalValues getNutrients(String fdcId) {
        NutritionalValues nutrients = null;

        try {
            HttpRequest nutrientsRequest = createNutrientRequest(fdcId);

            CompletableFuture<String> nutrientsResponse =
                    client.sendAsync(nutrientsRequest, HttpResponse.BodyHandlers.ofString())
                            .thenApply(HttpResponse::body);

            JsonObject nutrientsJson = gson.fromJson(nutrientsResponse.get(), JsonObject.class)
                    .getAsJsonObject(ClientConstants.PARSE_NUTRIENTS);

            nutrients = gson.fromJson(nutrientsJson, NutritionalValues.class);

            JsonObject json =  gson.fromJson(nutrientsResponse.get(), JsonObject.class);
            if(json.has(ClientConstants.PARSE_INGREDIENTS)) {
                nutrients.setIngredients(json.get(ClientConstants.PARSE_INGREDIENTS).toString());
            }

        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Something went wrong while trying to send the API request");
            e.printStackTrace();
        }

        return nutrients;
    }

    public List<Food> getFoodDetails(String food) {
        List<Food> foodList = new ArrayList<>();

        try {
            HttpRequest foodRequest = createFoodSearchRequest(food);
            CompletableFuture<String> foodResponse = client
                    .sendAsync(foodRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body);

            JsonArray result = gson.fromJson(foodResponse.get(), JsonObject.class)
                    .getAsJsonArray(ClientConstants.PARSE_FOOD);

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

}
