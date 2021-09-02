package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.exeption.impl.DuplicateTagException;
import com.epam.esm.exeption.impl.InvalidDataException;
import com.epam.esm.exeption.impl.NoSuchTagException;
import com.epam.esm.model.Gift;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.GiftRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.util.specification.TagSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    @Mock
    private TagRepository tagRepository;
    @Mock
    private GiftRepository giftRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TagService tagService;

    private Tag tag;
    private Gift gift;
    private List<Tag> tagList;
    private List<TagDTO> tagDTOList;
    private Pageable page;
    private static final int TEST_ID = 1;
    private static final String TEST_NAME = "TEST TAG";


    @BeforeEach
    void setUp() {
        page = Pageable.unpaged();

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

        tagService = new TagService(tagRepository, giftRepository, userRepository);
    }

    @Test
    void createTag() {
        given(tagRepository.save(any())).willReturn(tag);
        TagDTO actualTagDTO = tagService.createTag(TEST_NAME);
        assertEquals(TEST_NAME, actualTagDTO.getName());
    }

    @Test
    void createTagDuplicateTagException() {
        given(tagRepository.findByName(TEST_NAME)).willReturn(Optional.of(tag));
        assertThrows(DuplicateTagException.class, () -> tagService.createTag(TEST_NAME));
    }

    @Test
    void deleteTagById() {
        given(tagRepository.findById(TEST_ID)).willReturn(Optional.of(tag));
        tagService.deleteTagById(TEST_ID);
        verify(tagRepository, times(1)).deleteById(TEST_ID);
    }

    @Test
    void deleteTagByIdTagNoSuchTagException() {
        given(tagRepository.findById(TEST_ID)).willReturn(Optional.empty());
        assertThrows(NoSuchTagException.class, () -> tagService.deleteTagById(TEST_ID));
    }

    @Test
    void deleteTagByIdTagValidationException() {
        final int ID = -1;
        assertThrows(InvalidDataException.class, () -> tagService.deleteTagById(ID));
    }

    @Test
    void getTagById() {
        given(tagRepository.findById(TEST_ID)).willReturn(Optional.of(tag));
        TagDTO tagDTO = tagService.getTagById(TEST_ID);
        assertEquals(tagDTO.getName(), TEST_NAME);
        assertEquals(tagDTO.getId(), TEST_ID);
    }

    @Test
    void getTagByIdNoSuchTagException() {
        given(tagRepository.findById(TEST_ID)).willReturn(Optional.empty());
        assertThrows(NoSuchTagException.class, () -> tagService.getTagById(TEST_ID));
    }

    @Test
    void getTagByIdInvalidDataException() {
        final int ID = -1;
        assertThrows(InvalidDataException.class, () -> tagService.getTagById(ID));
    }

    @Test
    void getTagByName() {
        given(tagRepository.findByName(TEST_NAME)).willReturn(Optional.of(tag));
        TagDTO tagDTO = tagService.getTagByName(TEST_NAME);
        assertEquals(tagDTO.getName(), TEST_NAME);
        assertEquals(tagDTO.getId(), TEST_ID);
    }

    @Test
    void getTagByNameNoSuchTagException() {
        given(tagRepository.findByName(TEST_NAME)).willReturn(Optional.empty());
        assertThrows(NoSuchTagException.class, () -> tagService.getTagByName(TEST_NAME));
    }

    @Test
    void getTagByNameInvalidDataException() {
        final String NAME = "";
        assertThrows(InvalidDataException.class, () -> tagService.getTagByName(NAME));
    }


    @Test
    void getTagListByGiftId() {
        Page<Tag> pageable = mock(Page.class);
        given(giftRepository.findById(TEST_ID)).willReturn(Optional.of(gift));
        given(tagRepository.findAll(any(Specification.class),any(Pageable.class))).willReturn(pageable);
        when(pageable.toList()).thenReturn(tagList);
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
        Page<Tag> pageable = mock(Page.class);
        given(tagRepository.findAll(any(Pageable.class))).willReturn(pageable);
        when(pageable.toList()).thenReturn(tagList);
        List<TagDTO> allTags = tagService.getAllTags(page);
        assertIterableEquals(tagDTOList, allTags);
    }
}