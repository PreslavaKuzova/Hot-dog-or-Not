package utils.io.devices;

import java.io.*;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class FileDevice implements IODevice {
    private BlockingQueue<String> queries;
    private String outputDirectory;

    public FileDevice(String inputDirectory, String outputDirectory) throws IOException {
        this.queries = new LinkedBlockingQueue<>();
        this.fillQueue(inputDirectory);
        this.outputDirectory = outputDirectory;
    }

    @Override
    public String read() {
        try {
            return queries.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public synchronized void write(String data) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputDirectory, true)))) {
            writer.write(data + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillQueue(String directory) throws IOException {
        InputStream stream = new FileInputStream(directory);
        try (BufferedReader mysteryTextBuffer = new BufferedReader(new InputStreamReader(stream))) {
            Collection<String> lines = mysteryTextBuffer.lines().collect(Collectors.toList());
            queries.addAll(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
