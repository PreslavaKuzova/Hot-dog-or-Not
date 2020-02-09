package presenters;

import controllers.IOController;
import utils.constants.FoodAnalyzerConstants;
import utils.io.devices.IODevice;

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

    public <K> void showData(List<K> data) {
        for (K current : data) {
            device.write(current.toString());
        }
    }

    public <K> void showData(K data) {
        device.write(data.toString());
    }
}
