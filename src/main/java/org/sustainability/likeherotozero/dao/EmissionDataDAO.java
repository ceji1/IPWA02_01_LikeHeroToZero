package org.sustainability.likeherotozero.dao;

import org.sustainability.likeherotozero.entity.Country;
import org.sustainability.likeherotozero.entity.EmissionData;
import org.sustainability.likeherotozero.service.EntityManagerService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object (DAO) responsible for handling operations related to the {@link EmissionData} entity.
 * This class provides methods to retrieve emission data from the database based on various criteria such as ID,
 * country, date, and approval status.
 */
@Named
public class EmissionDataDAO implements Serializable {

    @Inject
    private EntityManagerService entityManagerService;

    /**
     * Retrieves an EmissionData entry by its unique ID.
     *
     * @param id the ID of the EmissionData entry to be retrieved.
     * @return the {@link EmissionData} entity if found, or null if no entry with the specified ID exists.
     */
    public EmissionData findOneById(int id) {
        EntityManager entityManager = entityManagerService.getEntityManager();
        Query query = entityManager.createQuery("SELECT e FROM EmissionData e WHERE id = :id");
        query.setParameter("id", id);

        try {
            return (EmissionData) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Retrieves an EmissionData entry by the country and the reported date.
     *
     * @param country the {@link Country} entity for which the data is reported.
     * @param reportedForDate the date for which the emission data is reported.
     * @return the {@link EmissionData} entity if found, or null if no entry exists for the specified criteria.
     */
    public EmissionData findOneByCountryAndDate(Country country, Date reportedForDate) {
        EntityManager entityManager = entityManagerService.getEntityManager();
        Query query = entityManager.createQuery("SELECT e FROM EmissionData e WHERE country = :country AND reportedFor = :reportedFor");
        query.setParameter("country", country);
        query.setParameter("reportedFor", reportedForDate);

        try {
            return (EmissionData) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Retrieves the most recent EmissionData entry for a specific country.
     * The results are ordered by the reported date in descending order, and the first result is returned.
     *
     * @param country the {@link Country} entity for which the most recent data is requested.
     * @return the newest {@link EmissionData} entity if found, or null if no entries exist for the specified country.
     */
    public EmissionData findNewestByCountry(Country country) {
        EntityManager entityManager = entityManagerService.getEntityManager();
        Query query = entityManager.createQuery("SELECT e FROM EmissionData e WHERE country = :country ORDER BY reportedFor DESC");
        query.setParameter("country", country);
        query.setMaxResults(1);

        try {
            return (EmissionData) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Retrieves a list of all unapproved EmissionData entries.
     * The results are ordered by the creation date in descending order.
     *
     * @return a {@link List} of unapproved {@link EmissionData} entities.
     */
    public List<EmissionData> findAllUnapproved() {
        EntityManager entityManager = entityManagerService.getEntityManager();
        Query query = entityManager.createQuery("SELECT e FROM EmissionData e WHERE e.approved = false ORDER BY e.createdAt DESC");

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }
}
