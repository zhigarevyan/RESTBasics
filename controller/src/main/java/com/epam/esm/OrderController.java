package com.epam.esm;

import com.epam.esm.dto.GiftDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.GiftService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.util.CreateOrderParameter;
import com.epam.esm.util.assembler.GiftModelAssembler;
import com.epam.esm.util.assembler.OrderModelAssembler;
import com.epam.esm.util.assembler.TagModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderService orderService;
    private final GiftService giftService;
    private final TagService tagService;
    private final TagModelAssembler tagModelAssembler;
    private final OrderModelAssembler orderModelAssembler;
    private final GiftModelAssembler giftModelAssembler;

    @Autowired
    public OrderController(OrderService orderService, GiftService giftService, TagService tagService, TagModelAssembler tagModelAssembler, OrderModelAssembler orderModelAssembler, GiftModelAssembler giftModelAssembler) {
        this.orderService = orderService;
        this.giftService = giftService;
        this.tagService = tagService;
        this.tagModelAssembler = tagModelAssembler;
        this.orderModelAssembler = orderModelAssembler;
        this.giftModelAssembler = giftModelAssembler;
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<EntityModel<OrderDTO>> getAllOrders(@Valid Pageable page) {
        return orderModelAssembler.toModel(orderService.getAllOrders(page));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<OrderDTO> createOrder(@RequestBody @Valid CreateOrderParameter parameter) {
        return orderModelAssembler.toModel(orderService.createOrder(parameter));
    }

    @GetMapping("/{id}/gifts")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<EntityModel<GiftDTO>> getGiftCertificateListByOrderID(@PathVariable int id,@Valid Pageable page) {
        return giftModelAssembler.toModel(giftService.getCertificateListByOrderID(id,page));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public EntityModel<OrderDTO> getOrderById(@PathVariable int orderId) {
        return orderModelAssembler.toModel(orderService.getOrderById(orderId));
    }

    @GetMapping("/byUser/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<EntityModel<OrderDTO>> getOrdersByUserId(@PathVariable int id) {
        return orderModelAssembler.toModel(orderService.getOrdersByUserId(id));
    }

    @GetMapping("highest-cost/user/most-used-tag")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public EntityModel<TagDTO> getMostWidelyUsedTagFromUserWithHighestCostOfAllOrders() {
        return tagModelAssembler.toModel(tagService.getMostWidelyUsedTagFromUserWithHighestCostOfAllOrders());
    }

}
