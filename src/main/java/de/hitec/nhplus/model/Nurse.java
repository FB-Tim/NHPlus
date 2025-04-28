package de.hitec.nhplus.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Nurse extends Person {
    private SimpleLongProperty nid;
    private final SimpleStringProperty phoneNumber;

    public Nurse(String firstName, String lastName, String phoneNumber) {
        super(firstName, lastName);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
    }

    public Nurse(long nid, String firstName, String surname, String phoneNumber) {
        super(firstName, surname);
        this.nid = new SimpleLongProperty(nid);
        this.phoneNumber =  new SimpleStringProperty(phoneNumber);
    }

    public SimpleLongProperty nidProperty() { return nid; }

    public long getNid() { return nid.get(); }

    public SimpleStringProperty phoneNumberProperty() { return phoneNumber; }

    public String getPhoneNumber() { return phoneNumber.get(); }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber.set(phoneNumber); }

    @Override
    public String toString() {
        return "Nurse" + "\nMNID: " + this.nid +
            "\nFirstname: " + this.getFirstName() +
            "\nSurname: " + this.getSurname() +
            "\nPhone: " + this.phoneNumber;
    }
}
