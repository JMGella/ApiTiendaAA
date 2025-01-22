package org.example.apitiendaaa.repository;

import org.example.apitiendaaa.domain.Order;
import org.example.apitiendaaa.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

  public List<Order> findAll();


  List<Order> findByCreationDate(LocalDate creationLocalDate);



  List<Order> findByStatus(String status);



  List<Order> findByStatusAndCreationDate(String status, LocalDate creationLocalDate);



  List<Order> findByUser(User user);

  List<Order> findByPaymentMethod(String paymentMethod);

  List<Order> findByPaymentMethodAndCreationDate(String paymentMethod, LocalDate creationLocalDate);

  List<Order> findByStatusAndPaymentMethod(String status, String paymentMethod);

  List<Order> findByStatusAndPaymentMethodAndCreationDate(String status, String paymentMethod, LocalDate creationLocalDate);
}
