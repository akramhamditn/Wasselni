package com.esprit.wasselni.Entities;

/**
 * Created by hamdi on 07/12/2015.
 */
public class Trajets {

    String id;
    String cin;
    String numPermis;
    String destination;
    String latitude;
    String longitude;
    String latChauffeur;
    String lonChauffeur;

    public Trajets() {
    }

    public Trajets(String id, String cin, String numPermis, String destination, String latitude, String longitude, String latChauffeur, String lonChauffeur) {
        this.id = id;
        this.cin = cin;
        this.numPermis = numPermis;
        this.destination = destination;
        this.latitude = latitude;
        this.longitude = longitude;
        this.latChauffeur = latChauffeur;
        this.lonChauffeur = lonChauffeur;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getNumPermis() {
        return numPermis;
    }

    public void setNumPermis(String numPermis) {
        this.numPermis = numPermis;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatChauffeur() {
        return latChauffeur;
    }

    public void setLatChauffeur(String latChauffeur) {
        this.latChauffeur = latChauffeur;
    }

    public String getLonChauffeur() {
        return lonChauffeur;
    }

    public void setLonChauffeur(String lonChauffeur) {
        this.lonChauffeur = lonChauffeur;
    }
}
