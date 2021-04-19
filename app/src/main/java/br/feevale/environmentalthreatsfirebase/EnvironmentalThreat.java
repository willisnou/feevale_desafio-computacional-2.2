package br.feevale.environmentalthreatsfirebase;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class EnvironmentalThreat implements Serializable {
    private String date;
    private String description;
    private String address;
    private String image;

    public EnvironmentalThreat() {
    }

    public EnvironmentalThreat(String date, String description, String address) {
        this.date = date;
        this.description = description;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @NonNull
    @Override
    public String toString(){
        return /*this.id + "," +*/ this.address + "," + this.date + "," + this.description;
    }
}
