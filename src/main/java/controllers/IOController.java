package controllers;

import data.client.BarcodeClient;
import data.client.CachedNetworkClient;
import data.client.ImageRecognitionClient;
import data.client.NetworkClient;
import data.model.Food;
import data.model.NutritionalValues;
import presenters.IOPresenter;
import utils.io.devices.IODevice;

import java.net.http.HttpClient;
import java.util.List;

public class IOController {

    private final IOPresenter presenter;
    private final NetworkClient networkClient;
    private final BarcodeClient barcodeClient;
    private final ImageRecognitionClient imageRecognitionClient;

    public IOController(IODevice device) {
        networkClient = new CachedNetworkClient(HttpClient.newHttpClient());
        barcodeClient = new BarcodeClient();
        imageRecognitionClient = new ImageRecognitionClient();
        presenter = new IOPresenter(device);
        presenter.setController(this);
        presenter.startReadingDataFlow();
    }

    public void onFoodRequestMade(String food) {
        new Thread(() -> {
            List<Food> foodDetails = networkClient.getFoodDetails(food);
            presenter.showData(foodDetails);
        }).start();
    }

    public void onNutrientsRequestMade(String fdcId) {
        new Thread(() -> {
            NutritionalValues nutrients = networkClient.getNutrients(fdcId);
            if (nutrients != null) {
                presenter.showData(nutrients);
            }
        }).start();
    }

    public void onBarcodeRequestMade(String barcode) {
        new Thread(() -> {
            Food food = barcodeClient.getFoodFromBarcode(barcode);
            if (food != null) {
                presenter.showData(food);
            }
        }).start();
    }

    public void onPhotoRequestMade(String directory) {
        new Thread(() -> {
            try {
                List<String> labels = imageRecognitionClient.recognizeImage(directory);
                presenter.showData(labels);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


}
