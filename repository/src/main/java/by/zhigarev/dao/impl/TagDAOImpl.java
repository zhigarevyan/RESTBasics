package by.zhigarev.dao.impl;

import by.zhigarev.dao.TagDAO;
import by.zhigarev.model.Tag;
import by.zhigarev.util.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDAOImpl implements TagDAO {
    private JdbcTemplate template;
    private TagMapper mapper;

    private static final String SQL_INSERT_TAG = "insert into tag(name) values(?)";
    private static final String SQL_GET_TAG_BY_ID = "select * from tag where id = ?";
    private static final String SQL_GET_TAG_BY_NAME = "select * from tag where name = ?";
    private static final String SQL_GET_ALL_TAGS = "select * from tag";
    private static final String SQL_DELETE_TAG_BY_ID = "delete from tag where id = ?";
    private static final String SQL_UPDATE_TAG_BY_ID = "update tag set name = ? where id = ?";
    private static final String SQL_GET_TAG_BY_GIFT_ID = "select * from tag t join gift_tag gt on t.id = gt.tag_id " +
            "where gt.gift_id = ?";


    private static final String MESSAGE_DUPLICATE_TAG = "Duplicate tag";

    @Autowired
    public TagDAOImpl(DataSource dataSource, TagMapper mapper) {
        this.template = new JdbcTemplate(dataSource);
        this.mapper = mapper;
    }

    @Override
    public Tag createTag(String name) {
            template.update(SQL_INSERT_TAG, name);
            return getTagByName(name).get();
    }

    @Override
    public void deleteTagById(int id) {
        template.update(SQL_DELETE_TAG_BY_ID, id);
    }

    @Override
    public Tag updateTagById(String name, int id) {
        template.update(SQL_UPDATE_TAG_BY_ID, name, id);
        return getTagByName(name).get();
    }

    @Override
    public Optional<Tag> getTagById(int id) {
        try {
            return Optional.ofNullable(template.queryForObject(SQL_GET_TAG_BY_ID, mapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Tag> getTagByName(String name) {
        try {
            return Optional.ofNullable(template.queryForObject(SQL_GET_TAG_BY_NAME, mapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> getTagListByGiftId(int giftId) {
        return template.query(SQL_GET_TAG_BY_GIFT_ID, mapper, giftId);

    }

    @Override
    public List<Tag> getAllTags() {
        return template.query(SQL_GET_ALL_TAGS, mapper);
    }

}
