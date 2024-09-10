package org.sustainability.likeherotozero.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.Serializable;

/**
 * Service class responsible for managing the {@link EntityManager} instances used in the application.
 * This class provides a centralized way to obtain an {@link EntityManager} for database operations.
 * It is scoped to the application, meaning it will be available throughout the application's lifecycle.
 */
@ApplicationScoped
public class EntityManagerService implements Serializable {

    private final EntityManagerFactory emf;

    private EntityManager em;

    /**
     * Initializes the EntityManagerService by creating an {@link EntityManagerFactory} with the "default" persistence unit.
     */
    public EntityManagerService() {
        this.emf = Persistence.createEntityManagerFactory("default");
    }

    /**
     * Provides an {@link EntityManager} for interacting with the database.
     * If the current EntityManager is null or closed, a new instance is created.
     *
     * @return the current {@link EntityManager} instance.
     */
    public EntityManager getEntityManager() {
        if (null == em || !em.isOpen()) {
            this.em = emf.createEntityManager();
        }

        return this.em;
    }
}