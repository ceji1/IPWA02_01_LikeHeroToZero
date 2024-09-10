package org.sustainability.likeherotozero.controller;

import org.sustainability.likeherotozero.dao.CountryDAO;
import org.sustainability.likeherotozero.dao.EmissionDataDAO;
import org.sustainability.likeherotozero.dao.UserRoleDAO;
import org.sustainability.likeherotozero.entity.Country;
import org.sustainability.likeherotozero.entity.EmissionData;
import org.sustainability.likeherotozero.entity.User;
import org.sustainability.likeherotozero.entity.UserRole;
import org.sustainability.likeherotozero.service.EntityManagerService;
import org.sustainability.likeherotozero.service.SessionService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing emission data.
 * This controller is responsible for handling CRUD operations,
 * user access control, and data validation related to emission records.
 * It is used in a JSF (Jakarta Server Faces) context and interacts with
 * various DAO classes and services to manage emission data for different countries.
 */
@Named
@ViewScoped
public class EmissionDataController implements Serializable {

    @Inject
    private SessionService sessionService;

    @Inject
    private CountryDAO countryDAO;

    @Inject
    private UserRoleDAO userRoleDAO;

    @Inject
    private EmissionDataDAO emissionDataDAO;

    @Inject
    private EntityManagerService entityManagerService;

    private int selectedCountryId;
    private EmissionData emissionData = null;

    /**
     * Default constructor.
     */
    public EmissionDataController() {

    }

    /**
     * Gets the ID of the currently selected country.
     *
     * @return the ID of the selected country.
     */
    public int getSelectedCountryId() {
        return selectedCountryId;
    }

    /**
     * Sets the ID of the selected country and initializes a new EmissionData instance.
     *
     * @param selectedCountryId the ID of the selected country.
     */
    public void setSelectedCountryId(int selectedCountryId) {
        this.selectedCountryId = selectedCountryId;
        this.initializeNewEmissionData();
    }

    /**
     * Checks if the current user is allowed to create or edit emission data.
     *
     * @return true if the user has the role 'ROLE_REPORTER', false otherwise.
     */
    public boolean isUserAllowedToCreateOrEdit() {
        User user = sessionService.getUser();
        UserRole requiredUserRole = userRoleDAO.findOneByRoleName("ROLE_SCIENTIST");

        if (null == user) {
            return false;
        }

        return user.getUserRoles().contains(requiredUserRole);
    }

    /**
     * Validates the selected country during form submission.
     *
     * @param context   the FacesContext.
     * @param component the UIComponent being validated.
     * @param value     the value submitted for the country.
     * @throws ValidatorException if the country is not found.
     */
    public void validateCountryChange(FacesContext context, UIComponent component, Object value) {
        Country country = countryDAO.findOneById((int) value);

        if (null == country) {
            throw new ValidatorException(new FacesMessage("Country not found!"));
        }
    }

    /**
     * Retrieves the list of emission data for the selected country.
     *
     * @return a list of EmissionData associated with the selected country.
     */
    public List<EmissionData> getEmissionDataBySelectedCountry() {
        Country country = countryDAO.findOneById(selectedCountryId);

        if (null == country) {
            return new ArrayList<EmissionData>();
        }

        return country.getEmissionData();
    }

    /**
     * Initializes a new EmissionData instance for data entry.
     */
    public void initializeNewEmissionData() {
        this.emissionData = new EmissionData();
    }

    /**
     * Initializes an existing EmissionData instance for editing.
     *
     * @param emissionDataId the ID of the EmissionData to be initialized.
     */
    public void initializingExistingEmissionData(int emissionDataId) {
        this.emissionData = emissionDataDAO.findOneById(emissionDataId);
    }

    /**
     * Gets the current EmissionData instance.
     *
     * @return the current EmissionData instance.
     */
    public EmissionData getEmissionData() {
        if (null == this.emissionData) {
            this.emissionData = new EmissionData();
        }

        return this.emissionData;
    }

    /**
     * Sets the current EmissionData instance.
     *
     * @param emissionData the EmissionData instance to set.
     */
    public void setEmissionData(EmissionData emissionData) {
        this.emissionData = emissionData;
    }

