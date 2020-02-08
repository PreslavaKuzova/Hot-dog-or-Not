package data.client;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import data.cache.BarcodeCache;
import data.model.Food;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class BarcodeClient {
    private final BarcodeCache barcodeCache;

    public BarcodeClient() {
        this.barcodeCache = BarcodeCache.getInstance();
    }

    public Food getFoodFromBarcode(String parameter) {
        if(Files.exists(new File(parameter).toPath())) {
            String barcode = getBarcodeFromFile(parameter);
            return barcodeCache.getFoodByBarcode(barcode);
        }

        return barcodeCache.getFoodByBarcode(parameter);
    }

    private String getBarcodeFromFile(String directory) {
        try (InputStream file = new FileInputStream(directory)) {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(
                            ImageIO.read(file))));

            Result barcodeResult = new MultiFormatReader().decode(binaryBitmap);

            return barcodeResult.getText();
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
