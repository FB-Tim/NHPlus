package de.hitec.nhplus.datastorage;

import java.sql.SQLException;
import java.util.List;

/**
 * Generic Data Access Object (DAO) interface that defines basic CRUD operations
 * for any type of entity.
 *
 * @param <T> The type of the entity for which this DAO is responsible.
 */
public interface Dao<T> {

    /**
     * Persists a new entity into the database.
     *
     * @param t The entity object to be created and stored.
     * @throws SQLException If a database access error occurs.
     */
    void create(T t) throws SQLException;

    /**
     * Retrieves a single entity from the database using its unique identifier.
     *
     * @param key The unique identifier (usually the primary key) of the entity.
     * @return The entity object associated with the given key, or {@code null} if not found.
     * @throws SQLException If a database access error occurs.
     */
    T read(long key) throws SQLException;

    /**
     * Retrieves all entities of this type from the database.
     *
     * @return A {@link List} containing all stored entities of type {@code T}.
     * @throws SQLException If a database access error occurs.
     */
    List<T> readAll() throws SQLException;

    /**
     * Updates an existing entity in the database.
     *
     * @param t The entity object with updated data to be persisted.
     * @throws SQLException If a database access error occurs.
     */
    void update(T t) throws SQLException;

    /**
     * Deletes an entity from the database using its unique identifier.
     *
     * @param key The unique identifier (usually the primary key) of the entity to be deleted.
     * @throws SQLException If a database access error occurs.
     */
    void deleteById(long key) throws SQLException;

    /**
     * Exports an entity from the database using its unique identifier.
     * The specific export format and behavior are implementation-dependent.
     *
     * @param key The unique identifier (usually the primary key) of the entity to be exported.
     * @throws SQLException If a database access error occurs.
     */
    void exportById(long key) throws SQLException;
}
