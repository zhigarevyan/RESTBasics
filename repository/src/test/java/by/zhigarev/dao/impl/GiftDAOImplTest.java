package by.zhigarev.dao.impl;

import by.zhigarev.dao.GiftDAO;
import by.zhigarev.dao.util.GiftMapper;
import by.zhigarev.model.Gift;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import static org.junit.jupiter.api.Assertions.*;

class GiftDAOImplTest {
    private EmbeddedDatabase database;
    private GiftDAO giftDAO;
    private GiftMapper giftMapper = new GiftMapper();
    private Gift giftCertificate;

    private static final String TEST_NAME = "Test Gift";
    private static final String TEST_DESC = "Test certificate";
    private static final int TEST_PRICE = 10;
    private static final int TEST_DURATION = 10;


    @BeforeEach
    void setUp() {
        database = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        giftDAO = new GiftDAOImpl(database, giftMapper);
        giftCertificate = new Gift();
        giftCertificate.setName(TEST_NAME);
        giftCertificate.setDescription(TEST_DESC);
        giftCertificate.setPrice(TEST_PRICE);
        giftCertificate.setDuration(TEST_DURATION);
    }

    @AfterEach
    void tearDown() {
        database.shutdown();
    }

    @Test
    void createGift() {
        Gift gift = giftDAO.createGift(giftCertificate);
        assertEquals(TEST_NAME,gift.getName());
        assertEquals(TEST_DESC,gift.getDescription());
        assertEquals(TEST_PRICE,gift.getPrice());
        assertEquals(TEST_DURATION,gift.getDuration());
    }

    @Test
    void deleteGiftById() {
    }

    @Test
    void updateGiftById() {
    }

    @Test
    void getGiftById() {
    }

    @Test
    void getGifts() {
    }

    @Test
    void getGiftsByParams() {
    }

    @Test
    void createGiftTag() {
    }

    @Test
    void deleteGiftTagByGiftId() {
    }
}