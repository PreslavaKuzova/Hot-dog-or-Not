package presenters;

import controllers.IOController;
import utils.io.devices.IODevice;
import data.model.Food;
import utils.constants.FoodAnalyzerConstants;

import java.util.List;

public class IOPresenter {
    private IODevice device;
    private IOController controller;

    public IOPresenter(IODevice device) {
        this.device = device;
    }

    public void startReadingDataFlow() {
        while (true) {
            String[] userOption = device.read().split(" ", FoodAnalyzerConstants.SPLIT_LIMIT);
            String parameter = userOption[FoodAnalyzerConstants.PARAMETER];

            switch (userOption[FoodAnalyzerConstants.OPTION]) {
                case FoodAnalyzerConstants.GET_FOOD_BY_NAME:
                    controller.onFoodRequestMade(parameter);
                    break;
                case FoodAnalyzerConstants.GET_FOOD_REPORT:
                    controller.onNutrientsRequestMade(parameter);
                    break;
                case FoodAnalyzerConstants.GET_FOOD_BY_BARCODE:
                    controller.onBarcodeRequestMade(parameter);
                    break;
                case FoodAnalyzerConstants.GET_FOOD_BY_PHOTO:
                    controller.onPhotoRequestMade(parameter);
                    break;
                case FoodAnalyzerConstants.EXIT:
                    System.exit(0);
                    break;
            }
        }
    }

    public void setController(IOController ioController) {
        this.controller = ioController;
    }

    public void showData(List<Food> foods) {
        for (Food f : foods) {
            device.write(f.toString());
        }
    }

    public <K> void showData(K data) {
        device.write(data.toString());
    }
}
