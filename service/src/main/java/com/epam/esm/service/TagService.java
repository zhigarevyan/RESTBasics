package com.epam.esm.service;


import com.epam.esm.dto.TagDTO;
import com.epam.esm.exeption.impl.DuplicateTagException;
import com.epam.esm.exeption.impl.InvalidDataException;
import com.epam.esm.exeption.impl.NoSuchGiftException;
import com.epam.esm.exeption.impl.NoSuchTagException;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.repository.GiftRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.TagEntityDTOMapper;
import com.epam.esm.util.Validator;
import com.epam.esm.util.specification.TagSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link TagService}. Interface provides methods to interact with TagRepository.
 * Methods should transforms received information into Repository-accepted data and invoke corresponding methods.
 */
@Service
public class TagService {
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
     * Error message when Gift wasn't found by id
     */
    private static final String MESSAGE_NO_SUCH_GIFT_EXCEPTION = "No such gift with id - %d exception";
    /**
     * Error code when Gift wasn't found by id
     */
    private static final String ERROR_CODE_NO_SUCH_GIFT = "0102404_%d";
    /**
     * An object of {@link TagRepository}
     */
    private final TagRepository tagRepository;
    /**
     * An object of {@link GiftRepository}
     */
    private final GiftRepository giftRepository;
    /**
     * An object of {@link UserRepository}
     */
    private final UserRepository userRepository;

    /**
     * Public constructor that receives tagRepository
     *
     * @param tagRepository is {@link TagRepository} interface providing Repository methods.
     */
    @Autowired
    public TagService(TagRepository tagRepository, GiftRepository giftRepository, UserRepository userRepository) {
        this.tagRepository = tagRepository;
        this.giftRepository = giftRepository;
        this.userRepository = userRepository;
    }

    /**
     * Invokes Repository method to create Tag with provided data.
     *
     * @param name is String with name of Tag data.
     * @return {@link TagDTO} object with created data.
     * @throws NoSuchTagException   if no Tag with provided name founded
     * @throws InvalidDataException if data failed validation
     */
    @Transactional
    public TagDTO createTag(String name) {
        if (!Validator.isValidString(name)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION, ERROR_CODE_INVALID_DATA);
        }
        if (tagRepository.findByName(name).isPresent()) {
            throw new DuplicateTagException(String.format(MESSAGE_DUPLICATE_TAG_EXCEPTION, name),
                    ERROR_CODE_DUPLICATE_TAG);
        }
        Tag tag = new Tag();
        tag.setName(name);
        return TagEntityDTOMapper.toDTO(tagRepository.save(tag));
    }

    /**
     * Invokes Repository method to delete Tag with provided id.
     *
     * @param id is id of tag to be deleted.
     * @throws NoSuchTagException   if no Tag with provided name founded
     * @throws InvalidDataException if data failed validation
     */
    @Transactional
    public void deleteTagById(int id) {
        if (!Validator.isValidNumber(id)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION, ERROR_CODE_INVALID_DATA);
        }
        if (tagRepository.findById(id).isEmpty()) {
            throw new NoSuchTagException(String.format(MESSAGE_NO_SUCH_TAG_WITH_ID_EXCEPTION, id),
                    String.format(ERROR_CODE_NO_SUCH_TAG_BY_ID, id));
        }
        tagRepository.deleteById(id);
    }

    /**
     * Invokes Repository method to get Tag with provided id.
     *
     * @param id is id of tag to be returned.
     * @return {@link TagDTO} object with tag data.
     * @throws NoSuchTagException   if no Tag with provided name founded
     * @throws InvalidDataException if data failed validation
     */
    public TagDTO getTagById(int id) {
        if (!Validator.isValidNumber(id)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION, ERROR_CODE_INVALID_DATA);
        }
        Optional<Tag> tagById = tagRepository.findById(id);
        if (tagById.isEmpty()) {
            throw new NoSuchTagException(MESSAGE_NO_SUCH_TAG_WITH_NAME_EXCEPTION,
                    String.format(ERROR_CODE_NO_SUCH_TAG_BY_ID, id));
        }
        return TagEntityDTOMapper.toDTO(tagById.get());
    }

    /**
     * Invokes Repository method to get Tag with provided name.
     *
     * @param name is name of tag to be returned.
     * @return {@link TagDTO} object with tag data.
     * @throws NoSuchTagException   if no Tag with provided name founded
     * @throws InvalidDataException if data failed validation
     */
    public TagDTO getTagByName(String name) {
        if (!Validator.isValidString(name)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION, ERROR_CODE_INVALID_DATA);
        }
        Optional<Tag> tagByName = tagRepository.findByName(name);
        if (tagByName.isEmpty()) {
            throw new NoSuchTagException(MESSAGE_NO_SUCH_TAG_WITH_ID_EXCEPTION,
                    ERROR_CODE_NO_SUCH_TAG_BY_NAME);
        }
        return TagEntityDTOMapper.toDTO(tagByName.get());
    }

    /**
     * Invokes Repository method to get List of all Tags that linked with GiftCertificate by it's id
     *
     * @param giftId is id of GiftCertificate.
     * @return List of {@link TagDTO} objects with tag data.
     * @throws NoSuchGiftException  if no Gift with provided id founded
     * @throws InvalidDataException if data failed validation
     */
    public List<TagDTO> getTagListByGiftId(int giftId, Pageable page) {
        if (!Validator.isValidNumber(giftId)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION, ERROR_CODE_INVALID_DATA);
        }
        if (giftRepository.findById(giftId).isEmpty()) {
            throw new NoSuchGiftException(String.format(MESSAGE_NO_SUCH_GIFT_EXCEPTION, giftId),
                    String.format(ERROR_CODE_NO_SUCH_GIFT, giftId));
        }
        return TagEntityDTOMapper.toDTO(tagRepository.findAll(TagSpecification.tagListByGiftID(giftId), page).toList());
    }

    /**
     * Invokes Repository method to get List of all Tags from database.
     *
     * @return List of {@link TagDTO} objects with tag data.
     */
    public List<TagDTO> getAllTags(Pageable page) {
        return TagEntityDTOMapper.toDTO(tagRepository.findAll(page).toList());
    }

    /**
     * Invokes Dao method to get the most widely used tag of a user with the highest cost of all orders
     *
     * @return {@link TagDTO} object with tag data.
     */
    @Transactional
    public TagDTO getMostWidelyUsedTagFromUserWithHighestCostOfAllOrders() {
        Pageable firstResult = PageRequest.of(0, 1);
        User user = userRepository.findUserWithHighestCost(firstResult).toList().get(0);
        Tag mostWidelyUsedTag = tagRepository.getMostWidelyUsedTag(user.getId());

        return TagEntityDTOMapper.toDTO(mostWidelyUsedTag);
    }
}
