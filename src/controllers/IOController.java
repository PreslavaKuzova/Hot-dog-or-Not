package controllers;

import data.client.Client;
import utils.io.devices.IODevice;
import presenters.IOPresenter;
import data.model.Food;
import data.model.NutritionalValues;

import java.util.List;

public class IOController {

    private final Client client;
    private final IOPresenter presenter;

    public IOController(IODevice device) {
        client = new Client();
        presenter = new IOPresenter(device);
        presenter.setController(this);
        presenter.startReadingDataFlow();
    }

    public void onFoodRequestMade(String food) {
        List<Food> foodDetails = client.getFoodDetails(food);
        presenter.showData(foodDetails);
    }

    public void onNutrientsRequestMade(String fdcId) {
        NutritionalValues nutrients = client.getNutrients(fdcId);
        if (nutrients != null) {
            presenter.showData(nutrients);
        }
    }

    public void onBarcodeRequestMade(String barcode) {
        Food food = client.getFoodFromBarcode(barcode);
        if (food != null) {
            presenter.showData(food);
        }
    }

    public void onPhotoRequestMade(String directory) {
        List<Food> foodList = client.recognizeFood(directory);
        presenter.showData(foodList);
    }
}
