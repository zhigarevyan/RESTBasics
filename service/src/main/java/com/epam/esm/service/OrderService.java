package com.epam.esm.service;

import com.epam.esm.dao.GiftDAO;
import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.dto.GiftDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exeption.impl.InvalidDataException;
import com.epam.esm.exeption.impl.NoSuchGiftException;
import com.epam.esm.exeption.impl.NoSuchOrderException;
import com.epam.esm.exeption.impl.NoSuchUserException;
import com.epam.esm.model.Gift;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.util.OrderEntityToDTOMapper;
import com.epam.esm.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * An object of {@link OrderDAO}
     */
    private final OrderDAO orderDAO;
    /**
     * An object of {@link UserDAO}
     */
    private final UserDAO userDAO;
    /**
     * An object of {@link GiftDAO}
     */
    private final GiftDAO giftDAO;

    /**
     * Public constructor that receives tagDAO
     *
     * @param orderDAO is {@link OrderDAO} interface providing DAO methods.
     */
    @Autowired
    public OrderService(OrderDAO orderDAO, UserDAO userDAO, GiftDAO giftDAO) {
        this.orderDAO = orderDAO;
        this.userDAO = userDAO;
        this.giftDAO = giftDAO;
    }

    /**
     * Invokes DAO method to get Order with provided id.
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
        if (orderDAO.getOrderById(orderId).isEmpty()) {
            throw new NoSuchOrderException(String.format(MESSAGE_NO_SUCH_ORDER_WITH_ID_EXCEPTION, orderId),
                    String.format(ERROR_CODE_NO_SUCH_ORDER_BY_ID, orderId));
        }
        return OrderEntityToDTOMapper.toDTO(orderDAO.getOrderById(orderId).get());
    }

    /**
     * Invokes DAO method to get List of all Tags that linked with GiftCertificate by it's id
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
        if (userDAO.getUserById(userId).isEmpty()) {
            throw new NoSuchUserException(
                    String.format(MESSAGE_NO_SUCH_USER_EXCEPTION, userId),
                    String.format(ERROR_CODE_NO_SUCH_USER_EXCEPTION, userId));
        }
        return OrderEntityToDTOMapper.toDTO(orderDAO.getOrdersByUserId(userId));
    }
    /**
     * Invokes DAO method to create Order with provided data.
     *
     * @param orderDTO is {@link OrderDTO} object with Order data.
     * @return {@link OrderDTO} object with created data.
     * @throws InvalidDataException if data failed validation
     * @throws NoSuchGiftException if gift with provided id wasn't found
     * @throws NoSuchUserException if user with provided id wasn't found
     */
    public OrderDTO createOrder(OrderDTO orderDTO) {
        if (!Validator.isValidOrderDTO(orderDTO)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION);
        }
        Integer giftId = orderDTO.getGiftID();
        Optional<Gift> giftById = giftDAO.getGiftById(giftId);
        Gift gift = giftById.orElseThrow(() ->
                new NoSuchGiftException(
                        String.format(MESSAGE_NO_SUCH_GIFT_EXCEPTION, giftId),
                        String.format(ERROR_CODE_NO_SUCH_GIFT, giftId)));
        Integer userId = orderDTO.getUserID();
        Optional<User> userById = userDAO.getUserById(userId);
        User user = userById.orElseThrow(() ->
                new NoSuchUserException(
                        String.format(MESSAGE_NO_SUCH_USER_EXCEPTION, userId),
                        String.format(ERROR_CODE_NO_SUCH_USER_EXCEPTION, userId)));
        Order order = new Order();
        order.setUser(user);
        order.setGift(gift);
        order.setPrice(orderDTO.getPrice());
        return OrderEntityToDTOMapper.toDTO(orderDAO.createOrder(order));
    }
}
