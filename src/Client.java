import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Food;
import utils.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static HttpClient client;
    private static Gson gson;

    public static void main(String[] args) {
        client = HttpClient.newHttpClient();
        gson = new Gson();
        List<Food> all = getFoodDetails("Cheddar cheese");

        for(Food el : all) {
            System.out.println(el.getIngredients());
        }

    }

    private static List<Food> getFoodDetails(String food) {
        List<Food> allFoods = new ArrayList<>();

        try {
            HttpRequest foodRequest = createFoodSearchRequest(food);
            HttpResponse<String> response = client.send(foodRequest, HttpResponse.BodyHandlers.ofString());
            JsonArray result = new Gson().fromJson(response.body(), JsonObject.class)
                    .getAsJsonArray(Constants.parseCriteria);

            for (JsonElement element : result) {
                JsonObject jsonObject = element.getAsJsonObject();
                allFoods.add(gson.fromJson(jsonObject, Food.class));
            }

        } catch (InterruptedException | IOException e ) {
            System.out.println("Something went wrong while trying to send the API request");
            e.printStackTrace();
        }

        return allFoods;
    }

    private static HttpRequest createFoodSearchRequest(String food) {
        final String uri = Constants.api
                + Constants.key
                + Constants.generalSearchInput
                + formatRequestData(food);

        return HttpRequest.newBuilder(URI.create(uri)).build();
    }

    private static String formatRequestData(String food) {
        return food.replaceAll(" ", Constants.encodingSymbol);
    }
}
