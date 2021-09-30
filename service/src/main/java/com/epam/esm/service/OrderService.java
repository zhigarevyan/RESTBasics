package com.epam.esm.service;


import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
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
import com.epam.esm.util.CreateOrderParameter;
import com.epam.esm.util.OrderEntityToDTOMapper;
import com.epam.esm.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    /**
     * Error message when data failed validation
     */
    private static final String MESSAGE_INVALID_DATA_EXCEPTION = "Invalid data";
    /**
     * Error message when Tag wasn't found by id
     */
    private static final String MESSAGE_NO_SUCH_ORDER_WITH_ID_EXCEPTION = "No such order with id - %d exception";
    /**
     * Error code when Tag wasn't found by id
     */
    private static final String ERROR_CODE_NO_SUCH_ORDER_BY_ID = "0402404_%d";
    /**
     * Error code when data failed validation
     */
    private static final String ERROR_CODE_INVALID_DATA = "0401";
    /**
     * Error message when User wasn't found by id
     */
    private static final String MESSAGE_NO_SUCH_USER_EXCEPTION = "No such user with id = %d";
    /**
     * Error code when User wasn't found by id
     */
    private static final String ERROR_CODE_NO_SUCH_USER_EXCEPTION = "0302404_%d";
    /**
     * Error message when Gift wasn't found by id
     */
    private static final String MESSAGE_NO_SUCH_GIFT_EXCEPTION = "No such gift with id - %d exception";
    /**
     * Error code when Gift wasn't found by id
     */
    private static final String ERROR_CODE_NO_SUCH_GIFT = "0102404_%d";
    /**
     * An object of {@link OrderRepository}
     */
    private final OrderRepository orderRepository;
    /**
     * An object of {@link UserRepository}
     */
    private final UserRepository userRepository;
    /**
     * An object of {@link GiftRepository}
     */
    private final GiftRepository giftRepository;

    /**
     * Public constructor that receives tagRepository
     *
     * @param orderRepository is {@link OrderRepository} interface providing Repository methods.
     */
    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        GiftRepository giftRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.giftRepository = giftRepository;
    }

    /**
     * Invokes Repository method to get Order with provided id.
     *
     * @param orderId is id of Order to be returned.
     * @return {@link OrderDTO} object with Order data.
     * @throws NoSuchOrderException if no Order with provided id founded
     * @throws InvalidDataException if data failed validation
     */
    public OrderDTO getOrderById(int orderId) {
        if (!Validator.isValidNumber(orderId)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION, ERROR_CODE_INVALID_DATA);
        }
        if (orderRepository.findById(orderId).isEmpty()) {
            throw new NoSuchOrderException(String.format(MESSAGE_NO_SUCH_ORDER_WITH_ID_EXCEPTION, orderId),
                    String.format(ERROR_CODE_NO_SUCH_ORDER_BY_ID, orderId));
        }
        return OrderEntityToDTOMapper.toDTO(orderRepository.findById(orderId).get());
    }

    /**
     * Invokes Repository method to get List of all Tags that linked with GiftCertificate by it's id
     *
     * @param userId is id of GiftCertificate.
     * @return List of {@link TagDTO} objects with tag data.
     * @throws NoSuchUserException  if no User with provided id founded
     * @throws InvalidDataException if data failed validation
     */
    public List<OrderDTO> getOrdersByUserId(int userId) {
        if (!Validator.isValidNumber(userId)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION);
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new NoSuchUserException(
                    String.format(MESSAGE_NO_SUCH_USER_EXCEPTION, userId),
                    String.format(ERROR_CODE_NO_SUCH_USER_EXCEPTION, userId));
        }
        return OrderEntityToDTOMapper.toDTO(orderRepository.findByUserId(userId));
    }

    /**
     * Invokes Repository method to create Order with provided data.
     *
     * @param parameter is {@link CreateOrderParameter} object with Order data.
     * @return {@link OrderDTO} object with created data.
     * @throws InvalidDataException if data failed validation
     * @throws NoSuchGiftException  if gift with provided id wasn't found
     * @throws NoSuchUserException  if user with provided id wasn't found
     */
    @Transactional
    public OrderDTO createOrder(CreateOrderParameter parameter) {
        int orderPrice = 0;
        List<Gift> giftList = new ArrayList<>();
        Integer userId = parameter.getUser();
        List<Integer> giftIds = parameter.getGifts();

        if (!Validator.isValidCreateOrderParameter(parameter)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION);
        }
        for (Integer giftId : giftIds) {
            Optional<Gift> giftById = giftRepository.findById(giftId);
            Gift gift = giftById.orElseThrow(() ->
                    new NoSuchGiftException(
                            String.format(MESSAGE_NO_SUCH_GIFT_EXCEPTION, giftId),
                            String.format(ERROR_CODE_NO_SUCH_GIFT, giftId)));
            orderPrice+=gift.getPrice();
            giftList.add(gift);
        }
        Optional<User> userById = userRepository.findById(userId);
        User user = userById.orElseThrow(() ->
                new NoSuchUserException(
                        String.format(MESSAGE_NO_SUCH_USER_EXCEPTION, userId),
                        String.format(ERROR_CODE_NO_SUCH_USER_EXCEPTION, userId)));
        Order order = new Order();
        order.setUser(user);
        order.setGiftList(giftList);
        order.setPrice(orderPrice);
        order.setDate(Instant.now());

        return OrderEntityToDTOMapper.toDTO(orderRepository.save(order));
    }

    /**
     * Invokes Repository method to get List of all Orders from database.
     *
     * @return List of {@link OrderDTO} objects with order data.
     */
    public List<OrderDTO> getAllOrders(Pageable page) {
        return OrderEntityToDTOMapper.toDTO(orderRepository.findAll(page).toList());
    }
}
