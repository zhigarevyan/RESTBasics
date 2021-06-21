package com.epam.esm.util;

import com.epam.esm.model.Gift;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class is implementation of {@link RowMapper}, that links Gift with ResultSet.
 */
@Component
public class GiftMapper implements RowMapper<Gift> {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";

    /**
     * Links ResultSet with Gift entity
     *
     * @param resultSet is {@link ResultSet} object from JDBC request.
     * @param i         is id of the row.
     * @return {@link Gift} entity from database.
     * @throws SQLException when something goes wrong.
     */
    @Override
    public Gift mapRow(ResultSet resultSet, int i) throws SQLException {

        Gift gift = new Gift();
        gift.setId(resultSet.getInt(ID));
        gift.setName(resultSet.getString(NAME));
        gift.setDescription(resultSet.getString(DESCRIPTION));
        gift.setPrice(resultSet.getInt(PRICE));
        gift.setDuration(resultSet.getInt(DURATION));
        gift.setCreateDate(resultSet.getTimestamp(CREATE_DATE).toInstant());
        gift.setLastUpdateDate(resultSet.getTimestamp(LAST_UPDATE_DATE).toInstant());
        return gift;
    }
}
