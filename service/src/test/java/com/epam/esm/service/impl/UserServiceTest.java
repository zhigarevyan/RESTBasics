package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exeption.impl.NoSuchUserException;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import com.epam.esm.util.Page;
import com.epam.esm.util.UserEntityToDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDAO userDAO;
    @InjectMocks
    private UserService userService;

    private String TEST_NAME = "TEST_NAME";
    private int TEST_ID = 1;
    private User user;
    private List<User> userList;
    private Page page;

    @BeforeEach
    void setUp() {
        page = Page.getDefaultPage();

        user = new User();
        user.setId(TEST_ID);
        user.setName(TEST_NAME);

        userList = new ArrayList<>();
        userList.add(user);

        userService = new UserService(userDAO);
    }

    @Test
    void getUserById() {
        given(userDAO.getUserById(TEST_ID)).willReturn(Optional.of(user));
        UserDTO userByID = userService.getUserById(TEST_ID);
        UserDTO userDTO = UserEntityToDTOMapper.toDTO(user);
        assertEquals(userDTO, userByID);
    }

    @Test
    void getUserByIdNoSuchUserException() {
        given(userDAO.getUserById(TEST_ID)).willReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, () -> userService.getUserById(TEST_ID));
    }

    @Test
    void getAllUsers() {
        given(userDAO.getAllUsers(page.getPage(), page.getSize())).willReturn(userList);
        List<UserDTO> allUsers = userService.getAllUsers(page);
        List<UserDTO> userDTOList = UserEntityToDTOMapper.toDTO(userList);
        assertEquals(userDTOList, allUsers);
    }
}