package com.epam.esm.util;

import com.epam.esm.dto.GiftDTO;
import com.epam.esm.model.Gift;
import com.epam.esm.model.Tag;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * Class is Mapper that links Gifts Entities with DTOs.
 */
public class GiftEntityDTOMapper {
    /**
     * Transforms Entity to DTO
     *
     * @param gift is {@link Gift} object with data to transform
     * @return transformed to {@link GiftDTO} data.
     */
    public static GiftDTO toDTO(Gift gift) {
        GiftDTO giftDTO = new GiftDTO();
        giftDTO.setId(gift.getId());
        giftDTO.setName(gift.getName());
        giftDTO.setDescription(gift.getDescription());
        giftDTO.setDuration(gift.getDuration());
        giftDTO.setPrice(gift.getPrice());
        giftDTO.setCreateDate(LocalDateTime.ofInstant(gift.getCreateDate(), ZoneOffset.UTC));
        giftDTO.setLastUpdateDate(LocalDateTime.ofInstant(gift.getLastUpdateDate(), ZoneOffset.UTC));
        List<Tag> tagList = gift.getTagList();
        if (tagList != null) {
            List<String> tagNamesList = new ArrayList<>();

            tagList.forEach(tag -> tagNamesList.add(tag.getName()));
            giftDTO.setTagList(tagNamesList);
        }


        return giftDTO;
    }

    /**
     * Transforms DTO to Entity
     *
     * @param giftDTO is {@link GiftDTO} object with data to transform
     * @return transformed to {@link Gift} entity.
     */
    public static Gift toEntity(GiftDTO giftDTO) {
        Gift gift = new Gift();
        if (giftDTO.getId() != null)
            gift.setId(giftDTO.getId());
        if (giftDTO.getName() != null)
            gift.setName(giftDTO.getName());
        if (giftDTO.getDescription() != null)
            gift.setDescription(giftDTO.getDescription());
        if (giftDTO.getDuration() != null)
            gift.setDuration(giftDTO.getDuration());
        if (giftDTO.getPrice() != null)
            gift.setPrice(giftDTO.getPrice());
        if (giftDTO.getCreateDate() != null)
            gift.setCreateDate(giftDTO.getCreateDate().toInstant(ZoneOffset.UTC));
        if (giftDTO.getLastUpdateDate() != null)
            gift.setLastUpdateDate(giftDTO.getLastUpdateDate().toInstant(ZoneOffset.UTC));
        return gift;

    }

    /**
     * Transforms List of Entities to List of DTOs
     *
     * @param gifts is List of {@link Gift} object with data to transform
     * @return transformed to List of {@link GiftDTO} data.
     */
    public static List<GiftDTO> toDTO(List<Gift> gifts) {
        List<GiftDTO> dtoList = new ArrayList<>();
        gifts.forEach(gift -> dtoList.add(toDTO(gift)));
        return dtoList;
    }

}
