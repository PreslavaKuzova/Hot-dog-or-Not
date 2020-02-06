import client.Client;
import model.Food;
import model.NutritionalValues;

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
            printMenu();
            int userOption = scanner.nextInt();
            scanner.nextLine();

            switch (userOption) {
                case 1:
                    System.out.print("Enter the desired food: ");
                    String foodName = scanner.nextLine();
                    List<Food> result = client.getFoodDetails(foodName);
                    for (Food f :result) {
                        System.out.println(f);
                    }
                    break;
                case 2:
                    System.out.print("Enter the fdcId: ");
                    String fdcId = scanner.nextLine();
                    NutritionalValues nutrients = client.getNutrients(fdcId);
                    if (nutrients != null) {
                        System.out.println(nutrients);
                    }
                    break;
                case 3:
                    System.out.print("Enter the gtinUpc: ");
                    String barcode = scanner.nextLine();
                    Food food = client.getFoodFromBarcode(barcode);
                    if(food != null) {
                        System.out.println(food);
                    }
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Wrong command! Try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("Main menu");
        System.out.println("1. Get food");
        System.out.println("2. Get food report");
        System.out.println("3. Get food by barcode");
        System.out.println("4. Get food by photo");
        System.out.println("5. Exit");
        System.out.print("Enter choice: ");
    }

}
