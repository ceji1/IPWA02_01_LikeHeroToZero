package org.sustainability.likeherotozero.dao;

import org.sustainability.likeherotozero.entity.User;
import org.sustainability.likeherotozero.service.EntityManagerService;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.io.Serializable;

/**
 * Data Access Object (DAO) responsible for handling operations related to the {@link User} entity.
 * This class provides methods to retrieve users from the database based on their username.
 */
public class UserDAO implements Serializable {

    @Inject
    private EntityManagerService entityManagerService;

    /**
     * Retrieves a User entity by its username.
     *
     * @param username the username of the user to be retrieved.
     * @return the {@link User} entity if found, or null if no user with the specified username exists.
     */
    public User findOneByUsername(String username) {
        EntityManager em = entityManagerService.getEntityManager();
        Query query = em.createQuery("SELECT u FROM User u WHERE username = :username");
        query.setParameter("username", username);

        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}