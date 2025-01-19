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

  List<Order> findByTotal(double totaldouble);

  List<Order> findByStatus(String status);

  List<Order> findByTotalAndCreationDate(double totaldouble, LocalDate creationLocalDate);

  List<Order> findByStatusAndCreationDate(String status, LocalDate creationLocalDate);

  List<Order> findByStatusAndTotal(String status, double totaldouble);

  List<Order> findByStatusAndTotalAndCreationDate(String status, double totaldouble, LocalDate creationLocalDate);

  List<Order> findByUser(User user);
}
