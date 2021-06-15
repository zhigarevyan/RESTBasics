package by.zhigarev.service;

import by.zhigarev.dto.TagDTO;

import java.util.List;

public interface TagService {
    TagDTO createTag(String name);

    void deleteTagById(int id);

    TagDTO getTagById(int id);

    TagDTO getTagByName(String name);

    List<TagDTO> getTagListByGiftId(int giftId);

    List<TagDTO> getAllTags();
}
