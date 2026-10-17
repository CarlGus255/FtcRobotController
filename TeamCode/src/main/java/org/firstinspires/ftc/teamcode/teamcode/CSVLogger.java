package org.firstinspires.ftc.teamcode.teamcode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVLogger {

    private PrintWriter writer;
    private long startTime;

    public CSVLogger(String filename, String... headers) throws IOException {

        // Folder on the Control Hub
        File directory = new File("/sdcard/FIRST/java/src/Datalogs");

        // Create folder if it doesn't exist
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create the file
        File file = new File(directory, filename + ".csv");

        writer = new PrintWriter(new FileWriter(file));

        startTime = System.currentTimeMillis();

        // Write headers
        for (int i = 0; i < headers.length; i++) {
            if (i > 0) {
                writer.print(",");
            }

            writer.print(headers[i]);
        }

        writer.println();
        writer.flush();
    }

    public void log(Object... values) {

        double time = (System.currentTimeMillis() - startTime) / 1000.0;

        writer.printf("%.3f", time);

        for (Object value : values) {
            writer.print(",");
            writer.print(value);
        }

        writer.println();

        // Make sure the data actually gets written
        writer.flush();
    }

    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
}