package org.sustainability.likeherotozero.dao;

import org.sustainability.likeherotozero.entity.Country;
import org.sustainability.likeherotozero.service.EntityManagerService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.io.Serializable;
import java.util.List;

/**
 * Data Access Object (DAO) responsible for handling operations related to the {@link Country} entity.
 * This class provides methods to retrieve countries from the database by various criteria such as ID or code.
 */
@Named
public class CountryDAO implements Serializable {

    @Inject
    private EntityManagerService entityManagerService;

    /**
     * Retrieves a list of all countries from the database.
     *
     * @return a {@link List} of {@link Country} entities.
     */
    public List<Country> findAll() {
        EntityManager entityManager = entityManagerService.getEntityManager();
        Query query = entityManager.createQuery("SELECT c FROM Country c");
        return query.getResultList();
    }

    /**
     * Retrieves a single country by its unique ID.
     *
     * @param countryId the ID of the country to be retrieved.
     * @return the {@link Country} entity if found, or null if no country with the specified ID exists.
     */
    public Country findOneById(int countryId) {
        EntityManager entityManager = entityManagerService.getEntityManager();
        Query query = entityManager.createQuery("SELECT c FROM Country c WHERE id = :id");
        query.setParameter("id", countryId);

        try {
            return (Country) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Retrieves a single country by its code.
     *
     * @param countryCode the code of the country to be retrieved.
     * @return the {@link Country} entity if found, or null if no country with the specified code exists.
     */
    public Country findOneByCode(String countryCode) {
        EntityManager entityManager = entityManagerService.getEntityManager();
        Query query = entityManager.createQuery("SELECT c FROM Country c WHERE code = :code");
        query.setParameter("code", countryCode);

        try {
            return (Country) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
