package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.Treatment;
import de.hitec.nhplus.utils.DateConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ArchiveDao extends DaoImp<Treatment> {

    public ArchiveDao(Connection connection) {
        super(connection);
    }

    @Override
    protected PreparedStatement getCreateStatement(Treatment treatment) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "INSERT INTO archive (tid, pid, treatment_date, begin, end, description, remark, status, delete_date, comment) " + //Hier wurde auch der Status hinzugefügt
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, treatment.getTid());
            preparedStatement.setLong(2, treatment.getPid());
            preparedStatement.setString(3, treatment.getDate());
            preparedStatement.setString(4, treatment.getBegin());
            preparedStatement.setString(5, treatment.getEnd());
            preparedStatement.setString(6, treatment.getDescription());
            preparedStatement.setString(7, treatment.getRemarks());
            preparedStatement.setBoolean(8, treatment.getStatus());
            if (treatment.getDateOfDelete() == null) {
                preparedStatement.setString(9, null);
            } else {
                preparedStatement.setString(9, treatment.getDateOfDelete().toString());
            }
            preparedStatement.setString(10, treatment.getComment());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }


    @Override
    protected PreparedStatement getReadByIDStatement(long tid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "SELECT * FROM archive WHERE tid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, tid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }


    @Override
    protected Treatment getInstanceFromResultSet(ResultSet result) throws SQLException {
        LocalDate date = DateConverter.convertStringToLocalDate(result.getString(3));
        LocalTime begin = DateConverter.convertStringToLocalTime(result.getString(4));
        LocalTime end = DateConverter.convertStringToLocalTime(result.getString(5));
        String deleteStr = result.getString(9);
        LocalDate delete = deleteStr == null ? null : DateConverter.convertStringToLocalDate(deleteStr);
        return new Treatment(result.getLong(1), result.getLong(2),
                date, begin, end, result.getString(6), result.getString(7), result.getBoolean(8), delete, result.getString(10)); //Hier wurde auch der Status hinzugefügt
    }


    @Override
    protected PreparedStatement getReadAllStatement() {
        PreparedStatement statement = null;
        try {
            final String SQL = "SELECT * FROM archive";
            statement = this.connection.prepareStatement(SQL);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return statement;
    }


    @Override
    protected ArrayList<Treatment> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        while (result.next()) {
            LocalDate date = DateConverter.convertStringToLocalDate(result.getString(3));
            LocalTime begin = DateConverter.convertStringToLocalTime(result.getString(4));
            LocalTime end = DateConverter.convertStringToLocalTime(result.getString(5));
            String deleteStr = result.getString(9);
            LocalDate delete = deleteStr == null ? null : DateConverter.convertStringToLocalDate(deleteStr);
            Treatment treatment = new Treatment(result.getLong(1), result.getLong(2),
                    date, begin, end, result.getString(6), result.getString(7), result.getBoolean(8), delete, result.getString(10)); //Hier wurde auch der Status hinzugefügt
            list.add(treatment);
        }
        return list;
    }


    private PreparedStatement getReadAllTreatmentsOfOnePatientByPid(long pid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "SELECT * FROM archive WHERE pid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, pid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    public List<Treatment> readTreatmentsByPid(long pid) throws SQLException {
        ResultSet result = getReadAllTreatmentsOfOnePatientByPid(pid).executeQuery();
        return getListFromResultSet(result);
    }

    @Override
    protected PreparedStatement getUpdateStatement(Treatment treatment) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL =
                    "UPDATE archive SET " +
                            "pid = ?, " +
                            "treatment_date = ?, " +
                            "begin = ?, " +
                            "end = ?, " +
                            "description = ?, " +
                            "remark = ?, " +
                            "status = ?, " +
                            "delete_date = ?, " +
                            "comment = ? " +
                            "WHERE tid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, treatment.getPid());
            preparedStatement.setString(2, treatment.getDate());
            preparedStatement.setString(3, treatment.getBegin());
            preparedStatement.setString(4, treatment.getEnd());
            preparedStatement.setString(5, treatment.getDescription());
            preparedStatement.setString(6, treatment.getRemarks());
            preparedStatement.setBoolean(7, treatment.getStatus()); // Status mit 7
            preparedStatement.setString(8, treatment.getDateOfDelete().toString()); // Date of Delete
            preparedStatement.setString(9, treatment.getComment());
            preparedStatement.setLong(10, treatment.getTid());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    @Override
    protected PreparedStatement getDeleteStatement(long tid) {
        PreparedStatement preparedStatement = null;
        try {
            final String SQL = "DELETE FROM archive WHERE tid = ?";
            preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, tid);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return preparedStatement;
    }

    public void autoDeletionExpiredRecords() throws SQLException {
        String sqlDelete = "DELETE FROM archive WHERE delete_date <=  DATE ('now')";
        try {
            this.connection.prepareStatement(sqlDelete).execute();
    } catch(SQLException exception){
            exception.printStackTrace();
        }
    }
}




