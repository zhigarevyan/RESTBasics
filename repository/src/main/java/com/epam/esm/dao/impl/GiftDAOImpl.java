package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftDAO;
import com.epam.esm.model.Gift;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link Gift}. Provides methods to interact with Tags data from database.
 * Methods connect to database using {@link DataSource} and manipulate with data(save, edit, etc.).
 */
@Repository
public class GiftDAOImpl implements GiftDAO {
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Gift createGift(Gift gift) {
        LocalDateTime currentLocalDateTime = LocalDateTime.now();

        gift.setCreateDate(currentLocalDateTime.toInstant(ZoneOffset.UTC));
        gift.setLastUpdateDate(currentLocalDateTime.toInstant(ZoneOffset.UTC));
        entityManager.persist(gift);
        entityManager.detach(gift);
        return gift;

    }

    @Override
    public void deleteGiftById(int id) {
        Gift gift = entityManager.find(Gift.class, id);
        entityManager.remove(gift);
    }

    @Override
    public void updateGiftById(String updateSQL, int id) {
        final String GIFT_ID_PARAMETER = "giftId";
        Query query = entityManager.createNativeQuery(updateSQL);
        query.setParameter(GIFT_ID_PARAMETER, id);
        query.executeUpdate();
    }

    @Override
    public Optional<Gift> getGiftById(int id) {
        Gift gift = entityManager.find(Gift.class, id);
        return Optional.ofNullable(gift);
    }

    @Override
    public List<Gift> getGifts() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Gift> query = criteriaBuilder.createQuery(Gift.class);
        Root<Gift> root = query.from(Gift.class);
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Gift> getGiftsByParams(String getSql) {
        Query query = entityManager.createQuery(getSql, Gift.class);
        return query.getResultList();
    }

    @Override
    public void createGiftTag(int giftId, int tagId) {
        final String QUERY_CREATE_GIFT_TAG = "createGiftTag";
        final String GIFT_ID_PARAMETER = "giftId";
        final String TAG_ID_PARAMETER = "tagId";
        Query namedQuery = entityManager.createNamedQuery(QUERY_CREATE_GIFT_TAG);
        namedQuery.setParameter(GIFT_ID_PARAMETER, giftId);
        namedQuery.setParameter(TAG_ID_PARAMETER, tagId);
        namedQuery.executeUpdate();
    }

    @Override
    public void deleteGiftTagByGiftId(int id) {
        final String QUERY_DELETE_GIFT_TAG = "deleteGiftTagByGiftId";
        final String GIFT_ID_PARAMETER = "giftId";
        Query namedQuery = entityManager.createNamedQuery(QUERY_DELETE_GIFT_TAG);
        namedQuery.setParameter(GIFT_ID_PARAMETER, id);
        namedQuery.executeUpdate();

    }
}

