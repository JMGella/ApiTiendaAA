package org.example.apitiendaaa.repository;

import org.example.apitiendaaa.domain.OrderDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends CrudRepository<OrderDetail, Long> {

    public List<OrderDetail> findAll();
    
    public List<OrderDetail> findByOrderId(long orderId);



    List<OrderDetail> findByOrderIdAndDiscount(long orderId, Float discount);

    List<OrderDetail> findByOrderIdAndDiscountAndQuantityAndSubtotal(long orderId, Float discount, Float quantity, Float subtotal);

    List<OrderDetail> findByOrderIdAndDiscountAndQuantity(long orderId, Float discount, Float quantity);

    List<OrderDetail> findByOrderIdAndDiscountAndSubtotal(long orderId, Float discount, Float subtotal);

    List<OrderDetail> findByOrderIdAndQuantityAndSubtotal(long orderId, Float quantity, Float subtotal);

    List<OrderDetail> findByOrderIdAndQuantity(long orderId, Float quantity);

    List<OrderDetail> findByOrderIdAndSubtotal(long orderId, Float subtotal);
}
