package by.zhigarev.service.impl;

import by.zhigarev.dao.GiftDAO;
import by.zhigarev.dao.TagDAO;
import by.zhigarev.dto.GiftDTO;
import by.zhigarev.exeption.impl.InvalidDataException;
import by.zhigarev.exeption.impl.NoSuchGiftException;
import by.zhigarev.model.Gift;
import by.zhigarev.model.Tag;
import by.zhigarev.service.GiftService;
import by.zhigarev.util.GiftEntityDTOMapper;
import by.zhigarev.util.GiftSQLQueryParameters;
import by.zhigarev.util.GiftSqlBuilder;
import by.zhigarev.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link GiftService}. Interface provides methods to interact with GiftDAO.
 * Methods should transforms received information into DAO-accepted data and invoke corresponding methods.
 */
@Service
public class GiftServiceImpl implements GiftService {
    /**
     * Error message when data failed validation
     */
    private static final String MESSAGE_INVALID_DATA_EXCEPTION = "Invalid data exception";
    /**
     * Error message when Gift wasn't found by id
     */
    private static final String MESSAGE_NO_SUCH_GIFT_EXCEPTION = "No such gift with id - %d exception";
    /**
     * Error code when data failed validation
     */
    private static final String ERROR_CODE_INVALID_DATA = "0101";
    /**
     * Error code when Gift wasn't found by id
     */
    private static final String ERROR_CODE_NO_SUCH_GIFT = "0102404_%d";
    /**
     * An object of {@link GiftDAO}
     */
    private final GiftDAO giftDAO;
    /**
     * An object of {@link TagDAO}
     */
    private final TagDAO tagDAO;

    /**
     * Public constructor that receives giftDAO and tagDAO
     *
     * @param giftDAO is {@link GiftDAO} interface providing DAO methods.
     * @param tagDAO             is {@link TagDAO} interface providing DAO methods.
     */
    @Autowired
    public GiftServiceImpl(GiftDAO giftDAO, TagDAO tagDAO) {
        this.giftDAO = giftDAO;
        this.tagDAO = tagDAO;
    }

