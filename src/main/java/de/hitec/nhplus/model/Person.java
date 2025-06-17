package de.hitec.nhplus.model;

import javafx.beans.property.SimpleStringProperty;


/**
 * Abstract base class representing a person with a first name and surname.
 * This class uses JavaFX properties to allow for data binding.
 */
public abstract class Person {
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty surname;

    /**
     * Constructs a new Person with the specified first name and surname.
     *
     * @param firstName The person's first name.
     * @param surname   The person's surname.
     */
    public Person(String firstName, String surname) {
        this.firstName = new SimpleStringProperty(firstName);
        this.surname = new SimpleStringProperty(surname);
    }

    /**
     * Returns the first name of this person.
     *
     * @return the first name.
     */
    public String getFirstName() {
        return firstName.get();
    }

    /**
     * Returns the JavaFX property for the first name.
     * This can be used for data binding.
     *
     * @return the firstName property.
     */
    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    /**
     * Sets the first name of this person.
     *
     * @param firstName the new first name.
     */
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    /**
     * Returns the surname of this person.
     *
     * @return the surname.
     */
    public String getSurname() {
        return surname.get();
    }

    /**
     * Returns the JavaFX property for the surname.
     * This can be used for data binding.
     *
     * @return the surname property.
     */
    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    /**
     * Sets the surname of this person.
     *
     * @param surname the new surname.
     */
    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    /**
     * Indicates whether this person has administrative privileges.
     * By default, returns false; subclasses may override this.
     *
     * @return true if the person is an admin, false otherwise.
     */
    public boolean isAdmin(){ return false;}
}
