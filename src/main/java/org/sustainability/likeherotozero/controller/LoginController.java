package org.sustainability.likeherotozero.controller;

import org.sustainability.likeherotozero.dao.UserDAO;
import org.sustainability.likeherotozero.entity.User;
import org.sustainability.likeherotozero.service.SessionService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.validator.ValidatorException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;

/**
 * Controller responsible for managing user login functionality.
 * This controller handles the validation of user credentials and manages
 * the user session after a successful login.
 */
@Named
@ViewScoped
public class LoginController implements Serializable {


    @Inject
    private UserDAO userDAO;

    @Inject
    private SessionService sessionService;

    private String username;
    private String password;

    /**
     * Post validation for the username. This method is triggered during the JSF lifecycle
     * to capture the username entered by the user.
     *
     * @param event the ComponentSystemEvent that contains the UI component with the username.
     */
    public void postValidateName(ComponentSystemEvent event) {
        UIInput temp = (UIInput) event.getComponent();
        this.username = (String) temp.getValue();
    }

    /**
     * Sets the username for the current login attempt.
     *
     * @param username the username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the username for the current login attempt.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the password for the current login attempt.
     *
     * @param password the password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the password for the current login attempt.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Attempts to log in the user by validating the username and password.
     * If successful, the user session is established and the user is redirected to the index page.
     *
     * @return the navigation outcome to redirect to the index page, or null if login fails.
     */
    public String login() {
        if (this.password == null || this.username == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username and password are required", null));
            return null;
        }

        User user = this.userDAO.findOneByUsername(this.username);

        if (user == null || !this.password.equals(user.getPassword())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", null));
            return null;
        }

        this.sessionService.setUser(user);
        return "index.xhtml";
    }

    /**
     * Post validation for the password. This method is triggered during the JSF lifecycle
     * to capture the password entered by the user.
     *
     * @param event the ComponentSystemEvent that contains the UI component with the password.
     */
    public void postValidatePassword(ComponentSystemEvent event) {
        UIInput temp = (UIInput) event.getComponent();
        this.password = (String) temp.getValue();
    }
}