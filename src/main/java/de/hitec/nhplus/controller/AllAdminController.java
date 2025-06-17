package de.hitec.nhplus.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.AdminDao;
import de.hitec.nhplus.model.Admin;
import de.hitec.nhplus.utils.DateConverter;
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
 * Controller class for managing the "All Admins" view in the JavaFX application.
 * This class provides functionality for displaying, editing, adding, and deleting
 * admin users in a TableView. It uses the {@link AdminDao} for persistence and
 * {@link Admin} objects for representing each admin entry.
 *
 * The controller binds JavaFX UI components to event handlers and uses listeners
 * for dynamic UI updates, such as enabling or disabling buttons based on input.
 */

public class AllAdminController {

    @FXML
    private TableView<Admin> tableView;

    @FXML
    private TableColumn<Admin, Integer> colID;

    @FXML
    private TableColumn<Admin, String> colFirstName;

    @FXML
    private TableColumn<Admin, String> colSurname;

    @FXML
    private TableColumn<Admin, String> colTelephone;

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

    private final ObservableList<Admin> admins = FXCollections.observableArrayList();
    private AdminDao dao;

    /**
     * Initializes the controller class. This method is automatically called after
     * the FXML file has been loaded and all @FXML annotated fields have been initialized.
     * It sets up the TableView, binds the columns to Admin properties, and adds listeners
     * for UI controls such as text fields and buttons.
     */
    public void initialize() {
        this.readAllAndShowInTableView();

        this.colID.setCellValueFactory(new PropertyValueFactory<>("aid"));

        // CellValueFactory to show property values in TableView
        this.colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        // CellFactory to write property values from with in the TableView
        this.colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        this.colSurname.setCellFactory(TextFieldTableCell.forTableColumn());

        //Anzeigen der Daten
        this.tableView.setItems(this.admins);

        this.btnDelete.setDisable(true);
        this.tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Admin>() {
            @Override
            public void changed(ObservableValue<? extends Admin> observableValue, Admin oldAdmin, Admin newAdmin) {;
                AllAdminController.this.btnDelete.setDisable(newAdmin == null);
            }
        });

        this.btnAdd.setDisable(true);
        ChangeListener<String> inputNewAdminListener = (observableValue, oldText, newText) ->
                AllAdminController.this.btnAdd.setDisable(!AllAdminController.this.areInputDataValid());
        this.txfSurname.textProperty().addListener(inputNewAdminListener);
        this.txfFirstname.textProperty().addListener(inputNewAdminListener);
    }

    /**
     * Handles the editing of the "firstName" column in the TableView.
     * When a first name is edited directly in the table, the new value
     * is persisted using the AdminDao.
     *
     * @param event the edit event containing the updated value and Admin object
     */
    @FXML
    public void handleOnEditFirstname(TableColumn.CellEditEvent<Admin, String> event) {
        event.getRowValue().setFirstName(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * Handles the editing of the "surname" column in the TableView.
     * When a surname is edited directly in the table, the new value
     * is persisted using the AdminDao.
     *
     * @param event the edit event containing the updated value and Admin object
     */
    @FXML
    public void handleOnEditSurname(TableColumn.CellEditEvent<Admin, String> event) {
        event.getRowValue().setSurname(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * Persists updates to Admin objects when a cell in the TableView is edited.
     * It calls the {@link AdminDao#update(Admin)} method.
     *
     * @param event the edit event containing the updated Admin object
     */
    private void doUpdate(TableColumn.CellEditEvent<Admin, String> event) {
        try {
            this.dao.update(event.getRowValue());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Loads all Admin entries from the database and displays them in the TableView.
     * It uses the {@link AdminDao#readAll()} method to retrieve data and updates
     * the observable list that backs the TableView.
     */
    private void readAllAndShowInTableView() {
        this.admins.clear();
        this.dao = DaoFactory.getDaoFactory().createAdminDao();
        try {
            this.admins.addAll(this.dao.readAll());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles the delete button action. It deletes the selected Admin entry
     * from both the database and the TableView.
     * It uses {@link AdminDao#deleteById(int)} to perform the deletion.
     */
    @FXML
    public void handleDelete() {
        Admin selectedItem = this.tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                DaoFactory.getDaoFactory().createAdminDao().deleteById(selectedItem.getAid());
                this.tableView.getItems().remove(selectedItem);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Handles the add button action. It creates a new Admin entry using input
     * from the text fields, hashes the password with BCrypt, and saves the entry
     * using {@link AdminDao#create(Admin)}.
     * The TableView is updated and input fields are cleared afterwards.
     */
    @FXML
    public void handleAdd() {
        String surname = this.txfSurname.getText();
        String firstName = this.txfFirstname.getText();
        String plainPassword = this.txtPassword.getText();
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
        try {
            this.dao.create(new Admin(firstName, surname, bcryptHashString));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        readAllAndShowInTableView();
        clearTextfields();
    }

    /**
     * Clears all contents from all <code>TextField</code>s.
     */
    private void clearTextfields() {
        this.txfFirstname.clear();
        this.txfSurname.clear();
        this.txtPassword.clear();
    }

    /**
     * Validates whether the input fields contain valid data for creating a new admin.
     *
     * @return true if both first name and surname fields are non-blank, false otherwise
     */
    private boolean areInputDataValid() {
        return !this.txfFirstname.getText().isBlank() && !this.txfSurname.getText().isBlank();
    }
}
