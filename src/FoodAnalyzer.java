import client.Client;
import model.Food;
import model.NutritionalValues;
import utils.constants.FoodAnalyzerConstants;

import java.util.List;
import java.util.Scanner;

public class FoodAnalyzer {

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        final Scanner scanner = new Scanner(System.in);
        final Client client = new Client();

        while (true) {
            String[] userOption = scanner.nextLine().split(" ", FoodAnalyzerConstants.SPLIT_LIMIT);
            String parameter = userOption[FoodAnalyzerConstants.PARAMETER];

            switch (userOption[FoodAnalyzerConstants.OPTION]) {
                case FoodAnalyzerConstants.GET_FOOD_BY_NAME:
                    List<Food> result = client.getFoodDetails(parameter);
                    for (Food f : result) {
                        System.out.println(f);
                    }
                    break;
                case FoodAnalyzerConstants.GET_FOOD_REPORT:
                    NutritionalValues nutrients = client.getNutrients(parameter);
                    if (nutrients != null) {
                        System.out.println(nutrients);
                    }
                    break;
                case FoodAnalyzerConstants.GET_FOOD_BY_BARCODE:
                    Food food = client.getFoodFromBarcode(parameter);
                    if (food != null) {
                        System.out.println(food);
                    }
                    break;
                case FoodAnalyzerConstants.GET_FOOD_BY_PHOTO:
                    break;
                case FoodAnalyzerConstants.EXIT:
                    System.exit(0);
                    break;
            }
        }
    }
    

}
