package com.epam.esm.util.assembler;

import com.epam.esm.OrderController;
import com.epam.esm.UserController;
import com.epam.esm.dto.OrderDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<OrderDTO,
        EntityModel<OrderDTO>> {

    @Override
    public EntityModel<OrderDTO> toModel(OrderDTO orderDTO) {
        return EntityModel.of(orderDTO,
                linkTo(methodOn(OrderController.class).getOrderById(orderDTO.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllOrders(Pageable.unpaged())).withRel("Orders"),
                linkTo(methodOn(OrderController.class).getGiftCertificateListByOrderID(orderDTO.getId(),Pageable.unpaged())).withRel(
                        "Gifts"),
                linkTo(methodOn(UserController.class).getUserById(orderDTO.getUser().getId())).withRel("User"));
    }

    public List<EntityModel<OrderDTO>> toModel(List<OrderDTO> orderDTO) {
        return orderDTO.stream().map(this::toModel).collect(Collectors.toList());
    }

}