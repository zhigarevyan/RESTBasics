package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftDAO;
import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.exeption.impl.InvalidDataException;
import com.epam.esm.exeption.impl.NoSuchGiftException;
import com.epam.esm.exeption.impl.NoSuchOrderException;
import com.epam.esm.exeption.impl.NoSuchUserException;
import com.epam.esm.model.Gift;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.OrderEntityToDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderDAO orderDAO;
    @Mock
    private UserDAO userDAO;
    @Mock
    private GiftDAO giftDAO;
    @InjectMocks
    private OrderService orderService;

    private Order order;
    private Order orderToCreate;
    private OrderDTO orderDTO;
    private User user;
    private Gift gift;
    private List<Order> orderList;
    private static final int TEST_PRICE = 10;
    private static final int TEST_DURATION = 10;
    private static final int TEST_ID = 1;
    private static final String TEST_NAME = "TEST_NAME";
    private static final String TEST_DESCRIPTION = "TEST_DESCRIPTION";


    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderDAO, userDAO, giftDAO);
        Instant now = Instant.now();

        user = new User();
        user.setId(TEST_ID);
        user.setName(TEST_NAME);

        gift = new Gift();
        gift.setId(TEST_ID);
        gift.setName(TEST_NAME);
        gift.setPrice(TEST_PRICE);
        gift.setDuration(TEST_DURATION);
        gift.setDescription(TEST_DESCRIPTION);

        order = new Order();
        order.setPrice(TEST_PRICE);
        order.setId(TEST_ID);
        order.setGift(gift);
        order.setUser(user);
        order.setDate(now);

        orderDTO = new OrderDTO();
        orderDTO.setPrice(TEST_PRICE);
        orderDTO.setId(TEST_ID);
        orderDTO.setGiftID(TEST_ID);
        orderDTO.setUserID(TEST_ID);
        orderDTO.setDate(LocalDateTime.ofInstant(now, ZoneOffset.UTC));

        orderToCreate = new Order();
        orderToCreate.setUser(user);
        orderToCreate.setPrice(TEST_PRICE);
        orderToCreate.setGift(gift);

        orderList = new ArrayList<>();
        orderList.add(order);
    }

    @Test
    void getOrderById() {
        given(orderDAO.getOrderById(TEST_ID)).willReturn(Optional.of(order));
        OrderDTO orderById = orderService.getOrderById(TEST_ID);
        OrderDTO orderDTO = OrderEntityToDTOMapper.toDTO(order);
        assertEquals(orderDTO, orderById);
    }

    @Test
    void getOrderByIdInvalidDataException() {
        assertThrows(InvalidDataException.class, () -> orderService.getOrderById(-TEST_ID));
    }

    @Test
    void getOrderByIdNoSuchOrderException() {
        given(orderDAO.getOrderById(TEST_ID)).willReturn(Optional.empty());
        assertThrows(NoSuchOrderException.class, () -> orderService.getOrderById(TEST_ID));
    }

    @Test
    void getOrdersByUserId() {
        given(userDAO.getUserById(TEST_ID)).willReturn(Optional.of(user));
        given(orderDAO.getOrdersByUserId(TEST_ID)).willReturn(orderList);
        List<OrderDTO> ordersByUserId = orderService.getOrdersByUserId(TEST_ID);
        List<OrderDTO> orderDTOList = OrderEntityToDTOMapper.toDTO(orderList);
        assertEquals(orderDTOList,ordersByUserId);
    }

    @Test
    void getOrdersByUserIdInvalidDataException() {
        assertThrows(InvalidDataException.class,()->orderService.getOrdersByUserId(-TEST_ID));
    }

    @Test
    void getOrdersByUserNoSuchUserException() {
        given(userDAO.getUserById(TEST_ID)).willReturn(Optional.empty());
        assertThrows(NoSuchUserException.class,()->orderService.getOrdersByUserId(TEST_ID));
    }

    @Test
    void createOrder() {
        given(userDAO.getUserById(TEST_ID)).willReturn(Optional.of(user));
        given(giftDAO.getGiftById(TEST_ID)).willReturn(Optional.of(gift));
        given(orderDAO.createOrder(orderToCreate)).willReturn(order);
        OrderDTO createdOrder = orderService.createOrder(TEST_ID,TEST_ID);
        assertEquals(orderDTO,createdOrder);
    }

    @Test
    void createOrderInvalidDataException() {
        assertThrows(InvalidDataException.class,()-> orderService.createOrder(-TEST_ID,-TEST_ID));
    }

    @Test
    void createOrderNoSuchGiftException() {
        given(giftDAO.getGiftById(TEST_ID)).willReturn(Optional.empty());
        assertThrows(NoSuchGiftException.class,()-> orderService.createOrder(TEST_ID,TEST_ID));
    }

    @Test
    void createOrderNoSuchUserException() {
        given(giftDAO.getGiftById(TEST_ID)).willReturn(Optional.of(gift));
        given(userDAO.getUserById(TEST_ID)).willReturn(Optional.empty());
        assertThrows(NoSuchUserException.class,()-> orderService.createOrder(TEST_ID,TEST_ID));
    }
    @Test
    void getAllOrders(){
        given(orderDAO.getAllOrders()).willReturn(orderList);
        List<OrderDTO> allOrders = orderService.getAllOrders();
        List<OrderDTO> orderDTOList = OrderEntityToDTOMapper.toDTO(orderList);
        assertEquals(orderDTOList,allOrders);
    }
}