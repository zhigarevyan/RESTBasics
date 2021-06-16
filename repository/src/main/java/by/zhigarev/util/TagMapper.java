package by.zhigarev.util;

import by.zhigarev.model.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class is implementation of {@link RowMapper}, that links Tag entity with ResultSet.
 */
@Component
public class TagMapper implements RowMapper<Tag> {
    private static final String ID = "id";
    private static final String NAME = "name";

    /**
     * Links ResultSet with GiftCertificate entity
     *
     * @param resultSet is {@link ResultSet} object from JDBC request.
     * @param i         is id of the row.
     * @return {@link Tag} entity from database.
     * @throws SQLException when something goes wrong.
     */
    @Override
    public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
        Tag tag = new Tag();
        tag.setId(resultSet.getInt(ID));
        tag.setName(resultSet.getString(NAME));
        return tag;
    }
}
