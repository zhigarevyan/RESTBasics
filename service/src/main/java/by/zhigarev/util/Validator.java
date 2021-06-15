package by.zhigarev.util;

import by.zhigarev.dto.GiftDTO;

public class Validator {


    public static boolean isValidString(String string) {
        return string != null && !string.isEmpty();
    }

    public static boolean isValidNumber(int number) {
        return number > 0;
    }

    public static boolean isValidGiftDto(GiftDTO gift) {
        return isValidString(gift.getName()) &&
                isValidString(gift.getDescription()) &&
                isValidNumber(gift.getDuration()) &&
                isValidNumber(gift.getPrice());
    }


}
