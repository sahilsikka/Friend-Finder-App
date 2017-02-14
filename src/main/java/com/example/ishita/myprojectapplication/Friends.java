package com.example.ishita.myprojectapplication;


/**
 * Created by rgrac on 11/17/2016.
 */
public class Friends
{
    private String username;
    private double latitude;
    private double longitude;
    public Friends(String name, double lat,double lon){
        this.username=name;
        this.latitude=lat;
        this.longitude=lon;
    }

    public String getUsername() {
        return username;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}

