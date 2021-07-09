package com.epam.esm;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.service.UserService;
import com.epam.esm.util.Page;
import com.epam.esm.util.assembler.UserModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
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


    @GetMapping("/{id}")
    public EntityModel<UserDTO> getUserById(@PathVariable int id) {
        return userModelAssembler.toModel(userService.getUserById(id));
    }

    @GetMapping
    public List<EntityModel<UserDTO>> getUsers(@Valid Page page) {
        return userModelAssembler.toModel(userService.getAllUsers(page));
    }


}
