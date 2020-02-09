import utils.io.devices.ConsoleDevice;
import controllers.IOController;
import utils.io.devices.FileDevice;

public class FoodAnalyzer {

    public static void main(String[] args) {
        new IOController(new ConsoleDevice());
    }

}
