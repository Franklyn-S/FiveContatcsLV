package com.example.fivecontacts.main.model;

import java.io.Serializable;

public class Contato implements Serializable {
    String name;
    String[] phoneNumbers;
    String selectedPhoneNumber;

    public Contato() {
        
    }

    public Contato(String name, String[] phoneNumbers, String selectedPhoneNUmber) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
        this.selectedPhoneNumber = selectedPhoneNUmber;
    }


    public Contato(String name, String[] phoneNumbers) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }

    public Contato(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumbers = new String[]{phoneNumber};
        this.selectedPhoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String[] phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getSelectedPhoneNumber() {
        return selectedPhoneNumber;
    }

    public void setSelectedPhoneNumber(String selectedPhoneNumber) {
        this.selectedPhoneNumber = selectedPhoneNumber;
    }


}
