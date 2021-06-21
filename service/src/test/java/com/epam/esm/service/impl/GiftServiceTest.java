package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.dto.GiftDTO;
import com.epam.esm.exeption.impl.InvalidDataException;
import com.epam.esm.exeption.impl.NoSuchGiftException;
import com.epam.esm.model.Gift;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftService;
import com.epam.esm.util.GiftSQLQueryParameters;
import com.epam.esm.util.GiftSqlBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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


    @Mock
    private GiftDAO giftDAO;
    @Mock
    private TagDAO tagDAO;
    @InjectMocks
    private GiftService giftService;



    @BeforeEach
    void setUp() {
        gift1 = new Gift();
        gift1.setId(TEST_ID_1);
        gift1.setName(TEST_NAME_1);
        gift1.setDescription(TEST_DESCRIPTION);
        gift1.setDuration(TEST_DURATION);
        gift1.setPrice(TEST_PRICE);

        giftDTO = new GiftDTO();
        giftDTO.setId(TEST_ID_1);
        giftDTO.setName(TEST_NAME_1);
        giftDTO.setDescription(TEST_DESCRIPTION);
        giftDTO.setDuration(TEST_DURATION);
        giftDTO.setPrice(TEST_PRICE);
        giftDTO.setTagList(new ArrayList<String>());

        giftDTOList = new ArrayList<>();
        giftDTOList.add(giftDTO);
        giftList = new ArrayList<>();
        giftList.add(gift1);

        tag = new Tag();
        tag.setId(TEST_ID_1);
        tag.setName(TEST_NAME_1);


        giftService = new GiftService(giftDAO,tagDAO);
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
        verify(giftDAO,times(1)).deleteGiftTagByGiftId(TEST_ID_1);

    }
    @Test
    void deleteGiftByIdNoSuchGiftException(){
        given(giftDAO.getGiftById(TEST_ID_1)).willReturn(Optional.empty());
        assertThrows(NoSuchGiftException.class,() -> giftService.deleteGiftById(TEST_ID_1));
    }

    @Test
    void updateGiftById() {
        given(giftDAO.getGiftById(TEST_ID_1)).willReturn(Optional.of(gift1));
        given(giftDAO.updateGiftById(any(),anyInt())).willReturn(gift1);

        GiftDTO updatedGift = giftService.updateGiftById(giftDTO, TEST_ID_1);
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
        given(giftDAO.getGifts()).willReturn(giftList);
        List<GiftDTO> gifts = giftService.getGifts();
        assertIterableEquals(giftDTOList,gifts);
    }

    @Test
    void getGiftsByParams() {
        given(giftDAO.getGiftsByParams(any())).willReturn(giftList);
        GiftSQLQueryParameters giftSQLQueryParameters = new GiftSQLQueryParameters();
        List<GiftDTO> giftsByParams = giftService.getGiftsByParams(giftSQLQueryParameters);
        verify(giftDAO).getGiftsByParams(GiftSqlBuilder.getGetWithParamsSQL(giftSQLQueryParameters));

        assertIterableEquals(giftDTOList,giftsByParams);
    }

    @Test
    void createGiftTag(){
        doNothing().when(giftDAO).createGiftTag(TEST_ID_1, TEST_ID_1);
        given(giftDAO.getGiftById(TEST_ID_1)).willReturn(Optional.of(gift1));
        given(tagDAO.getTagById(TEST_ID_1)).willReturn(Optional.of(tag));
        giftService.createGiftTag(TEST_ID_1, TEST_ID_1);
        verify(giftDAO,times(1)).createGiftTag(anyInt(),anyInt());
    }

    @Test
    void deleteGiftTag(){
        given(giftDAO.getGiftById(TEST_ID_1)).willReturn(Optional.of(gift1));
        giftService.deleteGiftTagByGiftId(TEST_ID_1);
        verify(giftDAO,times(1)).deleteGiftTagByGiftId(anyInt());
    }


}