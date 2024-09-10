package org.sustainability.likeherotozero.controller;

import org.sustainability.likeherotozero.service.SessionService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Controller class responsible for managing the country selection within the user's session.
 * This class provides methods to change and validate the selected country and handles the available locales.
 */
@Named
@ViewScoped
public class CountryController implements Serializable {

    private final SessionService sessionService;

    private String country;
    private final Map<String, Locale> availableCountries;

    /**
     * Initializes the CountryController and populates the availableCountries map
     * with all available locales.
     */
    @Inject
    public CountryController(SessionService sessionService) {
        this.sessionService = sessionService;

        List<Locale> sortedLocales = Arrays.stream(Locale.getAvailableLocales())
                .filter(locale -> !locale.getCountry().isEmpty()) // Filter out locales without countries
                .sorted(Comparator.comparing(locale -> locale.getDisplayCountry(Locale.ENGLISH)))
                .collect(Collectors.toList());

        availableCountries = new LinkedHashMap<>();
        for (Locale availableLocale : sortedLocales) {
            availableCountries.put(availableLocale.getCountry(), availableLocale);
        }
    }

    /**
     * Gets the currently selected country. If no country is selected, it returns the country
     * from the session's locale.
     *
     * @return the selected country as a string.
     */
    public String getCountry() {
        if (null == this.country) {
            return sessionService.getLocale().getCountry();
        }

        return country;
    }

    /**
     * Sets the currently selected country.
     *
     * @param country the country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets a map of available countries and their corresponding locales.
     *
     * @return a map where the key is the country code and the value is the {@link Locale}.
     */
    public Map<String, Locale> getAvailableCountries() {
        return availableCountries;
    }

    /**
     * Validates if the selected country is available in the list of locales.
     *
     * @param context the FacesContext for the request being processed.
     * @param component the UIComponent associated with the value to validate.
     * @param value the value to validate (country code).
     * @throws ValidatorException if the country is not available.
     */
    public void validateCountryChange(FacesContext context, UIComponent component, Object value) {
        Locale locale = availableCountries.get((String) value);

        if (null == locale) {
            throw new ValidatorException(new FacesMessage("Country not available!"));
        }
    }

    /**
     * Changes the session's locale to the selected country and redirects the user to the index page.
     *
     * @return the navigation outcome, which is "index.xhtml".
     */
    public String changeCountry() {
        Locale locale = this.availableCountries.get(this.country);
        sessionService.setLocale(locale);

        return "index.xhtml";
    }
}