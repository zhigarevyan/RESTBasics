package com.epam.esm;

import com.epam.esm.dto.GiftDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.GiftService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.util.CreateOrderParameter;
import com.epam.esm.util.Page;
import com.epam.esm.util.assembler.GiftModelAssembler;
import com.epam.esm.util.assembler.OrderModelAssembler;
import com.epam.esm.util.assembler.TagModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
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
    public List<EntityModel<OrderDTO>> getAllOrders(@Valid Page page) {
        return orderModelAssembler.toModel(orderService.getAllOrders(page));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<OrderDTO> createOrder(@RequestBody @Valid CreateOrderParameter parameter) {
        return orderModelAssembler.toModel(orderService.createOrder(parameter));
    }

    @GetMapping("/{id}/gifts")
    public List<EntityModel<GiftDTO>> getGiftCertificateListByOrderID(@PathVariable int id,@Valid Page page) {
        return giftModelAssembler.toModel(giftService.getCertificateListByOrderID(id,page));
    }

    @GetMapping("/{orderId}")
    public EntityModel<OrderDTO> getOrderById(@PathVariable int orderId) {
        return orderModelAssembler.toModel(orderService.getOrderById(orderId));
    }

    @GetMapping("/byUser/{id}")
    public List<EntityModel<OrderDTO>> getOrdersByUserId(@PathVariable int id,@Valid Page page) {
        return orderModelAssembler.toModel(orderService.getOrdersByUserId(id, page));
    }

    @GetMapping("highest-cost/user/most-used-tag")
    public EntityModel<TagDTO> getMostWidelyUsedTagFromUserWithHighestCostOfAllOrders() {
        return tagModelAssembler.toModel(tagService.getMostWidelyUsedTagFromUserWithHighestCostOfAllOrders());
    }

}
