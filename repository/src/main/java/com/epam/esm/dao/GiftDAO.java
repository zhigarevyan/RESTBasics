package com.epam.esm.dao;

import com.epam.esm.model.Gift;
import com.epam.esm.util.GiftQueryParameters;

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
     * @param gift {@link Gift} new gift object.
     * @param id        is Gift ID value.
     * @return updated {@link Gift} entity
     */
    Gift updateGiftById(Gift gift, int id);

    /**
     * Connects to database and returns Gift by ID.
     *
     * @param id is Gift ID value.
     * @return Optional of {@link Gift} entity from database.
     */
    Optional<Gift> getGiftById(int id);

    /**
     * Connects to database and returns all Gifts.
     * @param page is page number
     * @param size is page size
     * @return List of all {@link Gift} entities from database.
     */
    List<Gift> getGifts(int page, int size);

    /**
     * Connects to database and returns list of matching Gifts
     *
     * @param getSql String object containing SQL string and params for request
     * @return List of matched {@link Gift} entities from database.
     */
    List<Gift> getGiftsByParams(GiftQueryParameters parameters);

    /**
     * Connects to database and returns list of Gifts linked to Order in order_gift table
     *
     * @param id is Order id
     * @return List of matched {@link Gift} entities from database.
     */
    public List<Gift> getGiftCertificateListByOrderID(int id);

}
