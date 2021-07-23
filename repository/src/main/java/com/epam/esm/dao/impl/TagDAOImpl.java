package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.model.Order;
import com.epam.esm.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link TagDAO}. Provides methods to interact with Tags data from database.
 * Methods connect to database using {@link DataSource} and manipulate with data(save, edit, etc.).
 */
@Repository
public class TagDAOImpl implements TagDAO {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Tag createTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        entityManager.persist(tag);
        entityManager.detach(tag);
        return tag;
    }

    /**
     * Connects to database and deletes Tag with provided ID
     *
     * @param id is Tag ID value.
     */
    @Override
    public void deleteTagById(int id) {
        Tag tag = entityManager.find(Tag.class, id);
        entityManager.remove(tag);
    }

    /**
     * Connects to database and returns Tag by ID.
     *
     * @param id is Tag ID value.
     * @return Optional of {@link Tag} entity from database.
     */
    @Override
    public Optional<Tag> getTagById(int id) {
        Tag tag = entityManager.find(Tag.class, id);
        return Optional.ofNullable(tag);
    }

    /**
     * Connects to database and returns Tag by name.
     *
     * @param name is Tag name value.
     * @return Optional of {@link Tag} entity from database.
     */
    @Override
    public Optional<Tag> getTagByName(String name) {
        final String QUERY_GET_TAG_BY_NAME = "getTagByName";
        final String TAG_NAME_PARAMETER = "tagName";
        Optional<Tag> tag = Optional.empty();

        Query namedQuery = entityManager.createNamedQuery(QUERY_GET_TAG_BY_NAME);
        namedQuery.setParameter(TAG_NAME_PARAMETER,name);
        List<Tag> resultList = namedQuery.getResultList();
        if (!resultList.isEmpty()) {
            tag = Optional.of(resultList.get(0));
        }
        return tag;
    }

    /**
     * Connects to database and returns list of Tags linked to GiftCertificate in gift_tag table
     *
     * @param giftId is GiftCertificate
     * @return List of matched {@link Tag} entities from database.
     */
    @Override
    public List<Tag> getTagListByGiftId(int giftId, int page, int size) {
        final int PAGE_OFFSET = 1;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> tagQuery = criteriaBuilder.createQuery(Tag.class);

        Root<Gift> giftRoot = tagQuery.from(Gift.class);
        ListJoin<Gift, Tag> tagList = giftRoot.joinList(Gift_.TAG_LIST);
        tagQuery
                .select(tagList)
                .where(criteriaBuilder.equal(giftRoot.get(Gift_.ID), giftId));
        int itemsOffset = (page - PAGE_OFFSET) * size;
        return entityManager.createQuery(tagQuery).setFirstResult(itemsOffset).setMaxResults(size).getResultList();
    }

    /**
     * Connects to database and returns all Tags.
     *
     * @return List of all {@link Tag} entities from database.
     */
    @Override
    public List<Tag> getAllTags(int page, int size) {
        final int PAGE_OFFSET = 1;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);
        query.select(root);
        int itemsOffset = (page - PAGE_OFFSET) * size;
        return entityManager.createQuery(query).setFirstResult(itemsOffset).setMaxResults(size).getResultList();
    }

    @Override
    public Tag getMostWidelyUsedTagFromUser(int userId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> tagQuery = criteriaBuilder.createQuery(Tag.class);

        Root<User> userRoot = tagQuery.from(User.class);
        ListJoin<User, Order> orderList = userRoot.joinList(User_.ORDER_LIST);
        ListJoin<Order, Gift> giftList = orderList.joinList(Order_.GIFT_LIST);
        ListJoin<Gift, Tag> tagList = giftList.joinList(Gift_.TAG_LIST);

        Expression orderID = tagList.get(Order_.ID);
        tagQuery
                .select(tagList)
                .where(criteriaBuilder.equal(userRoot.get(User_.ID), userId))
                .groupBy(orderID)
                .orderBy(criteriaBuilder.desc(criteriaBuilder.count(orderID)));

        return entityManager.createQuery(tagQuery).setMaxResults(1).getSingleResult();
    }

}
