package de.hitec.nhplus.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.NurseDao;
import de.hitec.nhplus.model.Nurse;
import de.hitec.nhplus.utils.DateConverter;
import de.hitec.nhplus.utils.SessionManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller class for the "All Nurses" management view in the JavaFX application.
 * This class provides the UI logic to display, add, edit, and delete {@link Nurse} entries.
 * It connects UI elements like the {@link TableView} and {@link TextField}s with
 * business logic and persistence via the {@link NurseDao}.
 *
 * It also handles user permissions through {@link SessionManager} to show/hide
 * functionality based on the current user's role (e.g., Admin).
 *
 * Functionality includes:
 * - Displaying nurse records in a table
 * - Inline editing of first name, surname, and telephone
 * - Validation of input data
 * - Creating and deleting nurse records
 * - Dynamic enabling/disabling of buttons based on UI state
 */

public class AllNurseController {

    @FXML
    private TableView<Nurse> tableView;

    @FXML
    private TableColumn<Nurse, Integer> colID;

    @FXML
    private TableColumn<Nurse, String> colFirstName;

    @FXML
    private TableColumn<Nurse, String> colSurname;

    @FXML
    private TableColumn<Nurse, String> colTelephone;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnAdd;

    @FXML
    private TextField txfSurname;

    @FXML
    private TextField txfFirstname;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txfTelephone;



    private final ObservableList<Nurse> nurses = FXCollections.observableArrayList();
    private NurseDao dao;

    /**
     * Initializes the controller after FXML loading.
     * Sets up column mappings, loads data, attaches listeners,
     * and applies UI rules based on user permissions.
     */
    public void initialize() {
        setupTableColumns();
        loadAndShowData();
        setupSelectionListener();
        setupInputValidationListener();
        applyUserPermissions();
    }

    /**
     * Configures the column-to-property bindings and enables in-table editing
     * for first name, surname, and telephone fields.
     */
    private void setupTableColumns() {
        colID.setCellValueFactory(new PropertyValueFactory<>("nid"));

        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colSurname.setCellFactory(TextFieldTableCell.forTableColumn());

        colTelephone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colTelephone.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    /**
     * Loads nurse data from the database and binds it to the TableView.
     */
    private void loadAndShowData() {
        readAllAndShowInTableView();
        tableView.setItems(nurses);
    }

    /**
     * Adds a listener to the TableView selection model to enable or disable
     * the delete button based on whether a nurse is selected.
     */
    private void setupSelectionListener() {
        btnDelete.setDisable(true);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btnDelete.setDisable(newSelection == null);
        });
    }

    /**
     * Adds listeners to input fields to dynamically enable or disable the add button
     * based on whether the inputs are valid.
     */
    private void setupInputValidationListener() {
        btnAdd.setDisable(true);
        ChangeListener<String> inputListener = (obs, oldVal, newVal) -> {
            btnAdd.setDisable(!areInputDataValid());
        };
        txfSurname.textProperty().addListener(inputListener);
        txfFirstname.textProperty().addListener(inputListener);
        txfTelephone.textProperty().addListener(inputListener);
    }

    /**
     * Applies visibility and accessibility settings for UI elements based
     * on the currently logged-in user's permissions.
     * Admin users can modify data, others cannot.
     */
    private void applyUserPermissions() {
        boolean isAdmin = SessionManager.getCurrentUser() != null && SessionManager.getCurrentUser().isAdmin();

        btnAdd.setVisible(isAdmin);
        btnAdd.setManaged(isAdmin);

        btnDelete.setVisible(isAdmin);
        btnDelete.setManaged(isAdmin);

        txfFirstname.setVisible(isAdmin);
        txfFirstname.setManaged(isAdmin);

        txfSurname.setVisible(isAdmin);
        txfSurname.setManaged(isAdmin);

        txfTelephone.setVisible(isAdmin);
        txfTelephone.setManaged(isAdmin);

        txtPassword.setVisible(isAdmin);
        txtPassword.setManaged(isAdmin);

        if (!isAdmin) {
            colFirstName.setEditable(false);
            colSurname.setEditable(false);
            colTelephone.setEditable(false);
            btnAdd.setDisable(true);
            btnDelete.setDisable(true);
        }
    }

    /**
     * Handles inline editing of the nurse's first name in the TableView.
     * Updates the underlying model and persists the change.
     *
     * @param event the edit event containing the new value
     */
    @FXML
    public void handleOnEditFirstname(TableColumn.CellEditEvent<Nurse, String> event) {
        event.getRowValue().setFirstName(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * Handles inline editing of the nurse's surname in the TableView.
     * Updates the underlying model and persists the change.
     *
     * @param event the edit event containing the new value
     */
    @FXML
    public void handleOnEditSurname(TableColumn.CellEditEvent<Nurse, String> event) {
        event.getRowValue().setSurname(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * Handles inline editing of the nurse's telephone number in the TableView.
     * Updates the underlying model and persists the change.
     *
     * @param event the edit event containing the new value
     */
    @FXML
    public void handleOnEditTelephone(TableColumn.CellEditEvent<Nurse, String> event) {
        event.getRowValue().setPhoneNumber(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * Persists changes made to a nurse object after a cell edit event.
     * Uses the {@link NurseDao#update(Nurse)} method.
     *
     * @param event the edit event containing the modified Nurse object
     */
    private void doUpdate(TableColumn.CellEditEvent<Nurse, String> event) {
        try {
            this.dao.update(event.getRowValue());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Reloads all nurse entries from the database using {@link NurseDao#readAll()},
     * clears the current list, and populates the TableView.
     */
    private void readAllAndShowInTableView() {
        this.nurses.clear();
        this.dao = DaoFactory.getDaoFactory().createNurseDao();
        try {
            this.nurses.addAll(this.dao.readAll());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Deletes the currently selected nurse from both the TableView and the database.
     * Uses {@link NurseDao#deleteById(int)} for removal.
     */
    @FXML
    public void handleDelete() {
        Nurse selectedItem = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                DaoFactory.getDaoFactory().createNurseDao().deleteById(selectedItem.getNid());
                this.tableView.getItems().remove(selectedItem);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Adds a new nurse entry to the database and refreshes the TableView.
     * Collects values from input fields, hashes the password using BCrypt,
     * and creates a new {@link Nurse} object.
     */
    @FXML
    public void handleAdd() {
        String surname = this.txfSurname.getText();
        String firstName = this.txfFirstname.getText();
        String phoneNumber = this.txfTelephone.getText();
        String plainPassword = this.txtPassword.getText();
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
        try {
            this.dao.create(new Nurse(firstName, surname, phoneNumber, bcryptHashString));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        readAllAndShowInTableView();
        clearTextfields();
    }

    /**
     * Clears all input fields in the form (first name, surname, telephone).
     */
    private void clearTextfields() {
        this.txfFirstname.clear();
        this.txfSurname.clear();
        this.txfTelephone.clear();
    }

    /**
     * Validates user input before allowing a new nurse to be added.
     * Ensures that no fields are blank and checks the format of the telephone number.
     *
     * @return true if all input fields are valid, false otherwise
     */
    private boolean areInputDataValid() {
        if (!this.txfTelephone.getText().isBlank()) {
            // TODO: Find better Regex
            String regex = "\\(?([\\d \\-\\)\\–\\+\\/\\(]+){6,}\\)?([ .\\-–\\/]?)([\\d]+)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(this.txfTelephone.getText());

            if (!m.matches()) {
                return false;
            }
        }

        return !this.txfFirstname.getText().isBlank() && !this.txfSurname.getText().isBlank() && !this.txfTelephone.getText().isBlank();
    }
}
