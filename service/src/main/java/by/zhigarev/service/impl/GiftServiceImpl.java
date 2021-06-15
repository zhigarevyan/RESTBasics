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

@Service
public class GiftServiceImpl implements GiftService {
    private static final String MESSAGE_INVALID_DATA_EXCEPTION = "Invalid data exception";
    private static final String MESSAGE_NO_SUCH_GIFT_EXCEPTION = "No such gift exception";
    private GiftDAO giftDAO;
    private TagDAO tagDAO;

    @Autowired
    public GiftServiceImpl(GiftDAO giftDAO, TagDAO tagDAO) {
        this.giftDAO = giftDAO;
        this.tagDAO = tagDAO;
    }

    @Override
    @Transactional
    public GiftDTO createGift(GiftDTO gift) {
        if (!Validator.isValidGiftDto(gift)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION);
        }
        Gift createdGift = giftDAO.createGift(GiftEntityDTOMapper.toEntity(gift));
        insertTagsIfNotExists(createdGift.getId(), gift.getTagList());
        return setTagsAndConvertToDTO(createdGift);
    }

    @Transactional
    @Override
    public void deleteGiftById(int id) {
        if (giftDAO.getGiftById(id).isEmpty()) {
            throw new NoSuchGiftException(MESSAGE_NO_SUCH_GIFT_EXCEPTION);
        }
        giftDAO.deleteGiftById(id);
        giftDAO.deleteGiftTagByGiftId(id);
    }

    @Transactional
    @Override
    public GiftDTO updateGiftById(GiftDTO giftDTO, int id) {
        if (giftDAO.getGiftById(id).isEmpty()) {
            throw new NoSuchGiftException(MESSAGE_NO_SUCH_GIFT_EXCEPTION);
        }

        String updateSql = GiftSqlBuilder.getUpdateSql(GiftEntityDTOMapper.toEntity(giftDTO));
        Gift gift = giftDAO.updateGiftById(updateSql, id);
        return setTagsAndConvertToDTO(gift);

    }

    @Transactional
    @Override
    public GiftDTO getGiftById(int id) {
        Optional<Gift> giftById = giftDAO.getGiftById(id);
        if (giftById.isEmpty()) {
            throw new NoSuchGiftException(MESSAGE_NO_SUCH_GIFT_EXCEPTION);
        }
        return setTagsAndConvertToDTO(giftById.get());

    }

    @Override
    public List<GiftDTO> getGifts() {
        List<Gift> gifts = giftDAO.getGifts();
        List<GiftDTO> giftDTOList = new ArrayList<>();
        for (Gift gift : gifts) {
            giftDTOList.add(setTagsAndConvertToDTO(gift));
        }
        return giftDTOList;
    }

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

    @Transactional
    @Override
    public void createGiftTag(int giftId, int tagId) {
        Optional<Gift> giftById = giftDAO.getGiftById(giftId);
        Optional<Tag> tagById = tagDAO.getTagById(tagId);
        if (tagById.isPresent() && giftById.isPresent()) {
            giftDAO.createGiftTag(giftId, tagId);
        }
    }

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
