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


    @Override
    public User getUserWithHighestCostOfAllOrders() {
        final String SELECT_USER_WITH_HIGHEST_COST_OF_ALL_ORDERS_JPQL =
                "SELECT orders.user FROM Order orders " +
                        "GROUP BY orders.user ORDER BY SUM(orders.price) DESC";
        return (User) entityManager.createQuery(SELECT_USER_WITH_HIGHEST_COST_OF_ALL_ORDERS_JPQL)
                .setMaxResults(1).getSingleResult();
    }

    /**
     * Connects to database and returns all Users.
     *
     * @return List of all {@link User} entities from database.
     */
    @Override
    public List<User> getAllUsers(int page, int size) {
        final int PAGE_OFFSET = 1;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        int itemsOffset = (page - PAGE_OFFSET) * size;
        return entityManager.createQuery(query).setFirstResult(itemsOffset).setMaxResults(size).getResultList();
    }
}
