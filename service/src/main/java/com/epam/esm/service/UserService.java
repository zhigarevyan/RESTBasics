package com.epam.esm.service;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exeption.impl.InvalidDataException;
import com.epam.esm.exeption.impl.NoSuchUserException;
import com.epam.esm.util.UserEntityToDTOMapper;
import com.epam.esm.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    /**
     * Error message when User wasn't found by id
     */
    private static final String MESSAGE_NO_SUCH_USER_EXCEPTION = "No such user with id = %d";
    /**
     * Error code when User wasn't found by id
     */
    private static final String ERROR_CODE_NO_SUCH_USER_EXCEPTION = "0302404_%d";
    /**
     * Error code when data failed validation
     */
    private static final String ERROR_CODE_INVALID_DATA = "0301";
    /**
     * Error message when data failed validation
     */
    private static final String MESSAGE_INVALID_DATA_EXCEPTION = "Invalid data";
    /**
     * An object of {@link UserDAO}
     */
    private final UserDAO userDAO;
    /**
     * Public constructor that receives tagDAO
     *
     * @param userDAO is {@link UserDAO} interface providing DAO methods.
     */
    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    /**
     * Invokes DAO method to get User with provided id.
     *
     * @param userId is id of tag to be deleted.
     * @throws NoSuchUserException   if no User with provided name founded
     * @throws InvalidDataException if data failed validation
     * @return {@link UserDTO} object with User data.
     */
    public UserDTO getUserById(int userId){
        if(!Validator.isValidNumber(userId)){
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION,ERROR_CODE_INVALID_DATA);
        }
        if(userDAO.getUserById(userId).isEmpty()){
            throw new NoSuchUserException(String.format(MESSAGE_NO_SUCH_USER_EXCEPTION,userId),
                    String.format(ERROR_CODE_NO_SUCH_USER_EXCEPTION,userId));
        }
        return UserEntityToDTOMapper.toDTO(userDAO.getUserById(userId).get());
    }
    /**
     * Invokes DAO method to get List of all Users from database.
     *
     * @return List of {@link UserDTO} objects with tag data.
     */
    public List<UserDTO> getAllUsers(){
        return UserEntityToDTOMapper.toDTO(userDAO.getAllUsers());
    }
}
