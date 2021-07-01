package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
@Repository
public class UserDAOImpl implements UserDAO {
    /**
     * An object of {@link EntityManager} that is being injected.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Connects to database and returns User by ID.
     *
     * @param id is User ID value.
     * @return Optional of {@link User} entity from database.
     */
    @Override
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    /**
     * Connects to database and returns all Users.
     *
     * @return List of all {@link User} entities from database.
     */
    @Override
    public List<User> getAllUsers() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }
}
