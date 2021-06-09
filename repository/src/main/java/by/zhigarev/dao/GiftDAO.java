package by.zhigarev.dao;

import by.zhigarev.model.Gift;
import by.zhigarev.model.Tag;

import java.util.List;
import java.util.Optional;

public interface GiftDAO {
    Gift createGift(Gift gift);

    void deleteGiftById(int id);

    Gift updateGiftById(String updateSQL, int id);

    Optional<Gift> getGiftById(int id);

    List<Gift> getGifts();

    List<Gift> getGiftsByParams(String getSql);

    void createGiftTag(int giftId, int tagId);
    void deleteGiftTagByGiftId(int id);

}
