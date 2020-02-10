import utils.io.devices.ConsoleDevice;
import controllers.IOController;
import utils.io.devices.FileDevice;

import java.io.IOException;

public class FoodAnalyzer {

    public static void main(String[] args) throws IOException {
        new IOController(new ConsoleDevice());
    }

}
