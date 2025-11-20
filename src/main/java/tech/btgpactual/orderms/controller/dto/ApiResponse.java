package tech.btgpactual.orderms.controller.dto;

import java.util.List;

public record ApiResponse<T>  (List<T> data, PaginationReponse paginationReponse){
}
