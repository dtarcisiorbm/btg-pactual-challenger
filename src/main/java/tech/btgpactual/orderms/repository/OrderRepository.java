package tech.btgpactual.orderms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import tech.btgpactual.orderms.entity.OrderEntity;

// ADD THIS LINE
import org.springframework.data.domain.Pageable;

// You also need this import for the return type
import org.springframework.data.domain.Page;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {
    Long countByCustomerId(Long costumerId);

    Page<OrderEntity> findAllByCustomerId(Long customerId, Pageable pageable);
}
