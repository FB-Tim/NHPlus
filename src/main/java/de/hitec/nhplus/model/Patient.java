package de.hitec.nhplus.model;

import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Patients live in a NURSING home and are treated by nurses.
 */
public class Patient extends Person {
    private SimpleLongProperty pid;
    private final SimpleStringProperty dateOfBirth;
    private final SimpleStringProperty careLevel;
    private final SimpleStringProperty roomNumber;
    private final List<Treatment> allTreatments = new ArrayList<>();
    private boolean status;
    private LocalDate delete_date;

    /**
     * Constructor to initiate an object of class <code>Patient</code> with the given parameter. Use this constructor
     * to initiate objects, which are not persisted yet, because it will not have a patient id (pid).
     *
     * @param firstName First name of the patient.
     * @param surname Last name of the patient.
     * @param dateOfBirth Date of birth of the patient.
     * @param careLevel Care level of the patient.
     * @param roomNumber Room number of the patient.
     */
    public Patient(String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomNumber, boolean status) {
        super(firstName, surname);
        this.dateOfBirth = new SimpleStringProperty(DateConverter.convertLocalDateToString(dateOfBirth));
        this.careLevel = new SimpleStringProperty(careLevel);
        this.roomNumber = new SimpleStringProperty(roomNumber);
    }

    /**
     * Constructor to initiate an object of class <code>Patient</code> with the given parameter. Use this constructor
     * to initiate objects, which are already persisted and have a patient id (pid).
     *
     * @param pid Patient id.
     * @param firstName First name of the patient.
     * @param surname Last name of the patient.
     * @param dateOfBirth Date of birth of the patient.
     * @param careLevel Care level of the patient.
     * @param roomNumber Room number of the patient.
     */
    public Patient(long pid, String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomNumber, boolean status) {
        super(firstName, surname);
        this.pid = new SimpleLongProperty(pid);
        this.dateOfBirth = new SimpleStringProperty(DateConverter.convertLocalDateToString(dateOfBirth));
        this.careLevel = new SimpleStringProperty(careLevel);
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.status = status;
    }
    public Patient(long pid, String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomNumber, boolean status, LocalDate delete_date) {
        super(firstName, surname);
        this.pid = new SimpleLongProperty(pid);
        this.dateOfBirth = new SimpleStringProperty(DateConverter.convertLocalDateToString(dateOfBirth));
        this.careLevel = new SimpleStringProperty(careLevel);
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.status = status;
        this.delete_date = delete_date;
    }

    /**
     * Returns the patient ID.
     *
     * @return the patient's unique identifier
     */
    public long getPid() {
        return pid.get();
    }

    /**
     * Returns the patient ID property.
     *
     * @return the SimpleLongProperty representing the patient ID
     */
    public SimpleLongProperty pidProperty() {
        return pid;
    }


    /**
     * Returns the patient's date of birth as a string.
     *
     * @return date of birth in string format (YYYY-MM-DD)
     */
    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    public LocalDate getDateOfDelete() {
        return delete_date == null ? null : delete_date;
    }



    /**
     * Returns the date of birth property.
     *
     * @return the SimpleStringProperty representing the date of birth
     */
    public SimpleStringProperty dateOfBirthProperty() {
        return dateOfBirth;
    }

    public void setDateOfDelete(LocalDate date) {this.delete_date = date;}


    /**
     * Stores the given string as new <code>birthOfDate</code>.
     *
     * @param dateOfBirth as string in the following format: YYYY-MM-DD.
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    /**
     * Returns the care level of the patient.
     *
     * @return the care level string
     */
    public String getCareLevel() {
        return careLevel.get();
    }

    /**
     * Returns the care level property.
     *
     * @return the SimpleStringProperty representing the care level
     */
    public SimpleStringProperty careLevelProperty() {
        return careLevel;
    }

    /**
     * Sets the care level of the patient.
     *
     * @param careLevel the care level to set
     */
    public void setCareLevel(String careLevel) {
        this.careLevel.set(careLevel);
    }

    /**
     * Returns the room number of the patient.
     *
     * @return the room number string
     */
    public String getRoomNumber() {
        return roomNumber.get();
    }

    /**
     * Returns the room number property.
     *
     * @return the SimpleStringProperty representing the room number
     */
    public SimpleStringProperty roomNumberProperty() {
        return roomNumber;
    }

    /**
     * Sets the room number of the patient.
     *
     * @param roomNumber the room number to set
     */
    public void setRoomNumber(String roomNumber) {
        this.roomNumber.set(roomNumber);
    }

    public String getStatus() {
        return status ? "Archived" : "Active";
    }
    
    public boolean getStatusBool() {
        return status;
    }

    public void setStatus(boolean status) {this.status = status;}

    /**
     * Checks whether this person is an admin.
     * For patients, this is delegated to the superclass implementation.
     *
     * @return {@code true} if this person is an admin, {@code false} otherwise
     */
    @Override
    public boolean isAdmin() {
        return super.isAdmin();
    }

    /**
     * Adds a treatment to the list of treatments, if the list does not already contain the treatment.
     *
     * @param treatment Treatment to add.
     * @return False, if the treatment was already part of the list, else true.
     */
    public boolean add(Treatment treatment) {
        if (this.allTreatments.contains(treatment)) {
            return false;
        }
        this.allTreatments.add(treatment);
        return true;
    }

    /**
     * Returns a string representation of the patient,
     * including patient ID, first name, surname, date of birth, care level, and room number.
     *
     * @return formatted string representing this patient
     */
    public String toString() {
        return "Patient" + "\nMNID: " + this.pid +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nBirthday: " + this.dateOfBirth +
                "\nCarelevel: " + this.careLevel +
                "\nRoomnumber: " + this.roomNumber +
                "\nStatus: " + this.getStatus() +
                "\n";
    }
}