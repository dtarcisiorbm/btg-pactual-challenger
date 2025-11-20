package tech.btgpactual.orderms.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.btgpactual.orderms.controller.dto.ApiResponse;
import tech.btgpactual.orderms.controller.dto.OrderDto;
import tech.btgpactual.orderms.controller.dto.OrderTotalDTO;
import tech.btgpactual.orderms.listener.dto.OrderCreatedEvent;
import tech.btgpactual.orderms.service.OrderService;


@RestController()
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{orderId}/total")
    @ApiOperation("Return total value of an order")
    public ResponseEntity<OrderTotalDTO> getTotal(@PathVariable Long orderId){
        return ResponseEntity.ok(orderService.getTotalByOrder(orderId));
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<Page<OrderDto>> listOrders(@PathVariable Long customerId, Pageable pageable){
        return ResponseEntity.ok(orderService.getAllOrdersByCostumer(customerId, pageable));
    }

    @PostMapping()
    public ResponseEntity<Void>createOrder(@RequestBody OrderCreatedEvent orderDto){
        orderService.createOrder(orderDto);
        return ResponseEntity.accepted().build();
    }
}
