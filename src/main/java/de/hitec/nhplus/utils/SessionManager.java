package de.hitec.nhplus.utils;

import de.hitec.nhplus.model.Person;


/**
 * Manages the user session by storing the currently logged-in user.
 * <p>
 * This class provides static methods to set, get, and clear the current user session.
 * It acts as a simple session holder within the application runtime.
 * </p>
 */
public class SessionManager {
    private static Person currentUser;

    /**
     * Sets the current user for the session.
     *
     * @param user the {@link Person} object representing the logged-in user;
     *             passing {@code null} will clear the current user
     */
    public static void setCurrentUser(Person user) {
        currentUser = user;
    }

    /**
     * Retrieves the current user of the session.
     *
     * @return the {@link Person} object representing the current user,
     *         or {@code null} if no user is currently set
     */
    public static Person getCurrentUser() {
        return currentUser;
    }

    /**
     * Clears the current session by removing the current user.
     * <p>
     * After calling this method, {@link #getCurrentUser()} will return {@code null}.
     * </p>
     */
    public static void clearSession() {
        currentUser = null;
    }
}