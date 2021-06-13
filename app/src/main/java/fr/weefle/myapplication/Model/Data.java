package fr.weefle.myapplication.Model;

import java.io.Serializable;

public class Data implements Serializable {

    String date;
    String time;
    double temp;
    double lat;
    double lon;

    public Data(String date, String time, double temp, double lat, double lon) {
        this.date = date;
        this.time = time;
        this.temp = temp;
        this.lat = lat;
        this.lon = lon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
