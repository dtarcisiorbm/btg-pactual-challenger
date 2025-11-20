package tech.btgpactual.orderms.controller.dto;

import java.math.BigDecimal;

public class OrderTotalDTO {
    private BigDecimal orderTotal;

    public OrderTotalDTO (BigDecimal orderTotal){
        this.orderTotal = orderTotal;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }
}
