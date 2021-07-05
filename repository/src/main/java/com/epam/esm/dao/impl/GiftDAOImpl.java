package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftDAO;
import com.epam.esm.model.Gift;
import com.epam.esm.model.Order;
import com.epam.esm.model.Order_;
import com.epam.esm.util.GetGiftQueryHandler;
import com.epam.esm.util.GiftFieldUpdater;
import com.epam.esm.util.GiftQueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import java.time.Instant;
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
    /**
     * An object of {@link GetGiftQueryHandler}
     */
    GetGiftQueryHandler handler;

    @Autowired
    public GiftDAOImpl(GetGiftQueryHandler handler) {
        this.handler = handler;
    }

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
    public Gift updateGiftById(Gift gift, int id) {
        Gift oldGift = entityManager.find(Gift.class, id);
        GiftFieldUpdater.update(oldGift, gift);

        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        oldGift.setLastUpdateDate(currentLocalDateTime.toInstant(ZoneOffset.UTC));

        entityManager.merge(oldGift);
        return oldGift;
    }

    @Override
    public Optional<Gift> getGiftById(int id) {
        Gift gift = entityManager.find(Gift.class, id);
        return Optional.ofNullable(gift);
    }

    @Override
    public List<Gift> getGifts(int page, int size) {
        final int PAGE_OFFSET = 1;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Gift> query = criteriaBuilder.createQuery(Gift.class);
        Root<Gift> root = query.from(Gift.class);
        query.select(root);
        int itemsOffset = (page - PAGE_OFFSET) * size;
        return entityManager.createQuery(query).setFirstResult(itemsOffset).setMaxResults(size).getResultList();
    }

    @Override
    public List<Gift> getGiftsByParams(GiftQueryParameters parameters) {
        return handler.handle(entityManager, parameters);
    }

    @Override
    public List<Gift> getGiftCertificateListByOrderID(int id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Gift> giftCertificateQuery = criteriaBuilder.createQuery(Gift.class);

        Root<Order> giftRoot = giftCertificateQuery.from(Order.class);
        ListJoin<Order, Gift> giftCertificateList = giftRoot.joinList(Order_.GIFT_LIST);
        giftCertificateQuery
                .select(giftCertificateList)
                .where(criteriaBuilder.equal(giftRoot.get(Order_.ID), id));

        return entityManager.createQuery(giftCertificateQuery).getResultList();
    }


}

