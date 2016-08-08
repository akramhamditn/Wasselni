package com.esprit.wasselni.Entities;

/**
 * Created by hamdi on 30/11/2015.
 */
public class User {
    public String id;
    public String nom;
    public String prenom;
    public String adresse;
    public String telephone;
    public String email;
    public String password;
    public String type;
    public double latitude;
    public double longitude;
    public String etat;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String id, String nom, String prenom, String adresse, String telephone, String email, String password, String type) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public String getType() {
        return type;

    }

    public void setType(String type) {
        this.type = type;
    }
}
