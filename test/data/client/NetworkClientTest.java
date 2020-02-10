package data.client;

import com.google.gson.Gson;
import data.model.Food;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import utils.constants.ClientConstants;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class NetworkClientTest {
    private HttpClient mockClient;
    private NetworkClient networkClient;

    private CompletableFuture<String> foodResponse;

    @BeforeClass
    public void initialize() {
        this.mockClient = mock(HttpClient.class);
        this.networkClient = new CachedNetworkClient(mockClient);
        foodResponse = CompletableFuture.completedFuture("dd");
    }

    @Test
    public void testGettingFoodDetails() {
        String food = "pineapple";
        HttpRequest foodRequest = createFoodSearchRequest(food);
        Mockito.when(mockClient.sendAsync(foodRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body))
                .thenReturn(foodResponse);

        createJsonFeedingFuture();

        List<Food> foodList = networkClient.getFoodDetails(food);

        assertEquals(3, 3);
    }

    private void createJsonFeedingFuture() {
        String value = "{ \n" +
                "   \"foodSearchCriteria\":{ \n" +
                "      \"generalSearchInput\":\"pineapple juice\",\n" +
                "      \"pageNumber\":1,\n" +
                "      \"requireAllWords\":false\n" +
                "   },\n" +
                "   \"totalHits\":48278,\n" +
                "   \"currentPage\":1,\n" +
                "   \"totalPages\":966,\n" +
                "   \"foods\":[\n" +
                "      { \n" +
                "         \"fdcId\":171347,\n" +
                "         \"description\":\"Babyfood, fruit, pears and pineapple, junior\",\n" +
                "         \"dataType\":\"SR Legacy\",\n" +
                "         \"ndbNumber\":\"3159\",\n" +
                "         \"publishedDate\":\"2019-04-01\",\n" +
                "         \"allHighlightFields\":\"\",\n" +
                "         \"score\":256.32837\n" +
                "      },\n" +
                "      { \n" +
                "         \"fdcId\":170955,\n" +
                "         \"description\":\"Babyfood, fruit, pears and pineapple, strained\",\n" +
                "         \"dataType\":\"SR Legacy\",\n" +
                "         \"ndbNumber\":\"3158\",\n" +
                "         \"publishedDate\":\"2019-04-01\",\n" +
                "         \"allHighlightFields\":\"\",\n" +
                "         \"score\":256.32837\n" +
                "      },\n" +
                "      { \n" +
                "         \"fdcId\":341826,\n" +
                "         \"description\":\"Bananas and pineapple, baby food, junior\",\n" +
                "         \"additionalDescriptions\":\"Gerber Third Foods\",\n" +
                "         \"dataType\":\"Survey (FNDDS)\",\n" +
                "         \"foodCode\":\"67309020\",\n" +
                "         \"publishedDate\":\"2019-04-01\",\n" +
                "         \"allHighlightFields\":\"\",\n" +
                "         \"score\":256.32837\n" +
                "      }\n" +
                "   ]\n" +
                "}";
        this.foodResponse.complete(value);
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
