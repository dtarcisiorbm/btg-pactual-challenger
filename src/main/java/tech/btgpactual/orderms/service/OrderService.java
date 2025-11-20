package tech.btgpactual.orderms.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.btgpactual.orderms.controller.dto.CountDTO;
import tech.btgpactual.orderms.controller.dto.ItemDto;
import tech.btgpactual.orderms.controller.dto.OrderDto;
import tech.btgpactual.orderms.controller.dto.OrderTotalDTO;
import tech.btgpactual.orderms.entity.OrderEntity;
import tech.btgpactual.orderms.entity.OrderItem;
import tech.btgpactual.orderms.exception.ResourceNotFoundException;
import tech.btgpactual.orderms.listener.dto.OrderCreatedEvent;
import tech.btgpactual.orderms.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

import static tech.btgpactual.orderms.config.RabbitMqConfig.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    public OrderService(OrderRepository orderRepository, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void save(OrderCreatedEvent event){
        var entity = new OrderEntity();
        entity.setOrderId(event.codigoPedido());
        entity.setCustomerId(event.codigoCliente());
        entity.setItems(getOrderItens(event));
        entity.setTotal(getTotal(entity)
        );
        orderRepository.save(entity);
    }

    private static BigDecimal getTotal(OrderEntity entity) {
        return entity.getItems()
                .stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private static List<OrderItem> getOrderItens(OrderCreatedEvent event) {
        return event.itens().stream().map(orderItemEvent ->
                        new OrderItem(orderItemEvent.produto(), orderItemEvent.quantidade(), orderItemEvent.preco()))
                .toList();
    }

    public OrderTotalDTO getTotalByOrder(Long orderId){
        BigDecimal total = orderRepository.findById(orderId)
                .map(OrderEntity::getTotal)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id:" + orderId ));

        return new OrderTotalDTO(total);
    }

    public CountDTO getOrderCountByCostumer(Long customerId) {
        Long orderEntity = orderRepository.countByCustomerId(customerId);
        return new CountDTO(orderEntity);
    }

    public Page<OrderDto> getAllOrdersByCostumer(Long customerId, Pageable pageable){
        Page<OrderEntity> orderPage = orderRepository.findAllByCustomerId(customerId, pageable);
        return orderPage.map(this::convertToDto);
    }

    private OrderDto convertToDto(OrderEntity order) {
        return new OrderDto(order.getOrderId(), order.getCustomerId(),order.getTotal(),
                order.getItems()
                        .stream()
                        .map(orderItem ->
                                new ItemDto(orderItem.getProduct(), orderItem.getQuantity(), orderItem.getPrice()))
                        .toList());
    }

    public void createOrder(OrderCreatedEvent orderDto) {
        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_CREATED_ROUTING_KEY , orderDto);
    }
}
