package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftDAO;
import com.epam.esm.model.Gift;
import com.epam.esm.util.GiftMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link Gift}. Provides methods to interact with Tags data from database.
 * Methods connect to database using {@link DataSource} and manipulate with data(save, edit, etc.).
 */
@Repository
public class GiftDAOImpl implements GiftDAO {
    /**
     * An object of {@link JdbcTemplate}
     */
    private final JdbcTemplate template;
    /**
     * An object of {@link GiftMapper}
     */
    private final GiftMapper mapper;
    /**
     * Query for database to get the Gift with provided id
     */
    private static final String SQL_GET_GIFT_BY_ID = "select * from gift where id = ?";
    /**
     * Query for database to delete a Gift with provided id
     */
    private static final String SQL_DELETE_GIFT_BY_ID = "delete from gift where id = ?";
    /**
     * Query for database to delete records from gift_tag table with provided gift ID
     */
    private static final String SQL_DELETE_GIFT_TAG_BY_GIFT_ID = "delete from gift_tag where gift_id = ?";
    /**
     * Query for database to get all Gifts
     */
    private static final String SQL_GET_GIFTS = "select * from gift";
    /**
     * Query for database to make record to gift_tag table that Gift with provided giftID have tag with provided
     */
    private static final String SQL_INSERT_GIFT_TAG = "insert into gift_tag(gift_id,tag_id) values(?,?)";
    /**
     * Query for database to create a Gift with provided data
     */
    private static final String SQL_INSERT_GIFT = "insert into gift(name, description, price, duration,create_date," +
            "last_update_date) values (?,?,?,?,?,?)";

    /**
     * Constructor that requires dataSource
     *
     * @param dataSource is {@link DataSource} object that manages connections
     * @param mapper     is {@link GiftMapper} object that map entities
     */
    @Autowired
    public GiftDAOImpl(DataSource dataSource, GiftMapper mapper) {
        this.template = new JdbcTemplate(dataSource);
        this.mapper = mapper;

    }

    /**
     * Connects to database and add an new Gift.
     *
     * @param gift {@link Gift} gift is entity with data for creating Gift.
     * @return Created {@link Gift} entity from database
     */
    @Override
    public Gift createGift(Gift gift) {
        final Timestamp CURRENT_TIMESTAMP = Timestamp.from(Instant.now());
        final int NAME_INDEX = 1;
        final int DESCRIPTION_INDEX = 2;
        final int PRICE_INDEX = 3;
        final int DURATION_INDEX = 4;
        final int CREATE_DATE_INDEX = 5;
        final int LAST_UPDATE_DATE_INDEX = 6;
        final String ID_KEY = "id";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SQL_INSERT_GIFT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(NAME_INDEX, gift.getName());
            ps.setString(DESCRIPTION_INDEX, gift.getDescription());
            ps.setInt(PRICE_INDEX, gift.getPrice());
            ps.setInt(DURATION_INDEX, gift.getDuration());
            ps.setTimestamp(CREATE_DATE_INDEX, CURRENT_TIMESTAMP);
            ps.setTimestamp(LAST_UPDATE_DATE_INDEX, CURRENT_TIMESTAMP);
            return ps;
        }, keyHolder);
        int id;
        if (keyHolder.getKeys().size() > 1) {
            id = (int) keyHolder.getKeys().get(ID_KEY);
        } else {
            id = keyHolder.getKey().intValue();
        }
        return getGiftById(id).get();
    }

    /**
     * Connects to database and deletes Gift with provided ID
     *
     * @param id is Gift ID value.
     */
    @Override
    public void deleteGiftById(int id) {
        template.update(SQL_DELETE_GIFT_BY_ID, id);
    }

    /**
     * Connects to database and updates Gift.
     *
     * @param updateSql String object containing SQL string and params for request
     * @param id        is Gift ID value.
     * @return updated {@link Gift} entity
     */
    @Override
    public Gift updateGiftById(String updateSql, int id) {
        template.update(updateSql, id);
        return getGiftById(id).get();
    }

    /**
     * Connects to database and returns Gift by ID.
     *
     * @param id is Gift ID value.
     * @return Optional of {@link Gift} entity from database.
     */
    @Override
    public Optional<Gift> getGiftById(int id) {
        try {
            return Optional.ofNullable(template.queryForObject(SQL_GET_GIFT_BY_ID, mapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Connects to database and returns all Gifts.
     *
     * @return List of all {@link Gift} entities from database.
     */
    @Override
    public List<Gift> getGifts() {
        return template.query(SQL_GET_GIFTS, mapper);
    }

    /**
     * Connects to database and returns list of matching Gifts
     *
     * @param getSql String object containing SQL string and params for request
     * @return List of matched {@link Gift} entities from database.
     */
    @Override
    public List<Gift> getGiftsByParams(String getSql) {
        return template.query(getSql, mapper);
    }

    /**
     * Connects to database and make record to gift_tag table that Gift with provided giftID have tag with provided
     * tagID
     *
     * @param giftId is Gift ID value.
     * @param tagId  is Tag ID value.
     */
    @Override
    public void createGiftTag(int giftId, int tagId) {
        template.update(SQL_INSERT_GIFT_TAG, giftId, tagId);
    }

    /**
     * Connects to database and delete records from gift_tag table with provided gift ID
     *
     * @param id is Gift ID value.
     */
    @Override
    public void deleteGiftTagByGiftId(int id) {
        template.update(SQL_DELETE_GIFT_TAG_BY_GIFT_ID, id);
    }

}

