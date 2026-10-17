package org.firstinspires.ftc.teamcode.teamcode;

public class Data {
    int id;
    double data;
    public void createData (int Id, double doubleData) {
        id = Id;
        data = doubleData;
    }
    public int getId () {
        return id;
    }
    public double getDoubleData () {
        return data;
    }
}
