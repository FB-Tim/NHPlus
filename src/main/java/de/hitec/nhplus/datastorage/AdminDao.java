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
     * Creates a {@link PreparedStatement} for inserting a new {@link Admin} into the database.
     *
     * @param admin The {@link Admin} object to persist.
     * @return A {@link PreparedStatement} for the insert operation.
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
     * Creates a {@link PreparedStatement} to retrieve an {@link Admin} by its ID.
     *
     * @param nid The ID of the admin to retrieve.
     * @return A {@link PreparedStatement} for the select operation.
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
     * Maps a single row of a {@link ResultSet} to an {@link Admin} object.
     *
     * @param result The {@link ResultSet} positioned at a single row.
     * @return An {@link Admin} object populated with data from the result set.
     * @throws SQLException if an error occurs while reading from the result set.
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
     * Creates a {@link PreparedStatement} to retrieve all admins from the database.
     *
     * @return A {@link PreparedStatement} for retrieving all admin records.
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
     * Maps a {@link ResultSet} containing multiple rows to a list of {@link Admin} objects.
     *
     * @param result The {@link ResultSet} with multiple rows.
     * @return An {@link ArrayList} of {@link Admin} objects.
     * @throws SQLException if an error occurs while reading from the result set.
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
     * Finds an {@link Admin} by their first name.
     *
     * @param firstName The first name of the admin.
     * @return The matching {@link Admin}, or null if none found.
     */
    public Admin findByFirstName(String firstName) {
        Admin admin = null;
        try {
            String sql = "SELECT * FROM admin WHERE firstname = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, firstName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                admin = getInstanceFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }


    /**
     * Creates a {@link PreparedStatement} to update an existing {@link Admin} in the database.
     *
     * @param admin The {@link Admin} object with updated values.
     * @return A {@link PreparedStatement} for the update operation.
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
     * Creates a {@link PreparedStatement} to delete an {@link Admin} by their ID.
     *
     * @param nid The ID of the admin to delete.
     * @return A {@link PreparedStatement} for the delete operation.
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

    /**
     * Not implemented: This DAO does not support exporting by key.
     *
     * @param key The key used for export.
     * @return Always returns null.
     */
    @Override
    protected PreparedStatement getExportStatement(long key) {
        return null;
    }
}
