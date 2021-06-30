package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.model.Tag;
import com.epam.esm.model.Tag_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
    public List<Tag> getTagListByGiftId(int giftId) {
        final String GET_TAG_LIST_BY_GIFT_ID_QUERY = "getTagListByGiftId";
        final String GIFT_ID_PARAMETER = "giftId";
        Query namedQuery = entityManager.createNamedQuery(GET_TAG_LIST_BY_GIFT_ID_QUERY);
        namedQuery.setParameter(GIFT_ID_PARAMETER, giftId);

        return namedQuery.getResultList();
    }

    /**
     * Connects to database and returns all Tags.
     *
     * @return List of all {@link Tag} entities from database.
     */
    @Override
    public List<Tag> getAllTags() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }

}
