package by.zhigarev.util;

import by.zhigarev.dto.GiftDTO;

import java.sql.Timestamp;
import java.time.Instant;

import static java.lang.String.format;
import static org.springframework.util.StringUtils.hasLength;

public class GiftSqlBuilder {


    public String getUpdateSql(GiftDTO giftDTO) {
        final String SQL_UPDATE = "update gift set ";
        final String SQL_SET_NAME = "name = '%s' ";
        final String SQL_SET_DESCRIPTION = "description = '%s' ";
        final String SQL_SET_PRICE = "price = %d ";
        final String SQL_SET_DURATION = "duration = %d ";
        final String SQL_SET_LAST_UPDATE_DATE = "last_update_date = '%s' ";
        final String SQL_SET_COMMA = " , ";
        final String SQL_SET_WHERE_ID = "where id = %d ";

        StringBuilder updateSQl = new StringBuilder(SQL_UPDATE);

        updateSQl.append(format(SQL_SET_LAST_UPDATE_DATE, Timestamp.from(Instant.now())));
        if (hasLength(giftDTO.getName())) {
            updateSQl.append(SQL_SET_COMMA);
            updateSQl.append(format(SQL_SET_NAME, giftDTO.getName()));
        }
        if (hasLength(giftDTO.getDescription())) {
            updateSQl.append(SQL_SET_COMMA);
            updateSQl.append(format(SQL_SET_DESCRIPTION, giftDTO.getDescription()));
        }
        if (giftDTO.getPrice() != null) {
            updateSQl.append(SQL_SET_COMMA);
            updateSQl.append(format(SQL_SET_PRICE, giftDTO.getPrice()));
        }
        if (giftDTO.getDuration() != null) {
            updateSQl.append(SQL_SET_COMMA);
            updateSQl.append(format(SQL_SET_DURATION, giftDTO.getDuration()));
        }
        updateSQl.append(format(SQL_SET_WHERE_ID, giftDTO.getId()));
        return updateSQl.toString();
    }

    public String getGetWithParamsSQL(GiftSQLQueryParameters params) {
        final String SQL_GET_WITH_PARAMS = "select g.id, g.name, g.description, g.price," +
                "g.duration, g.create_date, g.last_update_date from gift g ";
        final String SQL_TAG_NAME = "where t.name = REGEXP '%S' ";
        final String SQL_NAME = "g.name = REGEXP '%S' ";
        final String SQL_DESCRIPTION = "g.description = REGEXP '%S' ";
        final String SQL_AND = "AND ";
        final String SQL_ASC = "ASC ";
        final String SQL_DESC = "DESC ";
        final String SQL_SORT_BY_DATE = "SORT BY g.create_date ";
        final String SQL_SORT_BY_NAME = "SORT BY g.name ";
        final String SQL_WHERE = "WHERE ";
        final String SQL_JOIN = "INNER JOIN gift_tag gt ON g.id = gt.gift " +
                "INNER JOIN tag t ON gt.tag = t.id ";
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
            getWithParamsSQL.append(format(SQL_NAME, name));
        }
        String description = params.getDescription();
        if (description != null) {
            if (!whereIsUsed) getWithParamsSQL.append(SQL_WHERE);
            else getWithParamsSQL.append(SQL_AND);
            getWithParamsSQL.append(format(SQL_DESCRIPTION, description));
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
        GiftDTO dto = new GiftDTO();
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
