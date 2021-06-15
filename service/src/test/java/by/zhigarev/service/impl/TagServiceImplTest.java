package by.zhigarev.service.impl;

import by.zhigarev.dao.TagDAO;
import by.zhigarev.dto.TagDTO;
import by.zhigarev.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    @Mock
    private TagDAO tagDAO;
    @InjectMocks
    private TagServiceImpl tagService;

    private Tag tag;
    private List<Tag> tagList;
    private List<TagDTO> tagDTOList;
    private static final Integer TEST_ID = 1;
    private static final String TEST_NAME = "TEST TAG";


    @BeforeEach
    void setUp() {
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

        tagService = new TagServiceImpl(tagDAO);
    }

    @Test
    void createTag() {
        given(tagDAO.createTag(TEST_NAME)).willReturn(tag);
        TagDTO actualTagDTO = tagService.createTag(TEST_NAME);
        assertEquals(TEST_NAME, actualTagDTO.getName());
    }

    @Test
    void deleteTagById() {
        given(tagDAO.getTagById(TEST_ID)).willReturn(Optional.of(tag));
        tagService.deleteTagById(TEST_ID);
        verify(tagDAO, times(1)).deleteTagById(TEST_ID);
    }

    @Test
    void getTagById() {
        given(tagDAO.getTagById(TEST_ID)).willReturn(Optional.of(tag));
        TagDTO tagDTO = tagService.getTagById(TEST_ID);
        assertEquals(tagDTO.getName(), TEST_NAME);
        assertEquals(tagDTO.getId(), TEST_ID);
    }

    @Test
    void getTagByName() {
        given(tagDAO.getTagByName(TEST_NAME)).willReturn(Optional.of(tag));
        TagDTO tagDTO = tagService.getTagByName(TEST_NAME);
        assertEquals(tagDTO.getName(), TEST_NAME);
        assertEquals(tagDTO.getId(), TEST_ID);
    }

    @Test
    void getTagListByGiftId() {
        given(tagDAO.getTagListByGiftId(TEST_ID)).willReturn(tagList);
        List<TagDTO> tagListByGiftId = tagService.getTagListByGiftId(TEST_ID);
        assertIterableEquals(tagDTOList,tagListByGiftId);

    }

    @Test
    void getAllTags() {
        given(tagDAO.getAllTags()).willReturn(tagList);
        List<TagDTO> allTags = tagService.getAllTags();
        assertIterableEquals(tagDTOList,allTags);
    }
}