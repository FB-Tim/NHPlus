package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.*;

import de.hitec.nhplus.utils.DateConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import de.hitec.nhplus.model.Patient;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;

import static de.hitec.nhplus.utils.DateConverter.convertStringToLocalDate;

public class CompletionPatientController {

    @FXML
    private Label labelPatientName;

    @FXML
    private Label labelDateOfBirth;

    @FXML
    private Label labelCareLevel;

    @FXML
    private RadioButton CheckBoxLeave;

    @FXML
    private RadioButton CheckBoxDead;


    @FXML
    private Label labelRemark;

    @FXML
    private Label labelDescription;

    @FXML
    private DatePicker dateOfDelete;


    @FXML
    private Button buttonConfirm;


    @FXML
    private TextArea comment;

    private AllPatientController controller;
    private Patient patient;
    private Stage stage;
    
    private final ObservableList<Patient> patients = FXCollections.observableArrayList();

    public void initializeController(AllPatientController controller, Stage stage, Patient patient) {
        this.stage = stage;
        this.controller= controller;

        PatientDao pDao = DaoFactory.getDaoFactory().createPatientDAO();


        try {
            this.patient = pDao.read((int) patient.getPid());
            this.showPatientData();

            showPatientData();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        this.dateOfDelete.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate localDate) {
                return (localDate == null) ? "" : DateConverter.convertLocalDateToString(localDate);
            }

            @Override
            public LocalDate fromString(String localDate) {
                return DateConverter.convertStringToLocalDate(localDate);
            }
        });
    }

    private void showPatientData(){
        this.labelPatientName.setText(patient.getSurname()+", "+patient.getFirstName());
        this.labelCareLevel.setText(patient.getCareLevel());
        this.labelDateOfBirth.setText(patient.getDateOfBirth());
    }

    @FXML
    public void handleConfirm() {
        if((CheckBoxLeave.isSelected() || CheckBoxDead.isSelected())) this.patient.setStatus(true);

        this.patient.setDateOfDelete(this.dateOfDelete.getValue());

        areInputDataInvalid();
        archivePatient(this.patient);
        controller.readAllAndShowInTableView();
        stage.close();
    }

    private void archivePatient(Patient patient) {
        this.patients.remove(patient);
        ArchivePatientDao archivePatientDao = DaoFactory.getDaoFactory().createArchivePatientDao();

        ArchivePatientDao tDao = DaoFactory.getDaoFactory().createArchivePatientDao();
        try {
            archivePatientDao.create(patient);
            archivePatientDao.autoDeletionExpiredRecords();
            tDao.deleteById(patient.getPid());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void handleCancel(){
        stage.close();
    }

    private boolean areInputDataInvalid() {
        return  (!CheckBoxLeave.isSelected() && !CheckBoxDead.isSelected());
    }

}
