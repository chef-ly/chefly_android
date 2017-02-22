package com.se491.chef_ly.model;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by davidchang on 2/19/17.
 */

public class User {
    private static String username = null;
    private static String authToken = null;

    private User() {
        // Hide the constructor as this is a singleton object
    }

    public static boolean isAuthenticated() {
        return (username != null
                && authToken != null
                && !username.isEmpty()
                && !authToken.isEmpty());
    }

    public static void removeAuthentication() {
        username = null;
        authToken = null;
    }

    public static void saveAuthentication(String inpUsername, String inpAuthToken) {
        if (inpUsername == null || inpAuthToken == null || inpUsername.isEmpty() || inpAuthToken.isEmpty()) {
            throw new IllegalArgumentException("Both username and authToken must not be null or empty");
        }

        username = inpUsername;
        authToken = inpAuthToken;
    }

    public static boolean authenticateExisting(String username, String password) {
        return true;
    }

    public static String getUsername() {
        return username;
    }

    public static String getAuthToken() {
        return authToken;
    }
}
