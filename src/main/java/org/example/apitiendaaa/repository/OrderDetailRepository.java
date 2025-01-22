package org.example.apitiendaaa.repository;

import org.example.apitiendaaa.domain.OrderDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends CrudRepository<OrderDetail, Long> {

     List<OrderDetail> findAll();
    
     List<OrderDetail> findByOrderId(long order_id);

    List<OrderDetail> findByOrderIdAndDiscount(long orderId, Float discount);

    List<OrderDetail> findByOrderIdAndDiscountAndQuantityAndSubtotal(long order_id, Float discount, Float quantity, Float subtotal);

    List<OrderDetail> findByOrderIdAndDiscountAndQuantity(long order_id, Float discount, Float quantity);

    List<OrderDetail> findByOrderIdAndDiscountAndSubtotal(long order_id, Float discount, Float subtotal);

    List<OrderDetail> findByOrderIdAndQuantityAndSubtotal(long order_id, Float quantity, Float subtotal);

    List<OrderDetail> findByOrderIdAndQuantity(long order_id, Float quantity);

    List<OrderDetail> findByOrderIdAndSubtotal(long order_id, Float subtotal);
}
