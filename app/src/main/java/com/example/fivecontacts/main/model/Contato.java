package com.example.fivecontacts.main.model;

import java.io.Serializable;

public class Contato implements Serializable {
    String name;
    String phoneNumber;

    public Contato() {
        
    }

    public Contato(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
