package com.epam.esm.dao;

import com.epam.esm.model.Gift;

import java.util.List;
import java.util.Optional;

/**
 * Interface provides methods to interact with Gift data from database.
 * Methods should connect to database and manipulate with data(save, edit, etc.).
 */
public interface GiftDAO {
    /**
     * Connects to database and add an new Gift.
     *
     * @param gift {@link Gift} gift is entity with data for creating Gift.
     * @return Created {@link Gift} entity from database
     */
    Gift createGift(Gift gift);

    /**
     * Connects to database and deletes Gift with provided ID
     *
     * @param id is Gift ID value.
     */
    void deleteGiftById(int id);

    /**
     * Connects to database and updates Gift.
     *
     * @param updateSQL String object containing SQL string and params for request
     * @param id        is Gift ID value.
     * @return updated {@link Gift} entity
     */
    Gift updateGiftById(String updateSQL, int id);

    /**
     * Connects to database and returns Gift by ID.
     *
     * @param id is Gift ID value.
     * @return Optional of {@link Gift} entity from database.
     */
    Optional<Gift> getGiftById(int id);

    /**
     * Connects to database and returns all Gifts.
     *
     * @return List of all {@link Gift} entities from database.
     */
    List<Gift> getGifts();

    /**
     * Connects to database and returns list of matching Gifts
     *
     * @param getSql String object containing SQL string and params for request
     * @return List of matched {@link Gift} entities from database.
     */
    List<Gift> getGiftsByParams(String getSql);

    /**
     * Connects to database and make record to gift_tag table that Gift with provided giftID have tag with provided
     * tagID
     *
     * @param giftId is Gift ID value.
     * @param tagId  is Tag ID value.
     */
    void createGiftTag(int giftId, int tagId);

    /**
     * Connects to database and delete records from gift_tag table with provided gift ID
     *
     * @param id is Gift ID value.
     */
    void deleteGiftTagByGiftId(int id);

}
