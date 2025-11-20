package tech.btgpactual.orderms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.btgpactual.orderms.controller.dto.ApiResponse;
import tech.btgpactual.orderms.controller.dto.CountDTO;
import tech.btgpactual.orderms.service.OrderService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final  OrderService orderService;

    public CustomerController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{customerId}/orders/count")
    public ResponseEntity<CountDTO> getOrderCountByCostumer (@PathVariable Long customerId){
        return ResponseEntity.ok(orderService.getOrderCountByCostumer(customerId));
    }
}