    /**
     * Validates the reported date to ensure it is not in the future.
     * Also checks if an emission entry already exists for the specified date.
     *
     * @param context   the FacesContext.
     * @param component the UIComponent being validated.
     * @param value     the date being validated.
     * @throws ValidatorException if the date is in the future or an entry already exists.
     */
    public void validateReportedForDate(FacesContext context, UIComponent component, Object value) {
        Date reportedForDate = (Date) value;
        Date currentDate = new Date();

        if (reportedForDate.compareTo(currentDate) > 0) {
            throw new ValidatorException(new FacesMessage("Date must not be in the future!"));
        }

        Country country = countryDAO.findOneById(selectedCountryId);
        EmissionData emissionData = emissionDataDAO.findOneByCountryAndDate(country, reportedForDate);

        if (null != emissionData && this.emissionData.getId() != emissionData.getId()) {
            throw new ValidatorException(new FacesMessage("There is already an entry for this date!"));
        }
    }

    /**
     * Validates that the emission value is not negative.
     *
     * @param context   the FacesContext.
     * @param component the UIComponent being validated.
     * @param value     the emission value being validated.
     * @throws ValidatorException if the emission value is negative.
     */
    public void validateEmissionValue(FacesContext context, UIComponent component, Object value) {
        double emissionValue = (double) value;

        if (emissionValue < 0) {
            throw new ValidatorException(new FacesMessage("Emission value must not be negative!"));
        }
    }

    /**
     * Saves the current EmissionData instance.
     * If the data is new, it sets the creation date and associates the data with the selected country.
     */
    public void saveEmissionData() {
        EntityManager entityManager = entityManagerService.getEntityManager();
        EntityTransaction t = entityManager.getTransaction();

        if (0 == this.emissionData.getId()) {
            Country country = countryDAO.findOneById(selectedCountryId);

            this.emissionData.setCreatedBy(sessionService.getUser());
            this.emissionData.setCreatedAt(new Date());
            this.emissionData.setCountry(country);
        }

        this.emissionData.setUpdatedAt(new Date());
        this.emissionData.setApproved(false); // Set new entries as unapproved

        t.begin();
        entityManager.merge(this.emissionData);
        t.commit();
        entityManager.close();

        this.initializeNewEmissionData();
    }

    /**
     * Checks if the current user is allowed to approve emission data.
     *
     * @return true if the user has the role 'ROLE_PUBLISHER', false otherwise.
     */
    public boolean isUserAllowedToApprove() {
        User user = sessionService.getUser();
        UserRole requiredUserRole = userRoleDAO.findOneByRoleName("ROLE_PUBLISHER");

        if (null == user) {
            return false;
        }

        return user.getUserRoles().contains(requiredUserRole);
    }

    /**
     * Retrieves the list of unapproved emission data.
     *
     * @return a list of unapproved EmissionData instances.
     */
    public List<EmissionData> getUnapprovedEmissionData() {
        return emissionDataDAO.findAllUnapproved();
    }

    /**
     * Approves the specified EmissionData instance.
     *
     * @param data the EmissionData instance to approve.
     */
    public void approveEmissionData(EmissionData data) {
        data.setApproved(true);
        saveEmissionData(data);
    }

    /**
     * Rejects (deletes) the specified EmissionData instance.
     *
     * @param data the EmissionData instance to reject.
     */
    public void rejectEmissionData(EmissionData data) {
        EntityManager entityManager = entityManagerService.getEntityManager();
        EntityTransaction t = entityManager.getTransaction();

        t.begin();
        entityManager.remove(entityManager.merge(data));
        t.commit();
        entityManager.close();
    }

    /**
     * Saves the specified EmissionData instance.
     *
     * @param data the EmissionData instance to save.
     */
    private void saveEmissionData(EmissionData data) {
        EntityManager entityManager = entityManagerService.getEntityManager();
        EntityTransaction t = entityManager.getTransaction();

        t.begin();
        entityManager.merge(data);
        t.commit();
        entityManager.close();
    }

    /**
     * Retrieves approved emission data for the selected country.
     *
     * @return a list of approved EmissionData objects for the selected country.
     */
    public List<EmissionData> getApprovedEmissionDataBySelectedCountry() {
        List<EmissionData> allEmissionData = getEmissionDataBySelectedCountry();
        return allEmissionData.stream()
                .filter(EmissionData::isApproved)
                .collect(Collectors.toList());
    }

    /**
     * Returns the current date.
     *
     * @return the current date.
     */
    public Date getCurrentDate() {
        return new Date();
    }
}
