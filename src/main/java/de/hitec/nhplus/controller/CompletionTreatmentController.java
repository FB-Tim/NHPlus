package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.ArchiveDao;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.PatientDao;
import de.hitec.nhplus.datastorage.TreatmentDao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.model.Treatment;

import java.sql.SQLException;

import static de.hitec.nhplus.utils.DateConverter.convertStringToLocalDate;

public class CompletionTreatmentController {

    @FXML
    private Label labelPatientName;

    @FXML
    private Label labelDatum;

    @FXML
    private Label labelRemark;

    @FXML
    private Label labelDescription;

    @FXML
    private Label labelCareLevel;

    @FXML
    private Button buttonConfirm;

    @FXML
    private Label labelFieldBegin;

    @FXML
    private Label labelFieldEnd;

    @FXML
    private Label dateOfDelete;

    @FXML
    private RadioButton CheckBoxLeave;

    @FXML
    private RadioButton CheckBoxDead;

    @FXML
    private TextArea comment;

    private AllTreatmentController controller;
    private Patient patient;
    private Stage stage;
    private Treatment treatment;

    private final ObservableList<Treatment> treatments = FXCollections.observableArrayList();

    public void initializeController(AllTreatmentController controller, Stage stage, Treatment treatment) {
        this.stage = stage;
        this.controller= controller;
        PatientDao pDao = DaoFactory.getDaoFactory().createPatientDAO();

        try {
            this.patient = pDao.read((int) treatment.getPid());
            this.treatment = treatment;
            treatment.setDateOfDelete(convertStringToLocalDate(treatment.getDate()).plusYears(10));
            treatment.getStatusLabel(treatment.getStatus());

            showData();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    private void showPatientData(){
        this.labelPatientName.setText(patient.getFirstName());
    }

    @FXML
    public void handleConfirm() {

        if((CheckBoxLeave.isSelected() || CheckBoxDead.isSelected())) this.treatment.setStatus(true);
        this.treatment.setComment(this.comment.getText());
        this.treatment.setDateOfDelete(convertStringToLocalDate(this.dateOfDelete.getText())); ;

        areInputDataInvalid();
        archiveTreatment(this.treatment);
        controller.readAllAndShowInTableView();
        stage.close();
    }

    private void archiveTreatment(Treatment treatment) {
        this.treatments.remove(treatment);
        ArchiveDao archiveDao = DaoFactory.getDaoFactory().createArchiveDao();
        TreatmentDao tDao = DaoFactory.getDaoFactory().createTreatmentDao();
        try {
            archiveDao.create(treatment);
            archiveDao.autoDeletionExpiredRecords();
            tDao.deleteById(treatment.getTid());
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

    private void showData(){
        this.labelPatientName.setText(patient.getSurname()+", "+patient.getFirstName());
        this.labelCareLevel.setText(patient.getCareLevel());
        this.labelDatum.setText(this.treatment.getDate());
        this.labelFieldBegin.setText(this.treatment.getBegin());
        this.labelFieldEnd.setText(this.treatment.getEnd());
        this.labelDescription.setText(this.treatment.getDescription());
        this.labelRemark.setText(this.treatment.getRemarks());
        this.dateOfDelete.setText(this.treatment.getDateOfDelete().toString());
    }
}
