import utils.io.devices.ConsoleDevice;
import controllers.IOController;

public class FoodAnalyzer {

    public static void main(String[] args) {
        new IOController(new ConsoleDevice());
    }

}
