package tech.btgpactual.orderms.controller.dto;

import java.math.BigDecimal;

public record ItemDto(String product, Integer quantity, BigDecimal price) {
}
