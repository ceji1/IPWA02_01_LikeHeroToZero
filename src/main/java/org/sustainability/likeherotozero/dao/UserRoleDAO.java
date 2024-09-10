package org.sustainability.likeherotozero.dao;
import org.sustainability.likeherotozero.entity.UserRole;
import org.sustainability.likeherotozero.service.EntityManagerService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.io.Serializable;

/**
 * Data Access Object (DAO) responsible for handling operations related to the {@link UserRole} entity.
 * This class provides methods to retrieve user roles from the database based on their role name.
 */
@Named
public class UserRoleDAO implements Serializable {

    @Inject
    private EntityManagerService entityManagerService;

    /**
     * Retrieves a UserRole entity by its role name.
     *
     * @param roleName the name of the role to be retrieved.
     * @return the {@link UserRole} entity if found, or null if no role with the specified name exists.
     */
    public UserRole findOneByRoleName(String roleName) {
        EntityManager entityManager = entityManagerService.getEntityManager();
        Query query = entityManager.createQuery("SELECT u FROM UserRole u WHERE role = :role");
        query.setParameter("role", roleName);

        try {
            return (UserRole) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}