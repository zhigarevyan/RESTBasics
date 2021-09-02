package com.epam.esm.util.specification;

import com.epam.esm.model.*;
import com.epam.esm.util.*;
import org.springframework.stereotype.Component;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.persistence.criteria.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public final class GiftSpecification {

    private static final String ANY_SYMBOL = "%";

    /**
     * Method to provide {@link Specification<Tag>} to find Tags by Gift id.
     *
     * @param orderId is gift id.
     * @return {@link Specification<Tag>} object to find most widely used tag from user.
     */
    public static Specification<Gift> giftListByOrderId(int orderId) {
        return (Specification<Gift>) (root, query, cb) -> {

            Root<Order> giftRoot = query.from(Order.class);
            ListJoin<Order, Gift> orderList = giftRoot.joinList(Gift_.TAG_LIST);

            return cb.equal(orderList.get(Order_.ID), orderId);
        };
    }

    /**
     * Returns Specification of {@link Gift} objects filtered by received parameters.
     *
     * @param giftQueryParameter {@link GiftQueryParameters} object with required params
     * @return {@link Specification<Gift>} object to find most widely used tag from user.
     */
    public static Specification<Gift> findByQueryParameter(
            GiftQueryParameters giftQueryParameter) {

        return (Specification<Gift>) (root, query, cb) -> {

            List<Predicate> predicateList = new ArrayList<>();

            String name = giftQueryParameter.getName();

            if (name != null) {
                Predicate predicate = cb.like(root.get(Gift_.NAME),
                        ANY_SYMBOL + name + ANY_SYMBOL);
                predicateList.add(predicate);
            }

            String description = giftQueryParameter.getDescription();
            if (description != null) {
                Predicate predicate = cb.like(root.get(Gift_.DESCRIPTION),
                        ANY_SYMBOL + description + ANY_SYMBOL);
                predicateList.add(predicate);
            }

            List<Filter> priceFilterList = giftQueryParameter.getPrice();
            if (priceFilterList != null) {
                Path<Gift> path = root.get(Gift_.PRICE);

                for (Filter filter : priceFilterList) {
                    Predicate predicate = filterToPredicate(cb, path, filter);
                    predicateList.add(predicate);
                }
            }

            List<Filter> durationFilterList = giftQueryParameter.getDuration();
            if (durationFilterList != null) {
                Path<Gift> path = root.get(Gift_.DURATION);

                for (Filter filter : durationFilterList) {
                    Predicate predicate = filterToPredicate(cb, path, filter);
                    predicateList.add(predicate);
                }
            }

            List<String> tagNameList = giftQueryParameter.getTagName();
            if (tagNameList != null) {
                List<String> tagNamesWithoutDuplicates = tagNameList.stream().distinct().collect(Collectors.toList());

                if (tagNameList.size() != tagNamesWithoutDuplicates.size()) {
                    return cb.disjunction();
                }

                ListJoin<Tag, Gift> tagJoin = root.joinList(Gift_.TAG_LIST);
                Path<Tag> tagList = tagJoin.get(Tag_.NAME);

                tagNameList.forEach(tag -> predicateList.add(cb.equal(tagList, tag)));
            }

            String columnNameToSort = null;

            Sort sort = giftQueryParameter.getSort();

            if (sort != null) {
                SortBy sortBy = sort.getSortBy();
                SortOrientation sortOrientation = sort.getSortOrientation();

                switch (sortBy) {
                    case NAME:
                        columnNameToSort = Gift_.NAME;
                        break;
                    case DATE:
                        columnNameToSort = Gift_.LAST_UPDATE_DATE;
                        break;
                }

                javax.persistence.criteria.Order order = null;

                switch (sortOrientation) {
                    case ASC:
                        order = cb.asc(root.get(columnNameToSort));
                        break;

                    case DESC:
                        order = cb.desc(root.get(columnNameToSort));
                        break;
                }
                query.orderBy(order);
            }

            return cb.and(predicateList.toArray(new Predicate[0]));
        };
    }

    private static Predicate filterToPredicate(
            CriteriaBuilder criteriaBuilder, Path expression, Filter filter) {

        FilterType filterType = filter.getType();
        int value = filter.getValue();

        switch (filterType) {
            case GT: {
                return criteriaBuilder.gt(expression, value);
            }
            case GTE: {
                return criteriaBuilder.ge(expression, value);
            }
            case LT: {
                return criteriaBuilder.lt(expression, value);
            }
            case LTE: {
                return criteriaBuilder.le(expression, value);
            }
            default: {
                return criteriaBuilder.equal(expression, value);
            }
        }
    }
}