package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.Nurse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NurseDao extends DaoImp<Nurse> {

    /**
     * The constructor initiates an object of <code>NurseDao</code> and passes the connection to its super class.
     *
     * @param connection Object of <code>Connection</code> to execute the SQL-statements.
     */
    public NurseDao(Connection connection) { super(connection); }

    /**
     * Generates a <code>PreparedStatement</code> to persist the given object of <code>Nurse</code>.
     *
     * @param nurse Object of <code>Nurse</code> to persist.
     * @return <code>PreparedStatement</code> to insert the given nurse.
     */
    @Override
    protected PreparedStatement getCreateStatement(Nurse nurse) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "INSERT INTO nurse (firstname, surname, phoneNumber, password) " + "VALUES (?, ?, ?, ?)";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, nurse.getFirstName());
            preparedStatement.setString(2, nurse.getSurname());
            preparedStatement.setString(3, nurse.getPhoneNumber());
            preparedStatement.setString(4, nurse.getPassword());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a <code>PreparedStatement</code> to query a nurse by a given nurse id (nid).
     *
     * @param nid Nurse id to query.
     * @return <code>PreparedStatement</code> to query the nurse.
     */
    @Override
    protected PreparedStatement getReadByIDStatement(long nid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "SELECT * FROM nurse WHERE id = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, nid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Maps a <code>ResultSet</code> of one nurse to an object of <code>Nurse</code>.
     *
     * @param result ResultSet with a single row. Columns will be mapped to an object of class <code>Nurse</code>.
     * @return Object of class <code>Nurse</code> with the data from the resultSet.
     */
    @Override
    protected Nurse getInstanceFromResultSet(ResultSet result) throws SQLException {
        return new Nurse(
            result.getInt(1),
            result.getString(2),
            result.getString(3),
            result.getString(4),
            result.getString(5)
        );
    }

    /**
     * Generates a <code>PreparedStatement</code> to query all nurses.
     *
     * @return <code>PreparedStatement</code> to query all nurses.
     */
    @Override
    protected PreparedStatement getReadAllStatement() {
        PreparedStatement statement = null;
        try {
            final String SQL = "SELECT * FROM nurse";
            statement = this.connection.prepareStatement(SQL);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return statement;
    }

    /**
     * Maps a <code>ResultSet</code> of all nurses to an <code>ArrayList</code> of <code>Nurse</code> objects.
     *
     * @param result ResultSet with all rows. The Columns will be mapped to objects of class <code>Nurse</code>.
     * @return <code>ArrayList</code> with objects of class <code>Nurse</code> of all rows in the
     * <code>ResultSet</code>.
     */
    @Override
    protected ArrayList<Nurse> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Nurse> list = new ArrayList<>();
        while (result.next()) {
            Nurse nurse = new Nurse(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
            list.add(nurse);
        }
        return list;
    }

    public Nurse findByFirstName(String firstName) {
        Nurse nurse = null;
        try {
            String sql = "SELECT * FROM nurse WHERE firstname = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, firstName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nurse = getInstanceFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nurse;
    }

    /**
     * Generates a <code>PreparedStatement</code> to update the given nurse, identified
     * by the id of the nurse (nid).
     *
     * @param nurse Nurse object to update.
     * @return <code>PreparedStatement</code> to update the given nurse.
     */
    @Override
    protected PreparedStatement getUpdateStatement(Nurse nurse) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL =
                "UPDATE nurse SET " +
                    "firstname = ?, " +
                    "surname = ?, " +
                    "phoneNumber = ? " +
                    "password = ? " +
                    "WHERE id = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, nurse.getFirstName());
            preparedStatement.setString(2, nurse.getSurname());
            preparedStatement.setString(3, nurse.getPhoneNumber());
            preparedStatement.setString(4, nurse.getPassword());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a <code>PreparedStatement</code> to delete a nurse with the given id.
     *
     * @param nid Id of the nurse to delete.
     * @return <code>PreparedStatement</code> to delete nurse with the given id.
     */
    @Override
    protected PreparedStatement getDeleteStatement(long nid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "DELETE FROM nurse WHERE id = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, nid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    @Override
    protected PreparedStatement getExportStatement(long nid) {
        PreparedStatement preparedStatement = null;
        try{
            final String SQL = "SELECT * FROM nurse WHERE id = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, nid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }
}
