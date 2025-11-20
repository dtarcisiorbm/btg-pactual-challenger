package tech.btgpactual.orderms.controller.dto;

public class CountDTO {

    private Long count;

    public CountDTO(Long quantity) {
        this.count = quantity;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
