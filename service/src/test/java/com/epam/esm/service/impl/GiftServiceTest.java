package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftDTO;
import com.epam.esm.exeption.impl.InvalidDataException;
import com.epam.esm.exeption.impl.NoSuchGiftException;
import com.epam.esm.model.Gift;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.GiftRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftService;
import com.epam.esm.util.GiftQueryParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftServiceTest {

    private static final int TEST_ID_1 = 1;
    private static final String TEST_NAME_1 = "TEST GIFT 1";
    private static final String TEST_DESCRIPTION = "TEST DESCRIPTION";
    private static final int TEST_DURATION = 10;
    private static final int TEST_PRICE = 10;

    private Gift gift1;
    private Tag tag;
    private GiftDTO giftDTO;
    private List<GiftDTO> giftDTOList;
    private List<Gift> giftList;
    private Pageable page = Pageable.unpaged();


    @Mock
    private GiftRepository giftRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private GiftService giftService;


    @BeforeEach
    void setUp() {
        Instant now = Instant.now();

        gift1 = new Gift();
        gift1.setId(TEST_ID_1);
        gift1.setName(TEST_NAME_1);
        gift1.setDescription(TEST_DESCRIPTION);
        gift1.setDuration(TEST_DURATION);
        gift1.setPrice(TEST_PRICE);
        gift1.setCreateDate(now);
        gift1.setLastUpdateDate(now);
        gift1.setTagList(new ArrayList<>());

        giftDTO = new GiftDTO();
        giftDTO.setId(TEST_ID_1);
        giftDTO.setName(TEST_NAME_1);
        giftDTO.setDescription(TEST_DESCRIPTION);
        giftDTO.setDuration(TEST_DURATION);
        giftDTO.setPrice(TEST_PRICE);
        giftDTO.setCreateDate(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        giftDTO.setLastUpdateDate(LocalDateTime.ofInstant(now, ZoneOffset.UTC));
        giftDTO.setTagList(new ArrayList<String>());

        giftDTOList = new ArrayList<>();
        giftDTOList.add(giftDTO);
        giftList = new ArrayList<>();
        giftList.add(gift1);

        tag = new Tag();
        tag.setId(TEST_ID_1);
        tag.setName(TEST_NAME_1);


        giftService = new GiftService(giftRepository, tagRepository, orderRepository);
    }

    @Test
    void createGift() {
        given(giftRepository.save(any())).willReturn(gift1);
        GiftDTO dto = giftService.createGift(giftDTO);

        assertEquals(TEST_ID_1, dto.getId());
        assertEquals(TEST_NAME_1, dto.getName());
        assertEquals(TEST_DESCRIPTION, dto.getDescription());
        assertEquals(TEST_DURATION, dto.getDuration());
        assertEquals(TEST_PRICE, dto.getPrice());
    }

    @Test
    void createGiftInvalidDataException() {
        GiftDTO giftDTO = new GiftDTO();
        assertThrows(InvalidDataException.class, () -> giftService.createGift(giftDTO));
    }

    @Test
    void deleteGiftById() {
        given(giftRepository.findById(TEST_ID_1)).willReturn(Optional.of(gift1));
        giftService.deleteGiftById(TEST_ID_1);

        verify(giftRepository, times(1)).deleteById(TEST_ID_1);

    }

    @Test
    void deleteGiftByIdNoSuchGiftException() {
        given(giftRepository.findById(TEST_ID_1)).willReturn(Optional.empty());
        assertThrows(NoSuchGiftException.class, () -> giftService.deleteGiftById(TEST_ID_1));
    }

    @Test
    void updateGiftById() {
        given(giftRepository.findById(TEST_ID_1)).willReturn(Optional.of(gift1));
        given(giftRepository.save(any())).willReturn(gift1);
        giftService.updateGiftById(giftDTO, TEST_ID_1);
        GiftDTO updatedGift = giftService.getGiftById(TEST_ID_1);
        assertNotEquals(giftDTO.getLastUpdateDate(), updatedGift.getLastUpdateDate());
        assertEquals(giftDTO.getCreateDate(), updatedGift.getCreateDate());
        assertEquals(giftDTO.getDuration(), updatedGift.getDuration());
        assertEquals(giftDTO.getDescription(), updatedGift.getDescription());
        assertEquals(giftDTO.getId(), updatedGift.getId());
        assertEquals(giftDTO.getPrice(), updatedGift.getPrice());
        assertEquals(giftDTO.getName(), updatedGift.getName());
        assertEquals(giftDTO.getTagList(), updatedGift.getTagList());
    }

    @Test
    void updateGiftByIdNoSuchGiftException() {
        given(giftRepository.findById(TEST_ID_1)).willReturn(Optional.empty());
        assertThrows(NoSuchGiftException.class, () -> giftService.updateGiftById(giftDTO, TEST_ID_1));
    }

    @Test
    void getGiftById() {
        given(giftRepository.findById(1)).willReturn(Optional.of(gift1));
        GiftDTO giftById = giftService.getGiftById(1);
        assertEquals(giftDTO, giftById);
    }

    @Test
    void getGiftByIdNoSuchGiftException() {
        given(giftRepository.findById(TEST_ID_1)).willReturn(Optional.empty());
        assertThrows(NoSuchGiftException.class, () -> giftService.getGiftById(TEST_ID_1));
    }

    @Test
    void getGifts() {
        Page<Gift> pageable = mock(Page.class);
        when(pageable.toList()).thenReturn(giftList);
        given(giftRepository.findAll(any(Pageable.class))).willReturn(pageable);
        List<GiftDTO> gifts = giftService.getGifts(page);
        assertIterableEquals(giftDTOList, gifts);
    }

    @Test
    public void getGiftsByParamsWithNoParams() {
        final int CORRECT_SIZE = 1;
        GiftQueryParameters giftQueryParameters = new GiftQueryParameters();
        Page<Gift> pageable = mock(Page.class);
        when(pageable.toList()).thenReturn(giftList);
        given(giftRepository.findAll(any(Pageable.class))).willReturn(pageable);
        List<GiftDTO> giftDTOList = giftService.getGiftsByParams(giftQueryParameters, Pageable.unpaged());
        assertEquals(CORRECT_SIZE, giftDTOList.size());
    }

    @Test
    public void getGiftsByParams() {
        final int CORRECT_SIZE = 1;
        GiftQueryParameters giftQueryParameters = new GiftQueryParameters();
        giftQueryParameters.setName(TEST_NAME_1);
        Page<Gift> pageable = mock(Page.class);
        when(pageable.toList()).thenReturn(giftList);
        given(giftRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(pageable);
        List<GiftDTO> giftDTOList = giftService.getGiftsByParams(giftQueryParameters, Pageable.unpaged());
        assertEquals(CORRECT_SIZE, giftDTOList.size());
    }


}