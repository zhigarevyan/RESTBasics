package com.epam.esm.util.specification;

import com.epam.esm.model.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Collection;

public final class TagSpecification {

    /**
     * Method to provide {@link Specification<Tag>} to find Tags by Gift id.
     *
     * @param giftID is gift id.
     * @return {@link Specification<Tag>} object to find Tags by Gift id.
     */
    public static Specification<Tag> tagListByGiftID(int giftID) {
        return (Specification<Tag>) (root, query, cb) -> {
            query.distinct(true);
            Root<Tag> tagRoot = root;
            Subquery<Gift> giftSubQuery = query.subquery(Gift.class);
            Root<Gift> giftRoot = giftSubQuery.from(Gift.class);
            Expression<Collection<Tag>> giftTagList = giftRoot.get(Gift_.TAG_LIST);
            giftSubQuery.select(giftRoot);
            giftSubQuery.where(cb.equal(giftRoot.get(Gift_.ID), giftID), cb.isMember(tagRoot, giftTagList));
            return cb.exists(giftSubQuery);
        };
    }
}
