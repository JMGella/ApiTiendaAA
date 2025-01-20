package org.example.apitiendaaa.repository;

import org.example.apitiendaaa.domain.OrderDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends CrudRepository<OrderDetail, Long> {

    public List<OrderDetail> findAll();
    
    public List<OrderDetail> findByOrderId(long orderId);


    List<OrderDetail> findByDiscount(long disccount);

    List<OrderDetail> findByUserId(long userId);

    List<OrderDetail> findByOrderIdAndDiscount(long orderId, long disccount);

    List<OrderDetail> findByUserIdAndOrderId(long userId, long orderId);

    List<OrderDetail> findByUserIdAndOrderIdAndDiscount(long userId, long orderId, long disccount);
}