    /**
     * Invokes DAO method to create Gift with provided data.
     *
     * @param gift is {@link GiftDTO} object with Gift data.
     * @return {@link GiftDTO} object with created data.
     * @throws InvalidDataException if data failed validation
     */
    @Override
    @Transactional
    public GiftDTO createGift(GiftDTO gift) {
        if (!Validator.isValidGiftDto(gift)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION,
                    ERROR_CODE_INVALID_DATA);
        }
        Gift createdGift = giftDAO.createGift(GiftEntityDTOMapper.toEntity(gift));
        insertTagsIfNotExists(createdGift.getId(), gift.getTagList());
        return setTagsAndConvertToDTO(createdGift);
    }

    /**
     * Invokes DAO method to delete Gift with provided id.
     *
     * @param id is id of Gift to be deleted.
     * @throws NoSuchGiftException if no Gift with provided id founded
     */
    @Transactional
    @Override
    public void deleteGiftById(int id) {
        if (giftDAO.getGiftById(id).isEmpty()) {
            throw new NoSuchGiftException(String.format(MESSAGE_NO_SUCH_GIFT_EXCEPTION, id),
                    String.format(ERROR_CODE_NO_SUCH_GIFT, id));
        }
        giftDAO.deleteGiftById(id);
        giftDAO.deleteGiftTagByGiftId(id);
    }

    /**
     * Invokes DAO method to update Gift with provided data.
     *
     * @param giftDTO is {@link GiftDTO} object with Gift data.
     * @return {@link GiftDTO} object with updated data.
     * @throws NoSuchGiftException if no Gift with provided id founded
     */
    @Transactional
    @Override
    public GiftDTO updateGiftById(GiftDTO giftDTO, int id) {
        if (giftDAO.getGiftById(id).isEmpty()) {
            throw new NoSuchGiftException(String.format(MESSAGE_NO_SUCH_GIFT_EXCEPTION, id),
                    String.format(ERROR_CODE_NO_SUCH_GIFT, id));
        }

        String updateSql = GiftSqlBuilder.getUpdateSql(GiftEntityDTOMapper.toEntity(giftDTO));
        Gift gift = giftDAO.updateGiftById(updateSql, id);
        return setTagsAndConvertToDTO(gift);

    }

    /**
     * Invokes DAO method to get Gift with provided id.
     *
     * @param id is id of Gift to be returned.
     * @return {@link GiftDTO} object with Gift data.
     * @throws NoSuchGiftException if no Gift with provided id founded
     */
    @Transactional
    @Override
    public GiftDTO getGiftById(int id) {
        Optional<Gift> giftById = giftDAO.getGiftById(id);
        if (giftById.isEmpty()) {
            throw new NoSuchGiftException(String.format(MESSAGE_NO_SUCH_GIFT_EXCEPTION, id),
                    String.format(ERROR_CODE_NO_SUCH_GIFT, id));
        }
        return setTagsAndConvertToDTO(giftById.get());

    }

    /**
     * Invokes DAO method to get List of all Gifts from database.
     *
     * @return List of {@link GiftDTO} objects with Gift data.
     */
    @Override
    public List<GiftDTO> getGifts() {
        List<Gift> gifts = giftDAO.getGifts();
        List<GiftDTO> giftDTOList = new ArrayList<>();
        for (Gift gift : gifts) {
            giftDTOList.add(setTagsAndConvertToDTO(gift));
        }
        return giftDTOList;
    }

    /**
     * Invokes DAO method to get List of all Gifts that matches parameters
     *
     * @param params is {@link GiftSQLQueryParameters} object with requested parameters
     * @return List of {@link GiftDTO} objects with Gift data.
     */
    @Override
    public List<GiftDTO> getGiftsByParams(GiftSQLQueryParameters params) {
        List<GiftDTO> giftDTOList = new ArrayList<>();
        String getWithParamsSQL = GiftSqlBuilder.getGetWithParamsSQL(params);
        List<Gift> giftsByParams = giftDAO.getGiftsByParams(getWithParamsSQL);
        for (Gift giftsByParam : giftsByParams) {
            giftDTOList.add(setTagsAndConvertToDTO(giftsByParam));
        }
        return giftDTOList;
    }
    /**
     * Invokes DAO method to create GiftTag
     *
     * @param giftId is id of gift
     * @param tagId  is id of tag
     */
    @Transactional
    @Override
    public void createGiftTag(int giftId, int tagId) {
        Optional<Gift> giftById = giftDAO.getGiftById(giftId);
        Optional<Tag> tagById = tagDAO.getTagById(tagId);
        if (tagById.isPresent() && giftById.isPresent()) {
            giftDAO.createGiftTag(giftId, tagId);
        }
    }
    /**
     * Invokes DAO method to delete GiftTag
     *
     * @param giftId is id of gift
     */
    @Transactional
    @Override
    public void deleteGiftTagByGiftId(int giftId) {
        Optional<Gift> giftById = giftDAO.getGiftById(giftId);
        if (giftById.isPresent()) {
            giftDAO.deleteGiftTagByGiftId(giftId);
        }
    }

    private GiftDTO setTagsAndConvertToDTO(Gift gift) {
        List<Tag> tagListByGiftId = tagDAO.getTagListByGiftId(gift.getId());
        GiftDTO giftDTO = GiftEntityDTOMapper.toDTO(gift);
        giftDTO.setTagList(tagListByGiftId);
        return giftDTO;
    }

    private void insertTagsIfNotExists(int giftID, List<String> tagList) {
        if (tagList != null) {
            tagList.forEach(tagName -> {
                Optional<Tag> optionalTag = tagDAO.getTagByName(tagName);
                Tag tag = optionalTag.orElseGet(() -> tagDAO.createTag(tagName));

                giftDAO.createGiftTag(giftID, tag.getId());
            });
        }
    }
}
