package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.exeption.impl.DuplicateUserException;
import com.epam.esm.exeption.impl.NoSuchUserException;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.SignUpUserData;
import com.epam.esm.util.UserEntityToDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService {

    /**
     * Format string to provide info by what id User wasn't found.
     */
    private static final String NOT_FOUND_BY_ID_PARAMETER = "id: %d";

    /**
     * Error message when User wasn't found by id.
     */
    private static final String NO_USER_WITH_ID_FOUND = "No user with id: %d found";

    /**
     * Error message when User with provided login already exists.
     */
    private static final String USER_BY_LOGIN_ALREADY_EXISTS = "User with login: %s already exists";

    /**
     * Error code when User with provided login already exists.
     */
    private static final String ERROR_CODE_USER_BY_LOGIN_ALREADY_EXISTS = "0304";

    /**
     * Error message when User wasn't found by login.
     */
    private static final String NO_USER_WITH_LOGIN_FOUND = "No user with login: %s found";

    /**
     * Error code when User wasn't found by id.
     */
    private static final String ERROR_CODE_USER_BY_ID_NOT_FOUND_FAILED = "0302404%d";

    /**
     * An object of {@link UserRepository}.
     */
    private final UserRepository userRepository;

    /**
     * An object of {@link RoleRepository}.
     */
    private final RoleRepository roleRepository;

    /**
     * An object of {@link PasswordEncoder}.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Public constructor that receives userRepository
     *
     * @param userRepository  is {@link UserRepository} interface providing Repository methods.
     * @param roleRepository  is {@link RoleRepository} interface providing Repository methods.
     * @param passwordEncoder is {@link PasswordEncoder} object providing password encoding methods
     */
    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Invokes Repository method to get User with provided id.
     *
     * @param id is id of user to be returned.
     * @return {@link UserDTO} object with user data.
     * @throws NoSuchUserException if no User with provided id founded
     */
    public UserDTO getUser(int id) {
        Optional<User> optionalUser = userRepository.findById(id);

        User user = optionalUser.orElseThrow(() -> new NoSuchUserException(
                String.format(NO_USER_WITH_ID_FOUND, id),
                String.format(ERROR_CODE_USER_BY_ID_NOT_FOUND_FAILED, id)));

        return UserEntityToDTOMapper.toDTO(user);
    }

    /**
     * Invokes Repository method to sign up new user.
     *
     * @param signUpUserData is {@link SignUpUserData} object with user data.
     * @return {@link UserDTO} object with user data.
     * @throws DuplicateUserException if user with provided login already exists.
     */

    @Transactional
    public UserDTO signUp(SignUpUserData signUpUserData) {

        Optional<User> userByLogin = userRepository.findByLogin(signUpUserData.getLogin());
        if (userByLogin.isPresent()) {
            throw new DuplicateUserException(USER_BY_LOGIN_ALREADY_EXISTS, ERROR_CODE_USER_BY_LOGIN_ALREADY_EXISTS);
        }

        User user = new User();

        user.setName(signUpUserData.getName());
        user.setLogin(signUpUserData.getLogin());
        user.setPassword(passwordEncoder.encode(signUpUserData.getPassword()));

        Role role = roleRepository.findRoleByName(Role.RoleNames.USER);
        user.setRole(role);

        return UserEntityToDTOMapper.toDTO(userRepository.saveAndFlush(user));
    }

    /**
     * Invokes Repository method to get List of all Users from database.
     *
     * @param pageable is {@link Pageable} object with page number and page size
     * @return List of {@link UserDTO} objects with user data.
     */
    public List<UserDTO> getUsers(Pageable pageable) {
        return UserEntityToDTOMapper.toDTO(userRepository.findAll(pageable).toList());
    }

    /**
     * Invokes Repository method to get User with provided login.
     *
     * @param login is login of user to be returned.
     * @return {@link UserDTO} object with user data.
     * @throws NoSuchUserException if no User with provided id founded
     */
    public UserDetails loadUserByUsername(String login) throws NoSuchUserException {
        Optional<User> optionalUser = userRepository.findByLogin(login);

        User user = optionalUser.orElseThrow(() -> new NoSuchUserException(String.format(NO_USER_WITH_LOGIN_FOUND,
                login), ERROR_CODE_USER_BY_ID_NOT_FOUND_FAILED));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getRole().getName())
                .build();
    }
}
