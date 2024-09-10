package org.sustainability.likeherotozero.controller;

import org.sustainability.likeherotozero.service.SessionService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

/**
 * Controller responsible for managing the logout functionality.
 * This controller handles the process of logging out a user by clearing the session
 * and redirecting them to the index page.
 */
@Named
@ViewScoped
public class LogoutController implements Serializable {

    @Inject
    private SessionService sessionService;

    /**
     * Logs out the current user by invalidating their session.
     * This method clears the user from the session and redirects to the index page.
     *
     * @return the navigation outcome to redirect to the index page.
     */
    public String logout() {
        this.sessionService.setUser(null);
        return "index.xhtml";
    }
}
