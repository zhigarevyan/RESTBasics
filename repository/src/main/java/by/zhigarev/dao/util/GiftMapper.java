package by.zhigarev.dao.util;

import by.zhigarev.model.Gift;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GiftMapper implements RowMapper<Gift> {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";


    @Override
    public Gift mapRow(ResultSet resultSet, int i) throws SQLException {

        Gift gift = new Gift();
        gift.setId(resultSet.getInt(ID));
        gift.setName(resultSet.getString(NAME));
        gift.setDescription(resultSet.getString(DESCRIPTION));
        gift.setPrice(resultSet.getInt(PRICE));
        gift.setDuration(resultSet.getInt(DURATION));
        gift.setCreateDate(resultSet.getDate(CREATE_DATE).toLocalDate());
        gift.setLastUpdateDate(resultSet.getDate(LAST_UPDATE_DATE).toLocalDate());
        return gift;
    }
}
