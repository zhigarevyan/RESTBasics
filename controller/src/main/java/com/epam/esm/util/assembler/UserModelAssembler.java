package com.epam.esm.util.assembler;

import com.epam.esm.OrderController;
import com.epam.esm.UserController;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.util.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDTO,
        EntityModel<UserDTO>> {

    @Override
    public EntityModel<UserDTO> toModel(UserDTO userDTO) {
        return EntityModel.of(userDTO,
                linkTo(methodOn(UserController.class).getUserById(userDTO.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getUsers(Page.getDefaultPage())).withRel("Users"),
                linkTo(methodOn(OrderController.class).getOrdersByUserId(userDTO.getId())).withRel("Orders"));
    }

    public List<EntityModel<UserDTO>> toModel(List<UserDTO> userDTO) {
        return userDTO.stream().map(this::toModel).collect(Collectors.toList());
    }
}