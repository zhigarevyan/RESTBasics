package com.epam.esm.util;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.model.Order;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class OrderEntityToDTOMapper {
    public static OrderDTO toDTO(Order order){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderDTO.getId());
        orderDTO.setPrice(orderDTO.getPrice());
        orderDTO.setDate(LocalDateTime.ofInstant(order.getDate(), ZoneOffset.UTC));
        orderDTO.setGiftID(order.getGift().getId());
        orderDTO.setUserID(order.getUser().getId());
        return orderDTO;
    }

    public static List<OrderDTO> toDTO(List<Order> orderList){
        List<OrderDTO> orderDTOList = new ArrayList<>();
        orderList.forEach(order -> orderDTOList.add(toDTO(order)));
        return orderDTOList;
    }
}
