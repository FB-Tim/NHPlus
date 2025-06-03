package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDao extends DaoImp<Admin> {

    /**
     * The constructor initiates an object of <code>AdminDao</code> and passes the connection to its super class.
     *
     * @param connection Object of <code>Connection</code> to execute the SQL-statements.
     */
    public AdminDao(Connection connection) { super(connection); }

    /**
     * Generates a <code>PreparedStatement</code> to persist the given object of <code>Admin</code>.
     *
     * @param Admin Object of <code>Admin</code> to persist.
     * @return <code>PreparedStatement</code> to insert the given Admin.
     */
    @Override
    protected PreparedStatement getCreateStatement(Admin admin) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "INSERT INTO admin (firstname, surname, password) " + "VALUES (?, ?, ?)";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, admin.getFirstName());
            preparedStatement.setString(2, admin.getSurname());
            preparedStatement.setString(3, admin.getPassword());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a <code>PreparedStatement</code> to query a Admin by a given Admin id (nid).
     *
     * @param nid Admin id to query.
     * @return <code>PreparedStatement</code> to query the Admin.
     */
    @Override
    protected PreparedStatement getReadByIDStatement(long nid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "SELECT * FROM admin WHERE id = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, nid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Maps a <code>ResultSet</code> of one Admin to an object of <code>Admin</code>.
     *
     * @param result ResultSet with a single row. Columns will be mapped to an object of class <code>Admin</code>.
     * @return Object of class <code>Admin</code> with the data from the resultSet.
     */
    @Override
    protected Admin getInstanceFromResultSet(ResultSet result) throws SQLException {
        return new Admin(
                result.getInt(1),
                result.getString(2),
                result.getString(3),
                result.getString(4));
    }

    /**
     * Generates a <code>PreparedStatement</code> to query all Admins.
     *
     * @return <code>PreparedStatement</code> to query all Admins.
     */
    @Override
    protected PreparedStatement getReadAllStatement() {
        PreparedStatement statement = null;
        try {
            final String SQL = "SELECT * FROM admin";
            statement = this.connection.prepareStatement(SQL);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return statement;
    }


    /**
     * Maps a <code>ResultSet</code> of all Admins to an <code>ArrayList</code> of <code>Admin</code> objects.
     *
     * @param result ResultSet with all rows. The Columns will be mapped to objects of class <code>Admin</code>.
     * @return <code>ArrayList</code> with objects of class <code>Admin</code> of all rows in the
     * <code>ResultSet</code>.
     */
    @Override
    protected ArrayList<Admin> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Admin> list = new ArrayList<>();
        while (result.next()) {
            Admin Admin = new Admin(result.getInt(1), result.getString(2), result.getString(3), result.getString(4));
            list.add(Admin);
        }
        return list;
    }


    /**
     * Generates a <code>PreparedStatement</code> to update the given Admin, identified
     * by the id of the Admin (nid).
     *
     * @param Admin admin object to update.
     * @return <code>PreparedStatement</code> to update the given Admin.
     */
    @Override
    protected PreparedStatement getUpdateStatement(Admin admin) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL =
                    "UPDATE admin SET " +
                            "firstname = ?, " +
                            "surname = ?, " +
                            "password = ? " +
                            "WHERE id = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, admin.getFirstName());
            preparedStatement.setString(2, admin.getSurname());
            preparedStatement.setString(3, admin.getPassword());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    /**
     * Generates a <code>PreparedStatement</code> to delete a Admin with the given id.
     *
     * @param nid Id of the Admin to delete.
     * @return <code>PreparedStatement</code> to delete Admin with the given id.
     */
    @Override
    protected PreparedStatement getDeleteStatement(long nid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "DELETE FROM admin WHERE id = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, nid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }
}
