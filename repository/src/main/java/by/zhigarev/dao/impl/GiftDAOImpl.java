package by.zhigarev.dao.impl;

import by.zhigarev.dao.GiftDAO;
import by.zhigarev.dao.util.GiftMapper;
import by.zhigarev.model.Gift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GiftDAOImpl implements GiftDAO {
    private JdbcTemplate template;
    private GiftMapper mapper;

    private static final String SQL_GET_GIFT_BY_ID = "select * from gift where id = ?";
    private static final String SQL_DELETE_GIFT_BY_ID = "delete from gift where id = ?";
    private static final String SQL_DELETE_GIFT_TAG_BY_TAG_ID = "delete from gift_tag where gift_id = ?";
    private static final String SQL_GET_GIFTS = "select * from gift";
    private static final String SQL_INSERT_GIFT_TAG = "insert into gift_tag(gift_id,tag_id) values(?,?)";
    private static final String SQL_INSERT_GIFT = "insert into gift(name, description, price, duration,create_date," +
            "last_update_date) values (?,?,?,?,?,?)";


    @Autowired
    public GiftDAOImpl(DataSource dataSource, GiftMapper mapper) {
        this.template = new JdbcTemplate(dataSource);
        this.mapper = mapper;

    }

    @Override
    public Gift createGift(Gift gift) {
        final Timestamp CURRENT_TIMESTAMP = Timestamp.from(Instant.now());
        final int NAME_INDEX = 1;
        final int DESCRIPTION_INDEX = 2;
        final int PRICE_INDEX = 3;
        final int DURATION_INDEX = 4;
        final int CREATE_DATE_INDEX = 5;
        final int LAST_UPDATE_DATE_INDEX = 6;

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

        int id = keyHolder.getKey().intValue();
        return getGiftById(id).get();
    }


    @Override
    public void deleteGiftById(int id) {
        template.update(SQL_DELETE_GIFT_BY_ID,id);
    }

    @Override
    public Gift updateGiftById(String updateSql, int id) {
        template.update(updateSql,id);
        return getGiftById(id).get();
    }

    @Override
    public Optional<Gift> getGiftById(int id) {
        return Optional.ofNullable(template.queryForObject(SQL_GET_GIFT_BY_ID, mapper, id));
    }

    @Override
    public List<Gift> getGifts() {
        return template.query(SQL_GET_GIFTS,mapper);
    }


    @Override
    public List<Gift> getGiftsByParams(String getSql) {
        return template.query(getSql,mapper);
    }

    @Override
    public void createGiftTag(int giftId, int tagId) {
        template.update(SQL_INSERT_GIFT_TAG,giftId,tagId);
    }

    @Override
    public void deleteGiftTagByGiftId(int id) {
        template.update(SQL_DELETE_GIFT_TAG_BY_TAG_ID,id);
    }

}

