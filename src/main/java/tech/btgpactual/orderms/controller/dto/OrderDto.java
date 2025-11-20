package tech.btgpactual.orderms.controller.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderDto(Long orderId, Long customerId, BigDecimal total, List<ItemDto> items) {
}
