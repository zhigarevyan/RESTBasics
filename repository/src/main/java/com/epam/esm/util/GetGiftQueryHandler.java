package com.epam.esm.util;

import com.epam.esm.model.Gift;
import com.epam.esm.model.Gift_;
import com.epam.esm.model.Tag;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetGiftQueryHandler {

    /**
     * Symbol for matching any symbol at LIKE sql requests
     */
    private static final String ANY_SYMBOL = "%";

    private static final int PAGE_NUMBER_OFFSET = 1;

    private static final String TAG_NAME_LIST_PARAMETER_NAME = "names";
    /**
     * JPQL query to get Tag by name
     */
    private static final String SELECT_TAG_BY_NAME_JPQL =
            "from Tag tag where tag.name in (:" + TAG_NAME_LIST_PARAMETER_NAME + ")";

    private GetGiftQueryHandler() {
    }

    /**
     * Returns List of {@link Gift} objects filtered by received parameters.
     *
     * @param entityManager      {@link EntityManager} object
     * @param giftQueryParameter {@link GiftQueryParameters} object with required params
     * @return List of {@link Gift} objects
     */
    public List<Gift> handle(EntityManager entityManager,
                             GiftQueryParameters giftQueryParameter) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Gift> criteriaQuery = criteriaBuilder.createQuery(Gift.class);
        Root<Gift> giftRoot = criteriaQuery.from(Gift.class);
        List<Predicate> predicateList = new ArrayList<>();

        String name = giftQueryParameter.getName();

        if (name != null) {
            Predicate predicate = criteriaBuilder.like(giftRoot.get(Gift_.NAME),
                    ANY_SYMBOL + name + ANY_SYMBOL);
            predicateList.add(predicate);
        }

        String description = giftQueryParameter.getDescription();
        if (description != null) {
            Predicate predicate = criteriaBuilder.like(giftRoot.get(Gift_.DESCRIPTION),
                    ANY_SYMBOL + description + ANY_SYMBOL);
            predicateList.add(predicate);
        }

        List<Filter> priceFilterList = giftQueryParameter.getPrice();
        if (priceFilterList != null) {
            Path<Gift> path = giftRoot.get(Gift_.PRICE);

            for (Filter filter : priceFilterList) {
                Predicate predicate = filterToPredicate(criteriaBuilder, path, filter);
                predicateList.add(predicate);
            }
        }

        List<Filter> durationFilterList = giftQueryParameter.getDuration();
        if (durationFilterList != null) {
            Path<Gift> path = giftRoot.get(Gift_.DURATION);

            for (Filter filter : durationFilterList) {
                Predicate predicate = filterToPredicate(criteriaBuilder, path, filter);
                predicateList.add(predicate);
            }
        }

        List<String> tagNameList = giftQueryParameter.getTagName();
        if (tagNameList != null) {
            List<String> tagNamesWithoutDuplicates = tagNameList.stream().distinct().collect(Collectors.toList());

            List<Tag> tags = entityManager.createQuery(SELECT_TAG_BY_NAME_JPQL, Tag.class)
                    .setParameter(TAG_NAME_LIST_PARAMETER_NAME, tagNamesWithoutDuplicates).getResultList();

            if (tags.size() != tagNamesWithoutDuplicates.size()) {
                return new ArrayList<>();
            }

            tags.forEach(tag -> predicateList.add(criteriaBuilder.isMember(tag, giftRoot.get(Gift_.TAG_LIST))));
        }

        criteriaQuery.select(giftRoot).where(predicateList.toArray(new Predicate[0]));


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

            Order order = null;

            switch (sortOrientation) {
                case ASC:
                    order = criteriaBuilder.asc(giftRoot.get(columnNameToSort));
                    break;

                case DESC:
                    order = criteriaBuilder.desc(giftRoot.get(columnNameToSort));
                    break;
            }
            criteriaQuery.orderBy(order);
        }

        Page pageObject = giftQueryParameter.getPage();
        int size = pageObject.getSize();
        int itemsOffset = (pageObject.getPage() - PAGE_NUMBER_OFFSET) * size;

        List<Gift> resultList = entityManager.createQuery(criteriaQuery).setFirstResult(itemsOffset).setMaxResults(size).getResultList();
        return resultList;
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
