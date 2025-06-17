package de.hitec.nhplus.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Represents a nurse in the system.
 * Extends the {@link Person} class and includes nurse-specific properties
 * such as nurse ID, phone number, and password.
 */
public class Nurse extends Person {
    private SimpleLongProperty nid;
    private String password;
    private final SimpleStringProperty phoneNumber;

    /**
     * Constructs a new Nurse with the given first name, last name, phone number, and password.
     * The nurse ID is not initialized in this constructor.
     *
     * @param firstName   the first name of the nurse
     * @param lastName    the last name of the nurse
     * @param phoneNumber the nurse's phone number
     * @param password    the password of the nurse
     */
    public Nurse(String firstName, String lastName, String phoneNumber, String password) {
        super(firstName, lastName);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.password = password;
    }

    /**
     * Constructs a new Nurse with the specified nurse ID, first name, last name, phone number, and password.
     *
     * @param nid         the unique identifier for the nurse
     * @param firstName   the first name of the nurse
     * @param surname     the last name of the nurse
     * @param phoneNumber the nurse's phone number
     * @param password    the password of the nurse
     */
    public Nurse(long nid, String firstName, String surname, String phoneNumber, String password) {
        super(firstName, surname);
        this.nid = new SimpleLongProperty(nid);
        this.phoneNumber =  new SimpleStringProperty(phoneNumber);
        this.password = password;
    }


    /**
     * Returns the nurse ID property.
     *
     * @return the SimpleLongProperty representing the nurse ID
     */
    public SimpleLongProperty nidProperty() { return nid; }


    /**
     * Returns the nurse ID.
     *
     * @return the nurse's unique identifier
     */
    public long getNid() { return nid.get(); }


    /**
     * Returns the phone number property.
     *
     * @return the SimpleStringProperty representing the phone number
     */
    public SimpleStringProperty phoneNumberProperty() { return phoneNumber; }

    /**
     * Returns the nurse's phone number.
     *
     * @return the phone number as a String
     */
    public String getPhoneNumber() { return phoneNumber.get(); }


    /**
     * Sets the nurse's phone number.
     *
     * @param phoneNumber the new phone number to set
     */
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber.set(phoneNumber); }

    /**
     * Returns the password of the nurse.
     *
     * @return the nurse's password
     */
    public String getPassword(){
        return password;
    }

    /**
     * Sets the password for the nurse.
     *
     * @param password the new password to set
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * Indicates whether this person is an admin.
     * This method delegates to the superclass implementation.
     *
     * @return {@code true} if this person is an admin, {@code false} otherwise
     */
    public boolean isAdmin() {
        return super.isAdmin();
    }

    /**
     * Returns a string representation of the nurse,
     * including nurse ID, first name, surname, and phone number.
     *
     * @return a formatted string representing this nurse
     */
    @Override
    public String toString() {
        return "Nurse" + "\nMNID: " + this.nid +
            "\nFirstname: " + this.getFirstName() +
            "\nSurname: " + this.getSurname() +
            "\nPhone: " + this.phoneNumber;
    }
}
