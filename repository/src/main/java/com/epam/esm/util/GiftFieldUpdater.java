package com.epam.esm.util;

import com.epam.esm.model.Gift;
import com.epam.esm.model.Tag;

import java.util.List;

public class GiftFieldUpdater {

    public static void update(Gift oldGift,Gift newGift){
        Integer price = newGift.getPrice();
        if (price != null) {
            oldGift.setPrice(price);
        }

        Integer duration = newGift.getDuration();
        if (duration != null) {
            oldGift.setDuration(duration);
        }

        String name = newGift.getName();
        if (name != null) {
            oldGift.setName(name);
        }

        String description = newGift.getDescription();
        if (description != null) {
            oldGift.setDescription(description);
        }

        List<Tag> tagList = newGift.getTagList();
        if (tagList != null) {
            oldGift.setTagList(tagList);
        }

    }
}
