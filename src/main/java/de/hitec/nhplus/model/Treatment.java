package de.hitec.nhplus.model;

import de.hitec.nhplus.utils.DateConverter;

import java.time.LocalDate;
import java.time.LocalTime;

public class Treatment {
    private long tid;
    private final long pid;
    private LocalDate date;
    private LocalTime begin;
    private LocalTime end;
    private String description;
    private String remarks;

    /**
     * Constructor to initiate an object of class <code>Treatment</code> with the given parameter. Use this constructor
     * to initiate objects, which are not persisted yet, because it will not have a treatment id (tid).
     *
     * @param pid Id of the treated patient.
     * @param date Date of the Treatment.
     * @param begin Time of the start of the treatment in format "hh:MM"
     * @param end Time of the end of the treatment in format "hh:MM".
     * @param description Description of the treatment.
     * @param remarks Remarks to the treatment.
     */
    public Treatment(long pid, LocalDate date, LocalTime begin,
                     LocalTime end, String description, String remarks) {
        this.pid = pid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.description = description;
        this.remarks = remarks;
    }

    /**
     * Constructor to initiate an object of class <code>Treatment</code> with the given parameter. Use this constructor
     * to initiate objects, which are already persisted and have a treatment id (tid).
     *
     * @param tid Id of the treatment.
     * @param pid Id of the treated patient.
     * @param date Date of the Treatment.
     * @param begin Time of the start of the treatment in format "hh:MM"
     * @param end Time of the end of the treatment in format "hh:MM".
     * @param description Description of the treatment.
     * @param remarks Remarks to the treatment.
     */
    public Treatment(long tid, long pid, LocalDate date, LocalTime begin,
                     LocalTime end, String description, String remarks) {
        this.tid = tid;
        this.pid = pid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.description = description;
        this.remarks = remarks;
    }

    /**
     * Returns the treatment ID.
     *
     * @return treatment ID
     */
    public long getTid() {
        return tid;
    }


    /**
     * Returns the patient ID associated with this treatment.
     *
     * @return patient ID
     */
    public long getPid() {
        return this.pid;
    }

    /**
     * Returns the treatment date as a string.
     *
     * @return treatment date in ISO format (YYYY-MM-DD)
     */
    public String getDate() {
        return date.toString();
    }

    /**
     * Returns the start time of the treatment as a string.
     *
     * @return start time in format "HH:mm:ss"
     */
    public String getBegin() {
        return begin.toString();
    }

    /**
     * Returns the end time of the treatment as a string.
     *
     * @return end time in format "HH:mm:ss"
     */
    public String getEnd() {
        return end.toString();
    }

    /**
     * Sets the treatment date from a string.
     *
     * @param date date string in the format "YYYY-MM-DD"
     */
    public void setDate(String date) {
        this.date = DateConverter.convertStringToLocalDate(date);
    }


    /**
     * Sets the start time of the treatment from a string.
     *
     * @param begin start time string in the format "HH:mm"
     */
    public void setBegin(String begin) {
        this.begin = DateConverter.convertStringToLocalTime(begin);;
    }

    /**
     * Sets the end time of the treatment from a string.
     *
     * @param end end time string in the format "HH:mm"
     */
    public void setEnd(String end) {
        this.end = DateConverter.convertStringToLocalTime(end);;
    }

    /**
     * Returns the description of the treatment.
     *
     * @return treatment description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the treatment.
     *
     * @param description description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns remarks related to the treatment.
     *
     * @return treatment remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets remarks related to the treatment.
     *
     * @param remarks remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * Returns a string representation of the treatment details.
     *
     * @return formatted string with treatment information
     */
    public String toString() {
        return "\nBehandlung" + "\nTID: " + this.tid +
                "\nPID: " + this.pid +
                "\nDate: " + this.date +
                "\nBegin: " + this.begin +
                "\nEnd: " + this.end +
                "\nDescription: " + this.description +
                "\nRemarks: " + this.remarks + "\n";
    }
}
