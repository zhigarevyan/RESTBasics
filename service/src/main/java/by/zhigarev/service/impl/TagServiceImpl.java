package by.zhigarev.service.impl;

import by.zhigarev.dao.TagDAO;
import by.zhigarev.dto.TagDTO;
import by.zhigarev.exeption.impl.DuplicateTagException;
import by.zhigarev.exeption.impl.InvalidDataException;
import by.zhigarev.exeption.impl.NoSuchTagException;
import by.zhigarev.model.Tag;
import by.zhigarev.service.TagService;
import by.zhigarev.util.TagEntityDTOMapper;
import by.zhigarev.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link TagService}. Interface provides methods to interact with TagDAO.
 * Methods should transforms received information into DAO-accepted data and invoke corresponding methods.
 */
@Service
public class TagServiceImpl implements TagService {
    /**
     * Error message when data failed validation
     */
    private static final String MESSAGE_INVALID_DATA_EXCEPTION = "Invalid data";
    /**
     * Error message when Tag with provided name already exists
     */
    private static final String MESSAGE_DUPLICATE_TAG_EXCEPTION = "Duplicate tag with name - %s";
    /**
     * Error message when Tag wasn't found by id
     */
    private static final String MESSAGE_NO_SUCH_TAG_WITH_ID_EXCEPTION = "No such tag with id - %d exception";
    /**
     * Error message when Tag wasn't found by name
     */
    private static final String MESSAGE_NO_SUCH_TAG_WITH_NAME_EXCEPTION = "No such tag with name - %s exception";
    /**
     * Error code when Tag with provided name already exists
     */
    private static final String ERROR_CODE_DUPLICATE_TAG = "0202";
    /**
     * Error code when data failed validation
     */
    private static final String ERROR_CODE_INVALID_DATA = "0201";

    /**
     * Error code when Tag wasn't found by id
     */
    private static final String ERROR_CODE_NO_SUCH_TAG_BY_ID = "0202404_%d";
    /**
     * Error code when Tag wasn't found by name
     */
    private static final String ERROR_CODE_NO_SUCH_TAG_BY_NAME = "0202404";
    /**
     * An object of {@link TagDAO}
     */
    private final TagDAO tagDAO;

    /**
     * Public constructor that receives tagDAO
     *
     * @param tagDAO is {@link TagDAO} interface providing DAO methods.
     */
    @Autowired
    public TagServiceImpl(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    /**
     * Invokes DAO method to create Tag with provided data.
     *
     * @param name is String with name of Tag data.
     * @return {@link TagDTO} object with created data.
     * @throws NoSuchTagException   if no Tag with provided name founded
     * @throws InvalidDataException if data failed validation
     */
    @Transactional
    @Override
    public TagDTO createTag(String name) {
        if (tagDAO.getTagByName(name).isPresent()) {
            throw new DuplicateTagException(String.format(MESSAGE_DUPLICATE_TAG_EXCEPTION, name),
                    ERROR_CODE_DUPLICATE_TAG);
        }
        if (!Validator.isValidString(name)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION, ERROR_CODE_INVALID_DATA);
        }
        return TagEntityDTOMapper.toDTO(tagDAO.createTag(name));
    }

    /**
     * Invokes DAO method to delete Tag with provided id.
     *
     * @param id is id of tag to be deleted.
     * @throws NoSuchTagException   if no Tag with provided name founded
     * @throws InvalidDataException if data failed validation
     */
    @Transactional
    @Override
    public void deleteTagById(int id) {
        if (tagDAO.getTagById(id).isEmpty()) {
            throw new NoSuchTagException(String.format(MESSAGE_NO_SUCH_TAG_WITH_ID_EXCEPTION, id),
                    String.format(ERROR_CODE_NO_SUCH_TAG_BY_ID, id));
        }
        if (!Validator.isValidNumber(id)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION, ERROR_CODE_INVALID_DATA);
        }
        tagDAO.deleteTagById(id);
    }

    /**
     * Invokes DAO method to get Tag with provided id.
     *
     * @param id is id of tag to be returned.
     * @return {@link TagDTO} object with tag data.
     * @throws NoSuchTagException   if no Tag with provided name founded
     * @throws InvalidDataException if data failed validation
     */
    @Override
    public TagDTO getTagById(int id) {
        if (!Validator.isValidNumber(id)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION, ERROR_CODE_INVALID_DATA);
        }
        Optional<Tag> tagById = tagDAO.getTagById(id);
        if (tagById.isEmpty()) {
            throw new NoSuchTagException(MESSAGE_NO_SUCH_TAG_WITH_NAME_EXCEPTION,
                    String.format(ERROR_CODE_NO_SUCH_TAG_BY_ID, id));
        }
        return TagEntityDTOMapper.toDTO(tagById.get());
    }

    /**
     * Invokes DAO method to get Tag with provided name.
     *
     * @param name is name of tag to be returned.
     * @return {@link TagDTO} object with tag data.
     * @throws NoSuchTagException   if no Tag with provided name founded
     * @throws InvalidDataException if data failed validation
     */
    @Override
    public TagDTO getTagByName(String name) {
        if (!Validator.isValidString(name)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION, ERROR_CODE_INVALID_DATA);
        }
        Optional<Tag> tagByName = tagDAO.getTagByName(name);
        if (tagByName.isEmpty()) {
            throw new NoSuchTagException(MESSAGE_NO_SUCH_TAG_WITH_ID_EXCEPTION,
                    ERROR_CODE_NO_SUCH_TAG_BY_NAME);
        }
        return TagEntityDTOMapper.toDTO(tagByName.get());
    }

    /**
     * Invokes DAO method to get List of all Tags that linked with GiftCertificate by it's id
     *
     * @param giftId is id of GiftCertificate.
     * @return List of {@link TagDTO} objects with tag data.
     */
    @Override
    public List<TagDTO> getTagListByGiftId(int giftId) {
        if (!Validator.isValidNumber(giftId)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION, ERROR_CODE_INVALID_DATA);
        }
        return TagEntityDTOMapper.toDTO(tagDAO.getTagListByGiftId(giftId));
    }

    /**
     * Invokes DAO method to get List of all Tags from database.
     *
     * @return List of {@link TagDTO} objects with tag data.
     */
    @Override
    public List<TagDTO> getAllTags() {
        return TagEntityDTOMapper.toDTO(tagDAO.getAllTags());
    }
}
