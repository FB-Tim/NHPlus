package de.hitec.nhplus.utils;

import de.hitec.nhplus.model.Person;

public class SessionManager {
    private static Person currentUser;

    public static void setCurrentUser(Person user) {
        currentUser = user;
    }

    public static Person getCurrentUser() {
        return currentUser;
    }

    public static void clearSession() {
        currentUser = null;
    }
}