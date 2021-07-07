package com.epam.esm;

import com.epam.esm.dto.GiftDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.service.GiftService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.util.CreateOrderParameter;
import com.epam.esm.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public OrderController(OrderService orderService, GiftService giftService,TagService tagService) {
        this.giftService = giftService;
        this.orderService = orderService;
        this.tagService = tagService;
    }

    @GetMapping
    public List<OrderDTO> getAllOrders(@RequestBody @Valid Page page) {
        return orderService.getAllOrders(page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody @Valid CreateOrderParameter parameter) {
        return orderService.createOrder(parameter);
    }

    @GetMapping("/{id}/gifts")
    public List<GiftDTO> getGiftCertificateListByOrderID(@PathVariable int id) {
        return giftService.getCertificateListByOrderID(id);
    }

    @GetMapping("/{orderId}")
    public OrderDTO getOrderById(@PathVariable int orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/byUser/{id}")
    public List<OrderDTO> getOrdersByUserId(@PathVariable int id) {
        return orderService.getOrdersByUserId(id);
    }

    @GetMapping("highest-cost/user/most-used-tag")
    public TagDTO getMostWidelyUsedTagFromUserWithHighestCostOfAllOrders() {
        return tagService.getMostWidelyUsedTagFromUserWithHighestCostOfAllOrders();
    }

}
