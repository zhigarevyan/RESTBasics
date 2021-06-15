package by.zhigarev.util;

import by.zhigarev.dto.GiftDTO;
import by.zhigarev.model.Gift;

import java.util.ArrayList;
import java.util.List;


public class GiftEntityDTOMapper {

    public static GiftDTO toDTO(Gift gift) {
        GiftDTO giftDTO = new GiftDTO();
        giftDTO.setId(gift.getId());
        giftDTO.setName(gift.getName());
        giftDTO.setDescription(gift.getDescription());
        giftDTO.setDuration(gift.getDuration());
        giftDTO.setPrice(gift.getPrice());
        giftDTO.setCreateDate(gift.getCreateDate());
        giftDTO.setLastUpdateDate(gift.getLastUpdateDate());

        return giftDTO;
    }


    public static Gift toEntity(GiftDTO giftDTO) {
        Gift gift = new Gift();
        if(giftDTO.getId()!=null)
        gift.setId(giftDTO.getId());
        if(giftDTO.getName()!=null)
        gift.setName(giftDTO.getName());
        if(giftDTO.getDescription()!=null)
        gift.setDescription(giftDTO.getDescription());
        if(giftDTO.getDuration()!=null)
        gift.setDuration(giftDTO.getDuration());
        if(giftDTO.getPrice()!=null)
        gift.setPrice(giftDTO.getPrice());
        if(giftDTO.getCreateDate()!=null)
        gift.setCreateDate(giftDTO.getCreateDate());
        if(giftDTO.getLastUpdateDate()!=null)
        gift.setLastUpdateDate(giftDTO.getLastUpdateDate());
        return gift;

    }

    public static List<GiftDTO> toDTO(List<Gift> gifts) {
        List<GiftDTO> dtoList = new ArrayList<>();
        for (Gift gift : gifts) {
            dtoList.add(toDTO(gift));
        }
        return dtoList;
    }
}
