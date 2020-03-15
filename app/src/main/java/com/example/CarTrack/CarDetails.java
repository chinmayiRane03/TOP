package com.example.CarTrack;

import android.net.Uri;

public class CarDetails {

    private String Uploader;
    private String Addresss;
    private Long accuracy;
    private Long TimeStamp;
private double Latitude;
    private double Longitude;
    private String Image_Url;

    public String getImage_Url() {
        return Image_Url;
    }

    public void setImage_Url(String image_Url) {
        Image_Url = image_Url;
    }


    public String getUploader() {
        return Uploader;
    }

    public void setUploader(String uploader) {
        Uploader = uploader;
    }

    public String getAddresss() {
        return Addresss;
    }

    public void setAddresss(String addresss) {
        Addresss = addresss;
    }

    public Long getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Long accuracy) {
        this.accuracy = accuracy;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }

    public CarDetails(String uploader, String address, Long accuracy, Long timestamp,String image_path_storage) {
        this.Uploader = uploader;
        this.Addresss = address;
        this.accuracy = accuracy;
        this.TimeStamp = timestamp;
        this.Image_Url=image_path_storage;

    }

    public CarDetails() {
          }


    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}