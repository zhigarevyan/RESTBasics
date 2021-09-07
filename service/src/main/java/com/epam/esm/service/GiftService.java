package com.epam.esm.service;


import com.epam.esm.dto.GiftDTO;
import com.epam.esm.exeption.impl.InvalidDataException;
import com.epam.esm.exeption.impl.NoSuchGiftException;
import com.epam.esm.exeption.impl.NoSuchOrderException;
import com.epam.esm.model.Gift;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.GiftRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.GiftEntityDTOMapper;
import com.epam.esm.util.GiftFieldUpdater;
import com.epam.esm.util.GiftQueryParameters;
import com.epam.esm.util.Validator;
import com.epam.esm.util.specification.GiftSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.util.GiftEntityDTOMapper.toDTO;

/**
 * Implementation of {@link GiftService}. Interface provides methods to interact with GiftRepository
 *.
 * Methods should transforms received information into Repository
 *-accepted data and invoke corresponding methods.
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
     * An object of {@link GiftRepository
     *}
     */
    private final GiftRepository giftRepository;
    /**
     * An object of {@link TagRepository
     *}
     */
    private final TagRepository tagRepository;
    /**
     * An object of {@link OrderRepository
     *}
     */
    private final OrderRepository orderRepository;

    /**
     * Public constructor that receives giftRepository
     *and tagRepository
     *
     *
     * @param giftRepository
     *is {@link GiftRepository
     *} interface providing Repository
     *methods.
     * @param tagRepository
     *is {@link TagRepository
     *} interface providing Repository
     *methods.
     */
    @Autowired
    public GiftService(GiftRepository giftRepository, TagRepository
            tagRepository, OrderRepository
            orderRepository) {
        this.giftRepository = giftRepository;
        this.tagRepository = tagRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Invokes Repository
     *method to create Gift with provided data.
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
        entity.setCreateDate(Instant.now());
        entity.setLastUpdateDate(Instant.now());
        Gift createdGift = giftRepository.save(entity);
        return toDTO(createdGift);
    }

    /**
     * Invokes Repository
     *method to delete Gift with provided id.
     *
     * @param id is id of Gift to be deleted.
     * @throws NoSuchGiftException if no Gift with provided id founded
     */
    @Transactional
    public void deleteGiftById(int id) {
        if (giftRepository.findById(id).isEmpty()) {
            throw new NoSuchGiftException(String.format(MESSAGE_NO_SUCH_GIFT_EXCEPTION, id),
                    String.format(ERROR_CODE_NO_SUCH_GIFT, id));
        }
        giftRepository.deleteById(id);
    }

    /**
     * Invokes Repository
     *method to update Gift with provided data.
     *
     * @param giftDTO is {@link GiftDTO} object with Gift data.
     * @return {@link GiftDTO} object with updated data.
     * @throws NoSuchGiftException if no Gift with provided id founded
     */
    @Transactional
    public GiftDTO updateGiftById(GiftDTO giftDTO, int id) {
        if (giftRepository.findById(id).isEmpty()) {
            throw new NoSuchGiftException(String.format(MESSAGE_NO_SUCH_GIFT_EXCEPTION, id),
                    String.format(ERROR_CODE_NO_SUCH_GIFT, id));
        }

        Gift giftFromDB = giftRepository.findById(id).orElseThrow(() ->
                new NoSuchGiftException(String.format(MESSAGE_NO_SUCH_GIFT_EXCEPTION, id),
                        String.format(ERROR_CODE_NO_SUCH_GIFT, id)));

        LocalDateTime now = LocalDateTime.now();
        giftDTO.setLastUpdateDate(now);

        List<Tag> tagList = createTagsIfNotFoundAndReturnAll(giftDTO.getTagList());
        Gift entity = GiftEntityDTOMapper.toEntity(giftDTO);
        entity.setTagList(tagList);

        GiftFieldUpdater.update(giftFromDB,entity);
        return toDTO(giftRepository.save(giftFromDB));
    }

    /**
     * Invokes Repository
     *method to get Gift with provided id.
     *
     * @param id is id of Gift to be returned.
     * @return {@link GiftDTO} object with Gift data.
     * @throws NoSuchGiftException if no Gift with provided id founded
     */
    @Transactional
    public GiftDTO getGiftById(int id) {
        Optional<Gift> giftById = giftRepository.findById(id);
        if (giftById.isEmpty()) {
            throw new NoSuchGiftException(String.format(MESSAGE_NO_SUCH_GIFT_EXCEPTION, id),
                    String.format(ERROR_CODE_NO_SUCH_GIFT, id));
        }

        return toDTO(giftById.get());
    }

    /**
     * Invokes Repository
     *method to get List of all Gifts that matches parameters
     *
     * @param params is {@link GiftQueryParameters} object with requested parameters
     * @return List of {@link GiftDTO} objects with Gift data.
     */
    public List<GiftDTO> getGiftsByParams(GiftQueryParameters params,Pageable page) {
        if (params.isEmpty()) {
            return getGifts(page);
        }
        List<Gift> giftList = giftRepository.findAll(GiftSpecification.findByQueryParameter(params),page).toList();

        return toDTO(giftList);
    }
    /**
     * Invokes Repository method to get List of all GiftCertificates from database.
     *
     * @param pageable is {@link Pageable} object with page number and page size
     * @return List of {@link GiftDTO} objects with GiftCertificate data.
     */
    public List<GiftDTO> getGifts(Pageable pageable) {
        List<Gift> giftCertificateList = giftRepository.findAll(pageable).toList();

        return GiftEntityDTOMapper.toDTO(giftCertificateList);
    }

    public List<GiftDTO> getCertificateListByOrderID(int id, Pageable page) {
        if (orderRepository.findById(id).isEmpty()) {
            throw new NoSuchOrderException(
                    String.format(MESSAGE_NO_SUCH_ORDER_EXCEPTION, id),
                    String.format(ERROR_CODE_NO_SUCH_ORDER, id));
        }
        List<Gift> giftList = giftRepository.findAll(GiftSpecification.giftListByOrderId(id),page).toList();

        return toDTO(giftList);
    }


    private List<Tag> createTagsIfNotFoundAndReturnAll(List<String> tagNamesList) {
        if (tagNamesList == null) {
            return new ArrayList<>();
        }
        List<Tag> tagList = new ArrayList<>();

        tagNamesList.forEach(tagName -> {
            Optional<Tag> optionalTag = tagRepository.findByName(tagName);

            Tag tagForSave = new Tag();
            tagForSave.setName(tagName);

            Tag tag = optionalTag.orElseGet(() -> tagRepository.save(tagForSave));
            tagList.add(tag);
        });

        return tagList;
    }
}
