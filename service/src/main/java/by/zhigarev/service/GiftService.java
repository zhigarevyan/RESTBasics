package by.zhigarev.service;

import by.zhigarev.dto.GiftDTO;
import by.zhigarev.util.GiftSQLQueryParameters;

import java.util.List;

public interface GiftService {
    GiftDTO createGift(GiftDTO gift);

    void deleteGiftById(int id);

    GiftDTO updateGiftById(GiftDTO giftDTO, int id);

    GiftDTO getGiftById(int id);

    List<GiftDTO> getGifts();

    List<GiftDTO> getGiftsByParams(GiftSQLQueryParameters params);

    void createGiftTag(int giftId, int tagId);
    void deleteGiftTagByGiftId(int id);
}
