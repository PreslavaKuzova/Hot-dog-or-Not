package utils.io.devices;

import java.util.Scanner;

public class ConsoleDevice implements IODevice {
    @Override
    public String read() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @Override
    public void write(String data) {
        System.out.println(data);
    }
}
