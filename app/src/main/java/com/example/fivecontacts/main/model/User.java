package com.example.fivecontacts.main.model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class User implements Serializable {
    String nome;
    String login;
    String password;
    String email;
    ArrayList<Contato> contactList;

    public User(String nome, String login, String password, String email, ArrayList<Contato> contactList) {
        this.nome = nome;
        this.login = login;
        this.password = password;
        this.email = email;
        this.contactList = contactList;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Contato> getContactList() {
        return contactList;
    }

    public void setContactList(ArrayList<Contato> contactList) {
        this.contactList = contactList;
    }


}
