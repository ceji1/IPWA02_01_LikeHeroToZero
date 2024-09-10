package org.sustainability.likeherotozero.entity;

import jakarta.persistence.*;

/**
 * Entity class representing a user role in the application.
 * This class stores information about a role, including its unique name.
 * User roles are used to define access rights and permissions for users.
 */
@Entity
public class UserRole {

    /**
     * The unique identifier for the user role.
     * This ID is auto-generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The name of the role.
     * This field is required and must be unique.
     */
    @Column(nullable = false, unique = true)
    private String role;

    /**
     * Default constructor for JPA.
     */
    public UserRole() {
    }

    /**
     * Gets the unique identifier of the role.
     *
     * @return the ID of the role.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the role.
     *
     * @param id the ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the role.
     *
     * @return the name of the role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the name of the role.
     *
     * @param role the name to set.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Checks if this UserRole is equal to another object.
     * Two UserRole objects are considered equal if they have the same ID and role name.
     *
     * @param o the object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof UserRole) {
            UserRole b = (UserRole) o;
            return b.getId() == this.id && b.getRole().equals(this.role);
        }
        return false;
    }
}