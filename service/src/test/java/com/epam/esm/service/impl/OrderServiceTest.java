package com.epam.esm.service.impl;


import com.epam.esm.dto.GiftDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserInOrderDTO;
import com.epam.esm.exeption.impl.InvalidDataException;
import com.epam.esm.exeption.impl.NoSuchGiftException;
import com.epam.esm.exeption.impl.NoSuchOrderException;
import com.epam.esm.exeption.impl.NoSuchUserException;
import com.epam.esm.model.Gift;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.repository.GiftRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.CreateOrderParameter;
import com.epam.esm.util.OrderEntityToDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GiftRepository giftRepository;
    @InjectMocks
    private OrderService orderService;

    private Order order;
    private Order orderToCreate;
    private OrderDTO orderDTO;
    private User user;
    private Gift gift;
    private List<Order> orderList;
    private List<Gift> giftList;
    private Pageable page;
    private CreateOrderParameter createOrderParameter;
    private static final int TEST_PRICE = 10;
    private static final int TEST_DURATION = 10;
    private static final int TEST_ID = 1;
    private static final String TEST_NAME = "TEST_NAME";
    private static final String TEST_DESCRIPTION = "TEST_DESCRIPTION";


    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, userRepository, giftRepository);
        Instant now = Instant.now();

        page = Pageable.unpaged();

        user = new User();
        user.setId(TEST_ID);
        user.setName(TEST_NAME);

        UserInOrderDTO userInOrderDTO = new UserInOrderDTO();
        userInOrderDTO.setId(TEST_ID);
        userInOrderDTO.setName(TEST_NAME);


        gift = new Gift();
        gift.setId(TEST_ID);
        gift.setName(TEST_NAME);
        gift.setPrice(TEST_PRICE);
        gift.setDuration(TEST_DURATION);
        gift.setDescription(TEST_DESCRIPTION);
        gift.setCreateDate(now);
        gift.setLastUpdateDate(now);


        GiftDTO giftDTO = new GiftDTO();
        giftDTO.setId(TEST_ID);
        giftDTO.setName(TEST_NAME);
        giftDTO.setPrice(TEST_PRICE);
        giftDTO.setDuration(TEST_DURATION);
        giftDTO.setDescription(TEST_DESCRIPTION);
        giftDTO.setCreateDate(LocalDateTime.ofInstant(now,ZoneOffset.UTC));
        giftDTO.setLastUpdateDate(LocalDateTime.ofInstant(now,ZoneOffset.UTC));

        giftList = new ArrayList<>();
        giftList.add(gift);

        List<GiftDTO> giftDTOList = new ArrayList<>();
        giftDTOList.add(giftDTO);

        order = new Order();
        order.setPrice(TEST_PRICE);
        order.setId(TEST_ID);
        order.setGiftList(giftList);
        order.setUser(user);
        order.setDate(now);

        orderDTO = new OrderDTO();
        orderDTO.setPrice(TEST_PRICE);
        orderDTO.setId(TEST_ID);
        orderDTO.setGifts(giftDTOList);
        orderDTO.setUser(userInOrderDTO);
        orderDTO.setDate(LocalDateTime.ofInstant(now, ZoneOffset.UTC));

        orderToCreate = new Order();
        orderToCreate.setUser(user);
        orderToCreate.setPrice(TEST_PRICE);
        orderToCreate.setGiftList(giftList);

        orderList = new ArrayList<>();
        orderList.add(order);

        createOrderParameter = new CreateOrderParameter();
        createOrderParameter.setUser(TEST_ID);

        List<Integer> giftIdList = new ArrayList<>();
        giftList.forEach(gift1 -> giftIdList.add(gift1.getId()));
        createOrderParameter.setGifts(giftIdList);


    }

    @Test
    void getOrderById() {
        given(orderRepository.findById(TEST_ID)).willReturn(Optional.of(order));
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
        given(orderRepository.findById(TEST_ID)).willReturn(Optional.empty());
        assertThrows(NoSuchOrderException.class, () -> orderService.getOrderById(TEST_ID));
    }

    @Test
    void getOrdersByUserId() {
        given(userRepository.findById(TEST_ID)).willReturn(Optional.of(user));
        given(orderRepository.findByUserId(TEST_ID)).willReturn(orderList);
        List<OrderDTO> ordersByUserId = orderService.getOrdersByUserId(TEST_ID);
        List<OrderDTO> orderDTOList = OrderEntityToDTOMapper.toDTO(orderList);
        assertEquals(orderDTOList, ordersByUserId);
    }

    @Test
    void getOrdersByUserIdInvalidDataException() {
        assertThrows(InvalidDataException.class, () -> orderService.getOrdersByUserId(-TEST_ID));
    }

    @Test
    void getOrdersByUserNoSuchUserException() {
        given(userRepository.findById(TEST_ID)).willReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, () -> orderService.getOrdersByUserId(TEST_ID));
    }

    @Test
    void createOrder() {
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(giftRepository.findById(any())).willReturn(Optional.of(gift));
        given(orderRepository.save(orderToCreate)).willReturn(order);
        OrderDTO createdOrder = orderService.createOrder(createOrderParameter);
        assertEquals(orderDTO, createdOrder);
    }

    @Test
    void createOrderInvalidDataException() {
        List<Integer> giftIdList = new ArrayList<>();
        giftList.forEach(gift1 -> giftIdList.add(-gift1.getId()));
        createOrderParameter.setGifts(giftIdList);
        createOrderParameter.setUser(-user.getId());

        assertThrows(InvalidDataException.class, () -> orderService.createOrder(createOrderParameter));
    }

    @Test
    void createOrderNoSuchGiftException() {
        given(giftRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(NoSuchGiftException.class, () -> orderService.createOrder(createOrderParameter));
    }

    @Test
    void createOrderNoSuchUserException() {
        given(giftRepository.findById(any())).willReturn(Optional.of(gift));
        given(userRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, () -> orderService.createOrder(createOrderParameter));
    }

    @Test
    void getAllOrders() {
        Page<Order> pageable = mock(Page.class) ;
        given(orderRepository.findAll(any(Pageable.class))).willReturn(pageable);
        when(pageable.toList()).thenReturn(orderList);
        List<OrderDTO> allOrders = orderService.getAllOrders(page);
        List<OrderDTO> orderDTOList = OrderEntityToDTOMapper.toDTO(orderList);
        assertEquals(orderDTOList, allOrders);
    }
}