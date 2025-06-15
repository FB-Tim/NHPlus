package de.hitec.nhplus.model;

import javafx.beans.property.SimpleLongProperty;

public class Admin extends Person {

    private SimpleLongProperty aid;
    private final String password;

    public Admin(String firstname, String lastname, String password) {
        super(firstname, lastname);
        this.password = password;
    }

    public Admin(long aid, String firstName, String surname, String password) {
        super(firstName, surname);
        this.aid = new SimpleLongProperty(aid);
        this.password =  password;
    }

    public String getPassword() {
        return password;
    }

    public long getAid() { return aid.get(); }

    @Override
    public boolean isAdmin() {
        return true;
    }
}