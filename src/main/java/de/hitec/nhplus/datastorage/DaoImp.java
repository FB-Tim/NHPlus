package de.hitec.nhplus.datastorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base implementation of the {@link Dao} interface.
 * <p>
 * Provides common CRUD operations with JDBC, while delegating
 * SQL statement preparation and object mapping to subclasses.
 *
 * @param <T> The type of the entity the DAO handles.
 */
public abstract class DaoImp<T> implements Dao<T> {
    protected Connection connection;

    /**
     * Constructs a DaoImp with the given database connection.
     *
     * @param connection the JDBC connection to use.
     */
    public DaoImp(Connection connection) {
        this.connection = connection;
    }

    /**
     * Persists the given entity in the database.
     *
     * @param t the entity to create.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void create(T t) throws SQLException {
        getCreateStatement(t).executeUpdate();
    }


    /**
     * Reads an entity identified by the given key from the database.
     *
     * @param key the unique identifier of the entity.
     * @return the entity if found, or null if not found.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public T read(long key) throws SQLException {
        T object = null;
        ResultSet result = getReadByIDStatement(key).executeQuery();
        if (result.next()) {
            object = getInstanceFromResultSet(result);
        }
        return object;
    }

    /**
     * Reads all entities of this type from the database.
     *
     * @return a list of all entities.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public List<T> readAll() throws SQLException {
        return getListFromResultSet(getReadAllStatement().executeQuery());
    }

    /**
     * Updates the given entity in the database.
     *
     * @param t the entity to update.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void update(T t) throws SQLException {
        getUpdateStatement(t).executeUpdate();
    }

    /**
     * Deletes the entity identified by the given key from the database.
     *
     * @param key the unique identifier of the entity to delete.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void deleteById(long key) throws SQLException {
        getDeleteStatement(key).executeUpdate();
    }

    /**
     * Exports the entity identified by the given key.
     * <p>
     * The specific export behavior is defined by subclasses.
     *
     * @param key the unique identifier of the entity to export.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void exportById(long key) throws SQLException{
        getExportStatement(key).executeUpdate();
    }

    /**
     * Maps a single row from the given {@link ResultSet} to an entity instance.
     *
     * @param set the result set positioned at a valid row.
     * @return an instance of the entity mapped from the current row.
     * @throws SQLException if a database access error occurs.
     */
    protected abstract T getInstanceFromResultSet(ResultSet set) throws SQLException;

    /**
     * Maps all rows from the given {@link ResultSet} to a list of entities.
     *
     * @param set the result set containing multiple rows.
     * @return a list of entity instances mapped from the result set.
     * @throws SQLException if a database access error occurs.
     */
    protected abstract ArrayList<T> getListFromResultSet(ResultSet set) throws SQLException;

    /**
     * Creates a {@link PreparedStatement} for inserting the given entity into the database.
     *
     * @param t the entity to insert.
     * @return the prepared statement ready for execution.
     */
    protected abstract PreparedStatement getCreateStatement(T t);

    /**
     * Creates a {@link PreparedStatement} for querying an entity by its unique key.
     *
     * @param key the unique identifier of the entity.
     * @return the prepared statement ready for execution.
     */
    protected abstract PreparedStatement getReadByIDStatement(long key);

    /**
     * Creates a {@link PreparedStatement} for querying all entities of this type.
     *
     * @return the prepared statement ready for execution.
     */
    protected abstract PreparedStatement getReadAllStatement();

    /**
     * Creates a {@link PreparedStatement} for updating the given entity in the database.
     *
     * @param t the entity to update.
     * @return the prepared statement ready for execution.
     */
    protected abstract PreparedStatement getUpdateStatement(T t);

    /**
     * Creates a {@link PreparedStatement} for deleting an entity identified by the given key.
     *
     * @param key the unique identifier of the entity to delete.
     * @return the prepared statement ready for execution.
     */
    protected abstract PreparedStatement getDeleteStatement(long key);

    /**
     * Creates a {@link PreparedStatement} for exporting an entity identified by the given key.
     * <p>
     * The exact behavior depends on the subclass implementation.
     *
     * @param key the unique identifier of the entity to export.
     * @return the prepared statement ready for execution.
     */
    protected abstract PreparedStatement getExportStatement(long key);
}
