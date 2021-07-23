package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDAO;
import com.epam.esm.model.Order;
import com.epam.esm.model.Order_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
@Repository
public class OrderDAOImpl implements OrderDAO {
    /**
     * An object of {@link EntityManager} that is being injected.
     */
    @PersistenceContext
    private EntityManager entityManager;
    /**
     * Connects to database and returns User by ID.
     *
     * @param id is Order ID value.
     * @return Optional of {@link Order} entity from database.
     */
    @Override
    public Optional<Order> getOrderById(int id) {
         return Optional.ofNullable(entityManager.find(Order.class,id));
    }
    /**
     * Connects to database and returns all Users.
     *
     * @param userId is User ID value.
     * @return List of all {@link Order} entities from database.
     */
    @Override
    public List<Order> getOrdersByUserId(int userId, int page, int size) {
        final int PAGE_OFFSET = 1;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root).where(criteriaBuilder.equal(root.get(Order_.USER), userId));

        int itemsOffset = (page - PAGE_OFFSET) * size;
        return entityManager.createQuery(query).setFirstResult(itemsOffset).setMaxResults(size).getResultList();
    }
    /**
     * Connects to database and returns all Orders.
     *
     * @return List of all {@link Order} entities from database.
     */
    @Override
    public List<Order> getAllOrders(int page, int size) {
        final int PAGE_OFFSET = 1;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root);
        int itemsOffset = (page - PAGE_OFFSET) * size;
        return entityManager.createQuery(query).setFirstResult(itemsOffset).setMaxResults(size).getResultList();
    }
    /**
     * Connects to database and add an new Order.
     *
     * @param order {@link Order} is entity with data for creating Order.
     * @return Created {@link Order} entity from database
     */
    @Override
    public Order createOrder(Order order) {
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        order.setDate(currentLocalDateTime.toInstant(ZoneOffset.UTC));
        entityManager.persist(order);

        return order;
    }
}
