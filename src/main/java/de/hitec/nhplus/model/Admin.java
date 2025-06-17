package de.hitec.nhplus.model;

import javafx.beans.property.SimpleLongProperty;

/**
 * Represents an administrator user in the system.
 * This class extends {@link Person} and adds admin-specific attributes
 * such as an admin ID and a password.
 */
public class Admin extends Person {

    private SimpleLongProperty aid;
    private final String password;

    /**
     * Constructs a new Admin with the given first name, last name, and password.
     * The admin ID is not initialized in this constructor.
     *
     * @param firstname the first name of the admin
     * @param lastname  the last name of the admin
     * @param password  the password of the admin
     */
    public Admin(String firstname, String lastname, String password) {
        super(firstname, lastname);
        this.password = password;
    }

    /**
     * Constructs a new Admin with the specified admin ID, first name, last name, and password.
     *
     * @param aid       the unique identifier for the admin
     * @param firstName the first name of the admin
     * @param surname   the last name of the admin
     * @param password  the password of the admin
     */
    public Admin(long aid, String firstName, String surname, String password) {
        super(firstName, surname);
        this.aid = new SimpleLongProperty(aid);
        this.password =  password;
    }

    /**
     * Returns the password of this admin.
     *
     * @return the admin password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the admin ID.
     *
     * @return the admin's unique identifier
     */
    public long getAid() { return aid.get(); }

    /**
     * Indicates whether this person is an admin.
     * This method always returns {@code true} for instances of {@code Admin}.
     *
     * @return {@code true} since this object represents an admin
     */
    @Override
    public boolean isAdmin() {
        return true;
    }
}