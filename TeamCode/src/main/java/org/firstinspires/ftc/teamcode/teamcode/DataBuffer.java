package org.firstinspires.ftc.teamcode.teamcode;

public class DataBuffer {
    private final double[] data;
    private final double[] sorted;

    public DataBuffer(int size) {
        data = new double[size];
        sorted = new double[size];
    }
    public void add(double value) {
        // Shift everything toward the end
        for (int i = data.length - 1; i > 0; i--) {
            data[i] = data[i - 1];
        }

        // Put newest value at index 0
        data[0] = value;
    }
    public double[] getData() {
        return data.clone();
    }
    double mean;
    public void calculateMean() {
        double sum = 0;

        for (int i = 0; i < data.length - 1; i++) {
            sum += data[i];
        }
        mean = sum/data.length;
    }
    public double getMean() {
        calculateMean();
        return mean;
    }
    double median;
    double middle;
    int middle2;
    boolean isSorted = false;
    boolean interruption = false;
    double larger;
    public void sort() {
        isSorted = false;
        //Copy from data into sorted
        for(int i = 0; i < data.length; i++) {
            sorted[i] = data[i];
        }
        while (!isSorted) {
            for (int i = 0; i < data.length - 1; i++) {
                if (sorted[i] > sorted[i+1]) {
                    larger = sorted[i];
                    sorted[i] = sorted[i+1];
                    sorted[i+1] = larger;
                    interruption = true;
                }
            }
            if (!interruption && !isSorted) {
                isSorted = true;
            }
            if (interruption) {
                interruption = false;
            }

        }
    }
    public void calculateMedian() {
        sort();
        middle = data.length/2;
        middle2 = data.length/2; //See if it gets truncated (odd number case)
        if (middle == middle2) {
            median = sorted[middle2];
        } else {
            median = (sorted[middle2] - sorted[middle2+1])/2;
        }
    }
    public double getMedian() {
        calculateMedian();
        return median;
    }
    double mode;
    double range;
    double highest;
    double lowest;
    public void calculateRange() {
        highest = data[0];
        lowest = data[0];
        for (int i = 0; i < data.length - 1; i ++) {
            if (data[i] > highest) {
                highest = data[i];
            }
            if (data[i] < lowest) {
                lowest = data[i];
            }
        }
        range = highest - lowest;
    }
    public double getRange() {
        calculateRange();
        return range;
    }
    double deviation;
    double variance;
    public void calculateDeviation() {
        calculateMean();
        for (int i = 0; i < data.length - 1; i++) {
            variance += Math.pow(data[i] - mean, 2);
        }
        variance /= data.length;
        deviation = Math.sqrt(variance);
    }
    public double getDeviation() {
        calculateDeviation();
        return deviation;
    }
}
