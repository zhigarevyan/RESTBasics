package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exeption.impl.DuplicateTagException;
import com.epam.esm.exeption.impl.InvalidDataException;
import com.epam.esm.exeption.impl.NoSuchTagException;
import com.epam.esm.model.Gift;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.util.Page;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    @Mock
    private TagDAO tagDAO;
    @Mock
    private GiftDAO giftDAO;
    @Mock
    private UserDAO userDAO;
    @InjectMocks
    private TagService tagService;

    private Tag tag;
    private Gift gift;
    private List<Tag> tagList;
    private List<TagDTO> tagDTOList;
    private Page page;
    private static final Integer TEST_ID = 1;
    private static final String TEST_NAME = "TEST TAG";


    @BeforeEach
    void setUp() {
        page = Page.getDefaultPage();

        gift = new Gift();

        tag = new Tag();
        tag.setId(TEST_ID);
        tag.setName(TEST_NAME);

        tagList = new ArrayList<>();
        tagList.add(tag);

        tagDTOList = new ArrayList<>();
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(TEST_ID);
        tagDTO.setName(TEST_NAME);
        tagDTOList.add(tagDTO);

        tagService = new TagService(tagDAO, giftDAO, userDAO);
    }

    @Test
    void createTag() {
        given(tagDAO.createTag(TEST_NAME)).willReturn(tag);
        TagDTO actualTagDTO = tagService.createTag(TEST_NAME);
        assertEquals(TEST_NAME, actualTagDTO.getName());
    }

    @Test
    void createTagDuplicateTagException() {
        given(tagDAO.getTagByName(TEST_NAME)).willReturn(Optional.of(tag));
        assertThrows(DuplicateTagException.class, () -> tagService.createTag(TEST_NAME));
    }

    @Test
    void deleteTagById() {
        given(tagDAO.getTagById(TEST_ID)).willReturn(Optional.of(tag));
        tagService.deleteTagById(TEST_ID);
        verify(tagDAO, times(1)).deleteTagById(TEST_ID);
    }

    @Test
    void deleteTagByIdTagNoSuchTagException() {
        given(tagDAO.getTagById(TEST_ID)).willReturn(Optional.empty());
        assertThrows(NoSuchTagException.class, () -> tagService.deleteTagById(TEST_ID));
    }

    @Test
    void deleteTagByIdTagValidationException() {
        final int ID = -1;
        assertThrows(InvalidDataException.class, () -> tagService.deleteTagById(ID));
    }

    @Test
    void getTagById() {
        given(tagDAO.getTagById(TEST_ID)).willReturn(Optional.of(tag));
        TagDTO tagDTO = tagService.getTagById(TEST_ID);
        assertEquals(tagDTO.getName(), TEST_NAME);
        assertEquals(tagDTO.getId(), TEST_ID);
    }

    @Test
    void getTagByIdNoSuchTagException() {
        given(tagDAO.getTagById(TEST_ID)).willReturn(Optional.empty());
        assertThrows(NoSuchTagException.class, () -> tagService.getTagById(TEST_ID));
    }

    @Test
    void getTagByIdInvalidDataException() {
        final int ID = -1;
        assertThrows(InvalidDataException.class, () -> tagService.getTagById(ID));
    }

    @Test
    void getTagByName() {
        given(tagDAO.getTagByName(TEST_NAME)).willReturn(Optional.of(tag));
        TagDTO tagDTO = tagService.getTagByName(TEST_NAME);
        assertEquals(tagDTO.getName(), TEST_NAME);
        assertEquals(tagDTO.getId(), TEST_ID);
    }

    @Test
    void getTagByNameNoSuchTagException() {
        given(tagDAO.getTagByName(TEST_NAME)).willReturn(Optional.empty());
        assertThrows(NoSuchTagException.class, () -> tagService.getTagByName(TEST_NAME));
    }

    @Test
    void getTagByNameInvalidDataException() {
        final String NAME = "";
        assertThrows(InvalidDataException.class, () -> tagService.getTagByName(NAME));
    }


    @Test
    void getTagListByGiftId() {
        given(tagDAO.getTagListByGiftId(TEST_ID,any(),any())).willReturn(tagList);
        given(giftDAO.getGiftById(TEST_ID)).willReturn(Optional.of(gift));
        List<TagDTO> tagListByGiftId = tagService.getTagListByGiftId(TEST_ID,page);
        assertIterableEquals(tagDTOList, tagListByGiftId);

    }

    @Test
    void getTagListByGiftIdInvalidDataException() {
        final int ID = -1;
        assertThrows(InvalidDataException.class, () -> tagService.getTagListByGiftId(ID,page));
    }

    @Test
    void getAllTags() {
        given(tagDAO.getAllTags(page.getPage(), page.getSize())).willReturn(tagList);
        List<TagDTO> allTags = tagService.getAllTags(page);
        assertIterableEquals(tagDTOList, allTags);
    }
}