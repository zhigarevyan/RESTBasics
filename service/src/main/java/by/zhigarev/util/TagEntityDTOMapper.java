package by.zhigarev.util;

import by.zhigarev.dto.TagDTO;
import by.zhigarev.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagEntityDTOMapper {

    public static TagDTO toDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName(tag.getName());
        tagDTO.setId(tag.getId());
        return tagDTO;
    }

    public static List<TagDTO> toDTO(List<Tag> tagList) {
        List<TagDTO> tagDTOList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagDTOList.add(toDTO(tag));
        }
        return tagDTOList;
    }
}
