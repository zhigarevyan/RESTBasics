package by.zhigarev.service;

import by.zhigarev.dto.GiftDTO;
import by.zhigarev.util.GiftSQLQueryParameters;

import java.util.List;

/**
 * Interface provides methods to interact with GiftDAO.
 * Methods should transforms received information into DAO-accepted data and invoke corresponding methods.
 */
public interface GiftService {
    /**
     * Invokes DAO method to create Gift with provided data.
     *
     * @param gift is {@link GiftDTO} object with Gift data.
     * @return {@link GiftDTO} object with created data.
     */
    GiftDTO createGift(GiftDTO gift);

    /**
     * Invokes DAO method to delete Gift with provided id.
     *
     * @param id is id of Gift to be deleted.
     */
    void deleteGiftById(int id);

    /**
     * Invokes DAO method to update Gift with provided data.
     *
     * @param giftDTO is {@link GiftDTO} object with Gift data.
     * @return {@link GiftDTO} object with updated data.
     */
    GiftDTO updateGiftById(GiftDTO giftDTO, int id);

    /**
     * Invokes DAO method to get Gift with provided id.
     *
     * @param id is id of Gift to be returned.
     * @return {@link GiftDTO} object with Gift data.
     */
    GiftDTO getGiftById(int id);

    /**
     * Invokes DAO method to get List of all Gifts from database.
     *
     * @return List of {@link GiftDTO} objects with Gift data.
     */
    List<GiftDTO> getGifts();

    /**
     * Invokes DAO method to get List of all Gifts that matches parameters
     *
     * @param params is {@link GiftSQLQueryParameters} object with requested parameters
     * @return List of {@link GiftDTO} objects with Gift data.
     */
    List<GiftDTO> getGiftsByParams(GiftSQLQueryParameters params);

    /**
     * Invokes DAO method to create GiftTag
     *
     * @param giftId is id of gift
     * @param tagId  is id of tag
     */
    void createGiftTag(int giftId, int tagId);
    /**
     * Invokes DAO method to delete GiftTag
     *
     * @param id is id of gift
     */
    void deleteGiftTagByGiftId(int id);
}
