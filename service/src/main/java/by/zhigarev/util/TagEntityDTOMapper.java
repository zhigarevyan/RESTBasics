package by.zhigarev.util;

import by.zhigarev.dto.TagDTO;
import by.zhigarev.model.Tag;

import java.util.ArrayList;
import java.util.List;
/**
 * Class is Mapper that links Tag Entities with DTOs.
 */
public class TagEntityDTOMapper {
    /**
     * Transforms Entity to DTO
     *
     * @param tag is {@link Tag} object with data to transform
     * @return transformed to {@link TagDTO} data.
     */
    public static TagDTO toDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName(tag.getName());
        tagDTO.setId(tag.getId());
        return tagDTO;
    }
    /**
     * Transforms List of Entities to List of DTOs
     *
     * @param tagList is List of {@link Tag} object with data to transform
     * @return transformed to List of {@link TagDTO} data.
     */
    public static List<TagDTO> toDTO(List<Tag> tagList) {
        List<TagDTO> tagDTOList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagDTOList.add(toDTO(tag));
        }
        return tagDTOList;
    }
}
