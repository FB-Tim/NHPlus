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
     * When <code>initialize()</code> gets called, all fields are already initialized. For example from the FXMLLoader
     * after loading an FXML-File. At this point of the lifecycle of the Controller, the fields can be accessed and
     * configured.
     */
    public void initialize() {
        setupTableColumns();
        loadAndShowData();
        setupSelectionListener();
        setupInputValidationListener();
        applyUserPermissions();
    }

    private void setupTableColumns() {
        colID.setCellValueFactory(new PropertyValueFactory<>("nid"));

        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colSurname.setCellFactory(TextFieldTableCell.forTableColumn());

        colTelephone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colTelephone.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    private void loadAndShowData() {
        readAllAndShowInTableView();
        tableView.setItems(nurses);
    }

    private void setupSelectionListener() {
        btnDelete.setDisable(true);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btnDelete.setDisable(newSelection == null);
        });
    }

    private void setupInputValidationListener() {
        btnAdd.setDisable(true);
        ChangeListener<String> inputListener = (obs, oldVal, newVal) -> {
            btnAdd.setDisable(!areInputDataValid());
        };
        txfSurname.textProperty().addListener(inputListener);
        txfFirstname.textProperty().addListener(inputListener);
        txfTelephone.textProperty().addListener(inputListener);
    }

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
     * When a cell of the column with first names was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditFirstname(TableColumn.CellEditEvent<Nurse, String> event) {
        event.getRowValue().setFirstName(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * When a cell of the column with surnames was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditSurname(TableColumn.CellEditEvent<Nurse, String> event) {
        event.getRowValue().setSurname(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * When a cell of the column with dates of birth was changed, this method will be called, to persist the change.
     *
     * @param event Event including the changed object and the change.
     */
    @FXML
    public void handleOnEditTelephone(TableColumn.CellEditEvent<Nurse, String> event) {
        event.getRowValue().setPhoneNumber(event.getNewValue());
        this.doUpdate(event);
    }

    /**
     * Updates a nurse by calling the method <code>update()</code> of {@link NurseDao}
     *
     * @param event Event including the changed object and the change.
     */
    private void doUpdate(TableColumn.CellEditEvent<Nurse, String> event) {
        try {
            this.dao.update(event.getRowValue());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Reloads all nurses to the table by clearing the list of all nurses and filling it again by all persisted
     * nurses, delivered by {@link NurseDao}.
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
     * This method handles events fired by the button to delete nurses. It calls {@link NurseDao} to delete the
     * nurse from the database and removes the object from the list, which is the data source of the
     * <code>TableView</code>.
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
     * This method handles the events fired by the button to add a nurse. It collects the data from the
     * <code>TextField</code>s, creates an object of class <code>Nurse</code> of it and passes the object to
     * {@link NurseDao} to persist the data.
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
     * Clears all contents from all <code>TextField</code>s.
     */
    private void clearTextfields() {
        this.txfFirstname.clear();
        this.txfSurname.clear();
        this.txfTelephone.clear();
    }

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
