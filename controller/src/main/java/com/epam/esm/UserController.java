package com.epam.esm;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.service.UserService;
import com.epam.esm.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserDTO> getUsers(@RequestBody @Valid Page page) {
        return userService.getAllUsers(page);
    }


}
