package by.zhigarev.util;

import by.zhigarev.model.Gift;

import java.sql.Timestamp;
import java.time.Instant;

import static java.lang.String.format;
import static org.springframework.util.StringUtils.hasLength;

public class GiftSqlBuilder {


    public static String getUpdateSql(Gift gift) {
        final String SQL_UPDATE = "update gift set ";
        final String SQL_SET_NAME = "name = '%s' ";
        final String SQL_SET_DESCRIPTION = "description = '%s' ";
        final String SQL_SET_PRICE = "price = %d ";
        final String SQL_SET_DURATION = "duration = %d ";
        final String SQL_SET_LAST_UPDATE_DATE = "last_update_date = '%s' ";
        final String SQL_SET_COMMA = " , ";
        final String SQL_SET_WHERE_ID = "where id = ? ";

        StringBuilder updateSQl = new StringBuilder(SQL_UPDATE);

        updateSQl.append(format(SQL_SET_LAST_UPDATE_DATE, Timestamp.from(Instant.now())));
        if (hasLength(gift.getName())) {
            updateSQl.append(SQL_SET_COMMA);
            updateSQl.append(format(SQL_SET_NAME, gift.getName()));
        }
        if (hasLength(gift.getDescription())) {
            updateSQl.append(SQL_SET_COMMA);
            updateSQl.append(format(SQL_SET_DESCRIPTION, gift.getDescription()));
        }
        if (gift.getPrice() != null) {
            updateSQl.append(SQL_SET_COMMA);
            updateSQl.append(format(SQL_SET_PRICE, gift.getPrice()));
        }
        if (gift.getDuration() != null) {
            updateSQl.append(SQL_SET_COMMA);
            updateSQl.append(format(SQL_SET_DURATION, gift.getDuration()));
        }
        updateSQl.append(SQL_SET_WHERE_ID);
        return updateSQl.toString();
    }

    public static String getGetWithParamsSQL(GiftSQLQueryParameters params) {
        final String SQL_GET_WITH_PARAMS = "select g.id, g.name, g.description, g.price," +
                "g.duration, g.create_date, g.last_update_date from gift g ";
        final String SQL_TAG_NAME = "where t.name = '%s' ";
        final String SQL_NAME = "g.name like '%s' ";
        final String SQL_DESCRIPTION = "g.description like '%s' ";
        final String SQL_AND = "AND ";
        final String SQL_ASC = "ASC ";
        final String SQL_DESC = "DESC ";
        final String SQL_PERCENT = "%";
        final String SQL_SORT_BY_DATE = "ORDER BY g.create_date ";
        final String SQL_SORT_BY_NAME = "ORDER BY g.name ";
        final String SQL_WHERE = "WHERE ";
        final String SQL_JOIN = "INNER JOIN gift_tag gt ON g.id = gt.gift_id " +
                "INNER JOIN tag t ON gt.tag_id = t.id ";
        boolean whereIsUsed = false;
        StringBuilder getWithParamsSQL = new StringBuilder(SQL_GET_WITH_PARAMS);
        String tagName = params.getTagName();
        if (tagName != null) {
            whereIsUsed = true;
            getWithParamsSQL.append(SQL_JOIN);
            getWithParamsSQL.append(format(SQL_TAG_NAME, tagName));
        }
        String name = params.getName();
        if (name != null) {
            if (!whereIsUsed) {
                whereIsUsed=true;
                getWithParamsSQL.append(SQL_WHERE);
            } else getWithParamsSQL.append(SQL_AND);
            getWithParamsSQL.append(format(SQL_NAME, SQL_PERCENT+name+SQL_PERCENT));
        }
        String description = params.getDescription();
        if (description != null) {
            if (!whereIsUsed) getWithParamsSQL.append(SQL_WHERE);
            else getWithParamsSQL.append(SQL_AND);
            getWithParamsSQL.append(format(SQL_DESCRIPTION, SQL_PERCENT+description+SQL_PERCENT));
        }
        SortBy sortBy = params.getSortBy();
        if (sortBy != null) {
            if (sortBy.equals(SortBy.DATE)) getWithParamsSQL.append(SQL_SORT_BY_DATE);
            else getWithParamsSQL.append(SQL_SORT_BY_NAME);
        }
        SortOrientation sortOrientation = params.getSortOrientation();
        if (sortOrientation != null) {
            if (sortOrientation.equals(SortOrientation.ASC)) getWithParamsSQL.append(SQL_ASC);
            else getWithParamsSQL.append(SQL_DESC);
        }

        return getWithParamsSQL.toString();
    }

    public static void main(String[] args) {
        Gift dto = new Gift();
        dto.setName("aaa");
        dto.setPrice(12);
        dto.setId(1);
        dto.setDescription("sdfsdf");
        String updateSql = new GiftSqlBuilder().getUpdateSql(dto);
        System.out.println(updateSql);
        GiftSQLQueryParameters giftSQLQueryParameters = new GiftSQLQueryParameters();
        //giftSQLQueryParameters.setName("1");
        giftSQLQueryParameters.setDescription("1");
        giftSQLQueryParameters.setTagName("tag");
        giftSQLQueryParameters.setSortOrientation(SortOrientation.ASC);
        giftSQLQueryParameters.setSortBy(SortBy.DATE);
        String getWithParamsSQL = new GiftSqlBuilder().getGetWithParamsSQL(giftSQLQueryParameters);
        System.out.println(getWithParamsSQL);
    }

}
