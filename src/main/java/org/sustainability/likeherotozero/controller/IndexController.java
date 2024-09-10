package org.sustainability.likeherotozero.controller;

import org.sustainability.likeherotozero.dao.CountryDAO;
import org.sustainability.likeherotozero.dao.EmissionDataDAO;
import org.sustainability.likeherotozero.entity.Country;
import org.sustainability.likeherotozero.entity.EmissionData;
import org.sustainability.likeherotozero.service.SessionService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

/**
 * Controller responsible for managing the index page.
 * This controller retrieves the most recent emission data for the user's country
 * based on their locale information.
 */
@Named
@ViewScoped
public class IndexController implements Serializable {

    @Inject
    private EmissionDataDAO emissionDataDAO;

    @Inject
    private CountryDAO countryDAO;

    @Inject
    private SessionService sessionService;

    /**
     * Retrieves the most recent emission data for the country associated with the user's locale.
     * The country is determined based on the ISO3 country code of the user's locale.
     *
     * @return the newest EmissionData for the user's country, or null if the country is not found.
     */
    public EmissionData getNewestEmissionData() {
        String countryCode = sessionService.getLocale().getISO3Country();
        Country country = countryDAO.findOneByCode(countryCode);

        System.out.println("Country : " + country.getName());
        System.out.println("Country code :" + countryCode);

        if (null == country) {
            return null;
        }

        return emissionDataDAO.findNewestByCountry(country);
    }
}