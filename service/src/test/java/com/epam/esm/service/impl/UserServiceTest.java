package com.epam.esm.service.impl;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.exeption.impl.NoSuchUserException;
import com.epam.esm.model.User;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.util.UserEntityToDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

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
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private String TEST_NAME = "TEST_NAME";
    private int TEST_ID = 1;
    private User user;
    private List<User> userList;
    private Pageable page;

    @BeforeEach
    void setUp() {
        page = Pageable.unpaged();

        user = new User();
        user.setId(TEST_ID);
        user.setName(TEST_NAME);

        userList = new ArrayList<>();
        userList.add(user);

        userService = new UserService(userRepository,roleRepository,passwordEncoder);
    }

    @Test
    void getUserById() {
        given(userRepository.findById(TEST_ID)).willReturn(Optional.of(user));
        UserDTO userByID = userService.getUser(TEST_ID);
        UserDTO userDTO = UserEntityToDTOMapper.toDTO(user);
        assertEquals(userDTO, userByID);
    }

    @Test
    void getUserByIdNoSuchUserException() {
        given(userRepository.findById(TEST_ID)).willReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, () -> userService.getUser(TEST_ID));
    }

    @Test
    void getAllUsers() {
        Page<User> pageable = mock(Page.class);
        given(userRepository.findAll(any(Pageable.class))).willReturn(pageable);
        when(pageable.toList()).thenReturn(userList);
        List<UserDTO> allUsers = userService.getUsers(page);
        List<UserDTO> userDTOList = UserEntityToDTOMapper.toDTO(userList);
        assertEquals(userDTOList, allUsers);
    }
}