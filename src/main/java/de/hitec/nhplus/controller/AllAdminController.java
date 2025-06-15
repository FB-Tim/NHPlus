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
     * When <code>initialize()</code> gets called, all fields are already initialized. For example from the FXMLLoader
     * after loading an FXML-File. At this point of the lifecycle of the Controller, the fields can be accessed and
     * configured.
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
     * When a cell of the column with first names was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditFirstname(TableColumn.CellEditEvent<Admin, String> event) {
        event.getRowValue().setFirstName(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * When a cell of the column with surnames was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditSurname(TableColumn.CellEditEvent<Admin, String> event) {
        event.getRowValue().setSurname(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * Updates a admin by calling the method <code>update()</code> of {@link AdminDao}
     *
     * @param event Event including the changed object and the change.
     */
    private void doUpdate(TableColumn.CellEditEvent<Admin, String> event) {
        try {
            this.dao.update(event.getRowValue());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Reloads all admins to the table by clearing the list of all admins and filling it again by all persisted
     * admins, delivered by {@link AdminDao}.
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
     * This method handles events fired by the button to delete admins. It calls {@link AdminDao} to delete the
     * admin from the database and removes the object from the list, which is the data source of the
     * <code>TableView</code>.
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
     * This method handles the events fired by the button to add a admin. It collects the data from the
     * <code>TextField</code>s, creates an object of class <code>Admin</code> of it and passes the object to
     * {@link AdminDao} to persist the data.
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

    private boolean areInputDataValid() {
        return !this.txfFirstname.getText().isBlank() && !this.txfSurname.getText().isBlank();
    }
}
