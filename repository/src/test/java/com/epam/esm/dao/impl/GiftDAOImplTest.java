package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftDAO;
import com.epam.esm.model.Gift;
import com.epam.esm.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(GiftDAOTestResolver.class)
class GiftDAOImplTest {
    private final EmbeddedDatabase database;
    private GiftDAO giftDAO;
    private Gift giftCertificate;
    private final GiftMapper giftMapper;

    private static final String TEST_NAME = "Test Gift";
    private static final String TEST_QUERY_NAME = "Gift";
    private static final String TEST_QUERY_DESCRIPTION = "First";
    private static final String SECOND_TAG_NAME = "Second Tag";
    private static final String SECOND_GIFT_NAME = "Second Gift";
    private static final int TEST_ID = 1;
    private static final int TEST_NOT_EXISTING_ID = 20;
    private static final String TEST_DESCRIPTION = "Test certificate";
    private static final int TEST_PRICE = 10;
    private static final int TEST_DURATION = 10;

    public GiftDAOImplTest(EmbeddedDatabase database, GiftMapper giftMapper) {
        this.database = database;
        this.giftMapper = giftMapper;
    }

    @BeforeEach
    void setUp() {

        giftDAO = new GiftDAOImpl(database, giftMapper);
        giftCertificate = new Gift();
        giftCertificate.setName(TEST_NAME);
        giftCertificate.setDescription(TEST_DESCRIPTION);
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
        assertNotNull(gift);
        assertEquals(TEST_NAME, gift.getName());
        assertEquals(TEST_DESCRIPTION, gift.getDescription());
        assertEquals(TEST_PRICE, gift.getPrice());
        assertEquals(TEST_DURATION, gift.getDuration());
    }

    @Test
    void deleteGiftById() {
        giftDAO.deleteGiftById(TEST_ID);
        Optional<Gift> gift = giftDAO.getGiftById(TEST_ID);
        assertFalse(gift.isPresent());
    }

    @Test
    void updateGiftById() {
        Gift gift = giftDAO.getGiftById(TEST_ID).get();

        Gift giftForUpdate = new Gift();

        giftForUpdate.setName(TEST_NAME);
        giftForUpdate.setDescription(TEST_DESCRIPTION);

        String updateSql = GiftSqlBuilder.getUpdateSql(giftForUpdate);

        Gift updatedGift = giftDAO.updateGiftById(updateSql, TEST_ID);
        assertEquals(TEST_NAME,updatedGift.getName());
        assertEquals(TEST_DESCRIPTION,updatedGift.getDescription());
        assertNotEquals(gift.getLastUpdateDate(),updatedGift.getLastUpdateDate());

    }

    @Test
    void getGiftById() {
        Optional<Gift> existing = giftDAO.getGiftById(TEST_ID);
        Optional<Gift> notExisting = giftDAO.getGiftById(TEST_NOT_EXISTING_ID);
        assertTrue(existing.isPresent());
        assertFalse(notExisting.isPresent());
    }

    @Test
    void getGifts() {
        final int COUNT = 2;
        List<Gift> gifts = giftDAO.getGifts();
        assertNotNull(gifts);
        assertEquals(COUNT, gifts.size());
    }

    @Test
    void getGiftsByParams() {
        final int COUNT_1 = 2;
        final int COUNT_2 = 1;

        GiftSQLQueryParameters paramsTag = new GiftSQLQueryParameters();
        paramsTag.setTagName(SECOND_TAG_NAME);

        String getWithParamsSQL1 = GiftSqlBuilder.getGetWithParamsSQL(paramsTag);
        List<Gift> giftsByParams1 = giftDAO.getGiftsByParams(getWithParamsSQL1);
        assertEquals(COUNT_1,giftsByParams1.size());

        GiftSQLQueryParameters paramsNameAndDescription = new GiftSQLQueryParameters();
        paramsNameAndDescription.setName(TEST_QUERY_NAME);
        paramsNameAndDescription.setDescription(TEST_QUERY_DESCRIPTION);

        String getWithParamsSQL2 = GiftSqlBuilder.getGetWithParamsSQL(paramsNameAndDescription);
        List<Gift> giftsByParams2 = giftDAO.getGiftsByParams(getWithParamsSQL2);
        assertEquals(COUNT_2,giftsByParams2.size());

        GiftSQLQueryParameters paramsSortByAndOrientation = new GiftSQLQueryParameters();
        paramsSortByAndOrientation.setSortBy(SortBy.NAME);
        paramsSortByAndOrientation.setSortOrientation(SortOrientation.DESC);

        String getWithParamsSQL3 = GiftSqlBuilder.getGetWithParamsSQL(paramsSortByAndOrientation);
        List<Gift> giftsByParams3 = giftDAO.getGiftsByParams(getWithParamsSQL3);
        Gift gift = giftsByParams3.get(0);
        assertEquals(SECOND_GIFT_NAME,gift.getName());


    }


}