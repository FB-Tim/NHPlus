package de.hitec.nhplus.controller;

import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.TreatmentDao;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import de.hitec.nhplus.model.Patient;
import de.hitec.nhplus.model.Treatment;
import de.hitec.nhplus.utils.DateConverter;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Controller class for creating a new treatment entry for a specific patient.
 * Handles form inputs, data validation, and saving the treatment to the database.
 */
public class NewTreatmentController {

    @FXML
    private Label labelFirstName;

    @FXML
    private Label labelSurname;

    @FXML
    private TextField textFieldBegin;

    @FXML
    private TextField textFieldEnd;

    @FXML
    private TextField textFieldDescription;

    @FXML
    private TextArea textAreaRemarks;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button buttonAdd;

    private AllTreatmentController controller;
    private Patient patient;
    private Stage stage;

    /**
     * Initializes the controller with necessary references and sets up listeners and UI components.
     *
     * @param controller the main treatment controller for data refresh
     * @param stage      the current window/stage
     * @param patient    the patient for whom the treatment is being created
     */
    public void initialize(AllTreatmentController controller, Stage stage, Patient patient) {

        this.controller= controller;
        this.patient = patient;
        this.stage = stage;

        this.buttonAdd.setDisable(true);
        ChangeListener<String> inputNewPatientListener = (observableValue, oldText, newText) ->
                NewTreatmentController.this.buttonAdd.setDisable(NewTreatmentController.this.areInputDataInvalid());

        this.textFieldBegin.textProperty().addListener(inputNewPatientListener);
        this.textFieldEnd.textProperty().addListener(inputNewPatientListener);
        this.textFieldDescription.textProperty().addListener(inputNewPatientListener);
        this.textAreaRemarks.textProperty().addListener(inputNewPatientListener);
        this.datePicker.valueProperty().addListener((observableValue, localDate, t1) -> NewTreatmentController.this.buttonAdd.setDisable(NewTreatmentController.this.areInputDataInvalid()));
        this.datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate localDate) {
                return (localDate == null) ? "" : DateConverter.convertLocalDateToString(localDate);
            }

            @Override
            public LocalDate fromString(String localDate) {
                return DateConverter.convertStringToLocalDate(localDate);
            }
        });
        this.showPatientData();
    }


    /**
     * Displays the current patient's first name and surname on the form.
     */
    private void showPatientData(){
        this.labelFirstName.setText(patient.getFirstName());
        this.labelSurname.setText(patient.getSurname());
    }

    /**
     * Handles the event when the "Add" button is pressed.
     * Collects form data, creates a treatment object, stores it, and refreshes the view.
     */
    @FXML
    public void handleAdd(){
        LocalDate date = this.datePicker.getValue();
        LocalTime begin = DateConverter.convertStringToLocalTime(textFieldBegin.getText());
        LocalTime end = DateConverter.convertStringToLocalTime(textFieldEnd.getText());
        String description = textFieldDescription.getText();
        String remarks = textAreaRemarks.getText();
        boolean status = false;
        Treatment treatment = new Treatment(patient.getPid(), date, begin, end, description, remarks, status); // "Status" zum Versuch
        createTreatment(treatment);
        controller.readAllAndShowInTableView();
        stage.close();
    }

    /**
     * Saves a new treatment to the database using the DAO.
     *
     * @param treatment the treatment object to be persisted
     */
    private void createTreatment(Treatment treatment) {
        TreatmentDao dao = DaoFactory.getDaoFactory().createTreatmentDao();
        try {
            dao.create(treatment);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Handles the event when the "Cancel" button is pressed.
     * Closes the current window without saving.
     */
    @FXML
    public void handleCancel(){
        stage.close();
    }

    /**
     * Validates all input fields to ensure correctness.
     * - Ensures times are in correct format and chronological
     * - Description is not blank
     * - Date is selected
     *
     * @return {@code true} if any input is invalid, {@code false} otherwise
     */
    private boolean areInputDataInvalid() {
        if (this.textFieldBegin.getText() == null || this.textFieldEnd.getText() == null) {
            return true;
        }
        try {
            LocalTime begin = DateConverter.convertStringToLocalTime(this.textFieldBegin.getText());
            LocalTime end = DateConverter.convertStringToLocalTime(this.textFieldEnd.getText());
            if (!end.isAfter(begin)) {
                return true;
            }
        } catch (Exception exception) {
            return true;
        }
        return this.textFieldDescription.getText().isBlank() || this.datePicker.getValue() == null;
    }

}