package com.epam.esm.util;

import com.epam.esm.dto.GiftDTO;
import com.epam.esm.dto.OrderDTO;

/**
 * Class is Validator that validates received data
 */
public class Validator {

    /**
     * Validates String
     *
     * @param string is string parameter
     * @return true if data is OK, false if data failed validation
     */
    public static boolean isValidString(String string) {
        return string != null && !string.isEmpty();
    }

    /**
     * Validates number
     *
     * @param number is number parameter
     * @return true if data is OK, false if data failed validation
     */
    public static boolean isValidNumber(int number) {
        return number > 0;
    }

    /**
     * Validates GiftDTO
     *
     * @param gift is {@link GiftDTO} instance of giftDTO
     * @return true if data is OK, false if data failed validation
     */
    public static boolean isValidGiftDto(GiftDTO gift) {
        return isValidString(gift.getName()) &&
                isValidString(gift.getDescription()) &&
                isValidNumber(gift.getDuration()) &&
                isValidNumber(gift.getPrice());
    }


    public static boolean isValidOrderDTO(OrderDTO orderDTO) {
        return isValidNumber(orderDTO.getPrice())&&
                isValidNumber(orderDTO.getGiftID())&&
                isValidNumber(orderDTO.getUserID());

    }
}
