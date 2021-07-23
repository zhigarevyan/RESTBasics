package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftDAO;
import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.dto.GiftDTO;
import com.epam.esm.exeption.impl.InvalidDataException;
import com.epam.esm.exeption.impl.NoSuchGiftException;
import com.epam.esm.model.Gift;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftService;
import com.epam.esm.util.GiftQueryParameters;
import com.epam.esm.util.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GiftServiceTest {

    private static final Integer TEST_ID_1 = 1;
    private static final String TEST_NAME_1 = "TEST GIFT 1";
    private static final String TEST_DESCRIPTION = "TEST DESCRIPTION";
    private static final Integer TEST_DURATION = 10;
    private static final Integer TEST_PRICE = 10;

    private Gift gift1;
    private Tag tag;
    private GiftDTO giftDTO;
    private List<GiftDTO> giftDTOList;
    private List<Gift> giftList;
    private Page page;


    @Mock
    private GiftDAO giftDAO;
    @Mock
    private TagDAO tagDAO;
    @Mock
    private OrderDAO orderDAO;
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

        page = Page.getDefaultPage();

        giftService = new GiftService(giftDAO,tagDAO,orderDAO);
    }

    @Test
    void createGift() {
        given(giftDAO.createGift(any())).willReturn(gift1);
        GiftDTO dto = giftService.createGift(giftDTO);

        assertEquals(TEST_ID_1,dto.getId());
        assertEquals(TEST_NAME_1,dto.getName());
        assertEquals(TEST_DESCRIPTION,dto.getDescription());
        assertEquals(TEST_DURATION,dto.getDuration());
        assertEquals(TEST_PRICE,dto.getPrice());
    }

    @Test
    void createGiftInvalidDataException(){
        GiftDTO giftDTO = new GiftDTO();
        assertThrows(InvalidDataException.class,() -> giftService.createGift(giftDTO));
    }

    @Test
    void deleteGiftById() {
        given(giftDAO.getGiftById(TEST_ID_1)).willReturn(Optional.of(gift1));
        giftService.deleteGiftById(TEST_ID_1);

        verify(giftDAO,times(1)).deleteGiftById(TEST_ID_1);

    }
    @Test
    void deleteGiftByIdNoSuchGiftException(){
        given(giftDAO.getGiftById(TEST_ID_1)).willReturn(Optional.empty());
        assertThrows(NoSuchGiftException.class,() -> giftService.deleteGiftById(TEST_ID_1));
    }

    @Test
    void updateGiftById() {
        given(giftDAO.getGiftById(TEST_ID_1)).willReturn(Optional.of(gift1));

        giftService.updateGiftById(giftDTO, TEST_ID_1);
        GiftDTO updatedGift = giftService.getGiftById(TEST_ID_1);
        assertEquals(giftDTO,updatedGift);
    }
    @Test
    void updateGiftByIdNoSuchGiftException(){
        given(giftDAO.getGiftById(TEST_ID_1)).willReturn(Optional.empty());
        assertThrows(NoSuchGiftException.class,() -> giftService.updateGiftById(giftDTO,TEST_ID_1));
    }

    @Test
    void getGiftById() {
        given(giftDAO.getGiftById(TEST_ID_1)).willReturn(Optional.of(gift1));
        GiftDTO giftById = giftService.getGiftById(TEST_ID_1);
        assertEquals(giftDTO,giftById);
    }
    @Test
    void getGiftByIdNoSuchGiftException(){
        given(giftDAO.getGiftById(TEST_ID_1)).willReturn(Optional.empty());
        assertThrows(NoSuchGiftException.class,() -> giftService.getGiftById(TEST_ID_1));
    }

    @Test
    void getGifts() {
        given(giftDAO.getGifts(any(),any())).willReturn(giftList);
        List<GiftDTO> gifts = giftService.getGifts(page);
        assertIterableEquals(giftDTOList,gifts);
    }

    @Test
    void getGiftsByParams() {
        given(giftDAO.getGiftsByParams(any())).willReturn(giftList);
        GiftQueryParameters giftQueryParameters = new GiftQueryParameters();
        List<GiftDTO> giftsByParams = giftService.getGiftsByParams(giftQueryParameters);
        verify(giftDAO).getGiftsByParams(giftQueryParameters);

        assertIterableEquals(giftDTOList,giftsByParams);
    }


}