package com.epam.esm.service;

import com.epam.esm.dao.GiftDAO;
import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.dto.GiftDTO;
import com.epam.esm.exeption.impl.InvalidDataException;
import com.epam.esm.exeption.impl.NoSuchGiftException;
import com.epam.esm.exeption.impl.NoSuchOrderException;
import com.epam.esm.model.Gift;
import com.epam.esm.model.Tag;
import com.epam.esm.util.GiftEntityDTOMapper;
import com.epam.esm.util.GiftQueryParameters;
import com.epam.esm.util.Page;
import com.epam.esm.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.util.GiftEntityDTOMapper.toDTO;

/**
 * Implementation of {@link GiftService}. Interface provides methods to interact with GiftDAO.
 * Methods should transforms received information into DAO-accepted data and invoke corresponding methods.
 */
@Service
public class GiftService {
    /**
     * Error message when data failed validation
     */
    private static final String MESSAGE_INVALID_DATA_EXCEPTION = "Invalid data exception";
    /**
     * Error message when Gift wasn't found by id
     */
    private static final String MESSAGE_NO_SUCH_GIFT_EXCEPTION = "No such gift with id - %d exception";
    /**
     * Error message when Gift wasn't found by id
     */
    private static final String MESSAGE_NO_SUCH_ORDER_EXCEPTION = "No such order with id - %d exception";
    /**
     * Error code when data failed validation
     */
    private static final String ERROR_CODE_INVALID_DATA = "0101";
    /**
     * Error code when Gift wasn't found by id
     */
    private static final String ERROR_CODE_NO_SUCH_GIFT = "0102404_%d";
    /**
     * Error code when Gift wasn't found by id
     */
    private static final String ERROR_CODE_NO_SUCH_ORDER = "0402404_%d";
    /**
     * An object of {@link GiftDAO}
     */
    private final GiftDAO giftDAO;
    /**
     * An object of {@link TagDAO}
     */
    private final TagDAO tagDAO;
    /**
     * An object of {@link OrderDAO}
     */
    private final OrderDAO orderDAO;

    /**
     * Public constructor that receives giftDAO and tagDAO
     *
     * @param giftDAO is {@link GiftDAO} interface providing DAO methods.
     * @param tagDAO  is {@link TagDAO} interface providing DAO methods.
     */
    @Autowired
    public GiftService(GiftDAO giftDAO, TagDAO tagDAO, OrderDAO orderDAO) {
        this.giftDAO = giftDAO;
        this.tagDAO = tagDAO;
        this.orderDAO = orderDAO;
    }

    /**
     * Invokes DAO method to create Gift with provided data.
     *
     * @param gift is {@link GiftDTO} object with Gift data.
     * @return {@link GiftDTO} object with created data.
     * @throws InvalidDataException if data failed validation
     */
    @Transactional
    public GiftDTO createGift(GiftDTO gift) {
        if (!Validator.isValidGiftDto(gift)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION,
                    ERROR_CODE_INVALID_DATA);
        }
        List<Tag> tagList = createTagsIfNotFoundAndReturnAll(gift.getTagList());
        Gift entity = GiftEntityDTOMapper.toEntity(gift);
        entity.setTagList(tagList);
        Gift createdGift = giftDAO.createGift(entity);
        return toDTO(createdGift);
    }

    /**
     * Invokes DAO method to delete Gift with provided id.
     *
     * @param id is id of Gift to be deleted.
     * @throws NoSuchGiftException if no Gift with provided id founded
     */
    @Transactional
    public void deleteGiftById(int id) {
        if (giftDAO.getGiftById(id).isEmpty()) {
            throw new NoSuchGiftException(String.format(MESSAGE_NO_SUCH_GIFT_EXCEPTION, id),
                    String.format(ERROR_CODE_NO_SUCH_GIFT, id));
        }
        giftDAO.deleteGiftById(id);
    }

    /**
     * Invokes DAO method to update Gift with provided data.
     *
     * @param giftDTO is {@link GiftDTO} object with Gift data.
     * @return {@link GiftDTO} object with updated data.
     * @throws NoSuchGiftException if no Gift with provided id founded
     */
    @Transactional
    public GiftDTO updateGiftById(GiftDTO giftDTO, int id) {
        if (giftDAO.getGiftById(id).isEmpty()) {
            throw new NoSuchGiftException(String.format(MESSAGE_NO_SUCH_GIFT_EXCEPTION, id),
                    String.format(ERROR_CODE_NO_SUCH_GIFT, id));
        }
        List<Tag> tagList = createTagsIfNotFoundAndReturnAll(giftDTO.getTagList());
        Gift entity = GiftEntityDTOMapper.toEntity(giftDTO);
        entity.setTagList(tagList);
        return toDTO(giftDAO.updateGiftById(entity, id));
    }

    /**
     * Invokes DAO method to get Gift with provided id.
     *
     * @param id is id of Gift to be returned.
     * @return {@link GiftDTO} object with Gift data.
     * @throws NoSuchGiftException if no Gift with provided id founded
     */
    @Transactional
    public GiftDTO getGiftById(int id) {
        Optional<Gift> giftById = giftDAO.getGiftById(id);
        if (giftById.isEmpty()) {
            throw new NoSuchGiftException(String.format(MESSAGE_NO_SUCH_GIFT_EXCEPTION, id),
                    String.format(ERROR_CODE_NO_SUCH_GIFT, id));
        }

        return toDTO(giftById.get());
    }

    /**
     * Invokes DAO method to get List of all Gifts from database.
     *
     * @return List of {@link GiftDTO} objects with Gift data.
     */
    public List<GiftDTO> getGifts(Page page) {
        List<Gift> gifts = giftDAO.getGifts(page.getPage(), page.getSize());
        return toDTO(gifts);
    }

    /**
     * Invokes DAO method to get List of all Gifts that matches parameters
     *
     * @param params is {@link GiftQueryParameters} object with requested parameters
     * @return List of {@link GiftDTO} objects with Gift data.
     */
    public List<GiftDTO> getGiftsByParams(GiftQueryParameters params) {
        if (params.isEmpty()) {
            return getGifts(params.getPage());
        }
        List<Gift> giftList = giftDAO.getGiftsByParams(params);

        return toDTO(giftList);
    }

    public List<GiftDTO> getCertificateListByOrderID(int id, Page page) {
        if (orderDAO.getOrderById(id).isEmpty()) {
            throw new NoSuchOrderException(
                    String.format(MESSAGE_NO_SUCH_ORDER_EXCEPTION, id),
                    String.format(ERROR_CODE_NO_SUCH_ORDER, id));
        }
        List<Gift> giftList = giftDAO.getGiftListByOrderID(id,page.getPage(), page.getSize());

        return toDTO(giftList);
    }


    private List<Tag> createTagsIfNotFoundAndReturnAll(List<String> tagNamesList) {
        if (tagNamesList == null) {
            return new ArrayList<>();
        }
        List<Tag> tagList = new ArrayList<>();

        tagNamesList.forEach(tagName -> {
            Optional<Tag> optionalTag = tagDAO.getTagByName(tagName);

            Tag tag = optionalTag.orElseGet(() -> tagDAO.createTag(tagName));
            tagList.add(tag);
        });

        return tagList;
    }
}
