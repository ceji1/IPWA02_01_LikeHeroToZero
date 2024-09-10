package org.sustainability.likeherotozero.service;

import org.sustainability.likeherotozero.entity.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Locale;

/**
 * Service class responsible for managing session-specific data such as the logged-in user and locale.
 * This class is session-scoped, meaning the data it holds is specific to the user's session.
 */
@Named
@SessionScoped
public class SessionService implements Serializable {

    private User user;
    private Locale locale;

    /**
     * Retrieves the locale for the current session.
     * If no locale is explicitly set, it defaults to the locale of the current view root.
     *
     * @return the {@link Locale} for the current session.
     */
    public Locale getLocale() {
        if (null == locale) {
            return FacesContext.getCurrentInstance().getViewRoot().getLocale();
        }
        return locale;
    }

    /**
     * Sets the locale for the current session.
     *
     * @param locale the {@link Locale} to set for the session.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Sets the user for the current session.
     *
     * @param user the {@link User} to set as the logged-in user.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Retrieves the user for the current session.
     *
     * @return the {@link User} who is currently logged in.
     */
    public User getUser() {
        return user;
    }

    /**
     * Checks if there is a user currently logged in.
     *
     * @return true if a user is logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return null != this.getUser();
    }
}