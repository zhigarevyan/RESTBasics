package com.epam.esm.util;

import com.epam.esm.dto.GiftDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserOrderDTO;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserEntityToDTOMapper {

    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userDTO.getId());
        userDTO.setName(userDTO.getName());
        return userDTO;
    }

    public static List<UserDTO> toDTO(List<User> userList) {
        List<UserDTO> userDTOList = new ArrayList<>();
        userList.forEach(user -> userDTOList.add(toDTO(user)));
        return userDTOList;
    }

    private static List<UserOrderDTO> toUserOrder(List<Order> orders) {
        if(orders != null) {
            return orders.stream().map(UserEntityToDTOMapper::toUserOrder).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private static UserOrderDTO toUserOrder(Order order) {
        UserOrderDTO userOrder = new UserOrderDTO();

        userOrder.setId(order.getId());

        List<GiftDTO> giftCertificateDtoList = GiftEntityDTOMapper.toDTO(order.getGiftList());
        userOrder.setGifts(giftCertificateDtoList);

        userOrder.setPrice(order.getPrice());
        userOrder.setDate(LocalDateTime.ofInstant(order.getDate(), ZoneOffset.UTC));

        return userOrder;
    }
}
