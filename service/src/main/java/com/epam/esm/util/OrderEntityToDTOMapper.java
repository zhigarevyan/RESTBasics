package com.epam.esm.util;

import com.epam.esm.dto.GiftDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserInOrderDTO;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class OrderEntityToDTOMapper {
    public static OrderDTO toDTO(Order order) {
        OrderDTO orderDto = new OrderDTO();

        orderDto.setId(order.getId());

        UserInOrderDTO userInOrder = toUserInOrder(order.getUser());
        orderDto.setUser(userInOrder);

        List<GiftDTO> giftCertificateDtoList = GiftEntityDTOMapper.toDTO(order.getGiftList());
        orderDto.setGifts(giftCertificateDtoList);

        orderDto.setPrice(order.getPrice());
        orderDto.setDate(LocalDateTime.ofInstant(order.getDate(),ZoneOffset.UTC));

        return orderDto;
    }

    private static UserInOrderDTO toUserInOrder(User user) {
        UserInOrderDTO userInOrder = new UserInOrderDTO();

        userInOrder.setId(user.getId());
        userInOrder.setName(user.getName());

        return userInOrder;
    }

    public static List<OrderDTO> toDTO(List<Order> orderList) {
        List<OrderDTO> orderDTOList = new ArrayList<>();
        orderList.forEach(order -> orderDTOList.add(toDTO(order)));
        return orderDTOList;
    }
}
