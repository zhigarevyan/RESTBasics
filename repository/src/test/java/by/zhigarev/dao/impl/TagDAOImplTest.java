package by.zhigarev.dao.impl;


import by.zhigarev.dao.TagDAO;
import by.zhigarev.model.Tag;
import by.zhigarev.util.TagDAOTestResolver;
import by.zhigarev.util.TagMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(TagDAOTestResolver.class)
@ComponentScan(basePackages = "by.zhigarev")
class TagDAOImplTest {
    private final EmbeddedDatabase database;
    private TagDAO tagDAO;
    private final TagMapper tagMapper;

    public TagDAOImplTest(EmbeddedDatabase database, TagMapper tagMapper) {
        this.tagMapper = tagMapper;
        this.database = database;
    }


    private static final String TEST_NAME = "Test Name";
    private static final String DB_NAME_FIRST = "First Tag";
    private static final String DB_NAME_SECOND = "Second Tag";
    private static final String DB_NAME_THIRD = "Third Tag";
    private static final String TEST_NEW_NAME = "New Name";
    private static final int TEST_ID_FIRST = 1;
    private static final int TEST_ID_SECOND = 2;
    private static final int TEST_ID_THIRD = 3;

    @BeforeEach
    void setUp() {
/*        database = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();*/
        tagDAO = new TagDAOImpl(database, tagMapper);

    }

    @AfterEach
    void tearDown() {
        database.shutdown();
    }

    @Test
    void createTag() {
        Tag createdTag = tagDAO.createTag(TEST_NAME);
        assertNotNull(createdTag);
        assertEquals(TEST_NAME, createdTag.getName());
    }

    @Test
    void deleteTagById() {
        tagDAO.deleteTagById(TEST_ID_FIRST);
        Optional<Tag> tag = tagDAO.getTagById(TEST_ID_FIRST);
        assertFalse(tag.isPresent());
    }

    @Test
    void updateTagById() {
        Tag updatedTag = tagDAO.updateTagById(TEST_NEW_NAME, TEST_ID_FIRST);
        assertEquals(TEST_NEW_NAME, updatedTag.getName());
    }

    @Test
    void getTagById() {
        Tag tag = tagDAO.getTagById(TEST_ID_FIRST).get();
        assertEquals(DB_NAME_FIRST, tag.getName());
    }

    @Test
    void getTagByName() {
        Tag tag = tagDAO.getTagByName(DB_NAME_FIRST).get();
        assertEquals(TEST_ID_FIRST, tag.getId());
    }

    @Test
    void getTagListByGiftId() {
        List<Tag> tagList = tagDAO.getTagListByGiftId(TEST_ID_FIRST);
        Tag firstTag = tagList.get(0);
        Tag secondTag = tagList.get(1);
        assertEquals(TEST_ID_FIRST, firstTag.getId());
        assertEquals(TEST_ID_SECOND, secondTag.getId());
        assertEquals(DB_NAME_FIRST, firstTag.getName());
        assertEquals(DB_NAME_SECOND, secondTag.getName());
    }

    @Test
    void getAllTags() {
        List<Tag> tagList = tagDAO.getAllTags();
        Tag firstTag = tagList.get(0);
        Tag secondTag = tagList.get(1);
        Tag thirdTag = tagList.get(2);
        assertEquals(TEST_ID_FIRST, firstTag.getId());
        assertEquals(TEST_ID_SECOND, secondTag.getId());
        assertEquals(TEST_ID_THIRD, thirdTag.getId());
        assertEquals(DB_NAME_FIRST, firstTag.getName());
        assertEquals(DB_NAME_SECOND, secondTag.getName());
        assertEquals(DB_NAME_THIRD, thirdTag.getName());

    }
}