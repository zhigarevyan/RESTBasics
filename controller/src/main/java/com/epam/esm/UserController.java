package com.epam.esm;

import com.epam.esm.dto.UserDTO;

import com.epam.esm.service.UserService;
import com.epam.esm.util.SignUpUserData;
import com.epam.esm.util.assembler.UserModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    @Autowired
    public UserController(UserService userService, UserModelAssembler userModelAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<UserDTO> signUp(@RequestBody SignUpUserData signUpUserData) {
        return userModelAssembler.toModel(userService.signUp(signUpUserData));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public EntityModel<UserDTO> getUserById(@PathVariable int id) {
        return userModelAssembler.toModel(userService.getUser(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<EntityModel<UserDTO>> getUsers(@Valid Pageable page) {
        return userModelAssembler.toModel(userService.getUsers(page));
    }


}
