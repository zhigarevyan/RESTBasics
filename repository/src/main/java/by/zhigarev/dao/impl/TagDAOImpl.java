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

/**
 * Implementation of {@link TagDAO}. Provides methods to interact with Tags data from database.
 * Methods connect to database using {@link DataSource} and manipulate with data(save, edit, etc.).
 */
@Repository
public class TagDAOImpl implements TagDAO {
    /**
     * An object of {@link JdbcTemplate}
     */
    private JdbcTemplate template;
    /**
     * An object of {@link TagMapper}
     */
    private TagMapper mapper;
    /**
     * Query for database to create a tag with provided name
     */
    private static final String SQL_INSERT_TAG = "insert into tag(name) values(?)";
    /**
     * Query for database to get the tag with provided id
     */
    private static final String SQL_GET_TAG_BY_ID = "select * from tag where id = ?";
    /**
     * Query for database to get the tags that linked to a gift with provided name
     */
    private static final String SQL_GET_TAG_BY_NAME = "select * from tag where name = ?";
    /**
     * Query for database to get all tags
     */
    private static final String SQL_GET_ALL_TAGS = "select * from tag";
    /**
     * Query for database to delete a tag with provided id
     */
    private static final String SQL_DELETE_TAG_BY_ID = "delete from tag where id = ?";
    /**
     * Query for database to get the tags that linked to a gift with provided id
     */
    private static final String SQL_GET_TAG_BY_GIFT_ID = "select * from tag t join gift_tag gt on t.id = gt.tag_id " +
            "where gt.gift_id = ?";


    /**
     * Constructor that requires dataSource
     *
     * @param dataSource is {@link DataSource} object that manages connections
     * @param mapper     is {@link TagMapper} object that map entities
     */
    @Autowired
    public TagDAOImpl(DataSource dataSource, TagMapper mapper) {
        this.template = new JdbcTemplate(dataSource);
        this.mapper = mapper;
    }

    /**
     * Connects to database and add an new Tag.
     *
     * @param name is Tag name value
     * @return Created {@link Tag} entity from database
     */
    @Override
    public Tag createTag(String name) {
        template.update(SQL_INSERT_TAG, name);
        return getTagByName(name).get();
    }

    /**
     * Connects to database and deletes Tag with provided ID
     *
     * @param id is Tag ID value.
     */
    @Override
    public void deleteTagById(int id) {
        template.update(SQL_DELETE_TAG_BY_ID, id);
    }

    /**
     * Connects to database and returns Tag by ID.
     *
     * @param id is Tag ID value.
     * @return Optional of {@link Tag} entity from database.
     */
    @Override
    public Optional<Tag> getTagById(int id) {
        try {
            return Optional.ofNullable(template.queryForObject(SQL_GET_TAG_BY_ID, mapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Connects to database and returns Tag by name.
     *
     * @param name is Tag name value.
     * @return Optional of {@link Tag} entity from database.
     */
    @Override
    public Optional<Tag> getTagByName(String name) {
        try {
            return Optional.ofNullable(template.queryForObject(SQL_GET_TAG_BY_NAME, mapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Connects to database and returns list of Tags linked to GiftCertificate in gift_tag table
     *
     * @param giftId is GiftCertificate
     * @return List of matched {@link Tag} entities from database.
     */
    @Override
    public List<Tag> getTagListByGiftId(int giftId) {
        return template.query(SQL_GET_TAG_BY_GIFT_ID, mapper, giftId);

    }

    /**
     * Connects to database and returns all Tags.
     *
     * @return List of all {@link Tag} entities from database.
     */
    @Override
    public List<Tag> getAllTags() {
        return template.query(SQL_GET_ALL_TAGS, mapper);
    }

}
