package org.example.apitiendaaa.service;

import org.example.apitiendaaa.domain.DTO.OrderOutDTO;
import org.example.apitiendaaa.domain.Order;
import org.example.apitiendaaa.domain.User;
import org.example.apitiendaaa.exception.OrderNotFoundException;
import org.example.apitiendaaa.exception.UserNotFoundException;
import org.example.apitiendaaa.repository.OrderRepository;
import org.example.apitiendaaa.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    private ModelMapper modelMapper;

    public List<OrderOutDTO> getAll(String status, String total, String creationDate) {

        List<Order> allOrders;
        double totaldouble = Double.parseDouble(total);
        LocalDate creationLocalDate = LocalDate.parse(creationDate);
        if (status.isEmpty() && total.isEmpty() && creationDate.isEmpty()) {
            allOrders = orderRepository.findAll();
        } else if (status.isEmpty() && total.isEmpty()) {
            allOrders = orderRepository.findByCreationDate(creationLocalDate);
        } else if (status.isEmpty() && creationDate.isEmpty()) {

            allOrders = orderRepository.findByTotal(totaldouble);
        } else if (total.isEmpty() && creationDate.isEmpty()) {
            allOrders = orderRepository.findByStatus(status);
        } else if (status.isEmpty()) {
            allOrders = orderRepository.findByTotalAndCreationDate(totaldouble, creationLocalDate);
        } else if (total.isEmpty()) {
            allOrders = orderRepository.findByStatusAndCreationDate(status, creationLocalDate);
        } else if (creationDate.isEmpty()) {
            allOrders = orderRepository.findByStatusAndTotal(status, totaldouble);
        } else {
            allOrders = orderRepository.findByStatusAndTotalAndCreationDate(status, totaldouble, creationLocalDate);
        }
        return modelMapper.map(allOrders, new TypeToken<List<OrderOutDTO>>() {}.getType());

    }


    public Order add(long userId, Order order) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        order.setUser(user);
        return orderRepository.save(order);

    }

    public List<OrderOutDTO> getOrdersByUser(long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Order> orders = orderRepository.findByUser(user);
        return modelMapper.map(orders, new TypeToken<List<OrderOutDTO>>() {}.getType());
    }

    public Order get(long orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        return order;
    }

    public Order update(long userId, long orderId, Order order) throws UserNotFoundException, OrderNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Order orderToUpdate = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        orderToUpdate.setCreationDate(order.getCreationDate());
        orderToUpdate.setStatus(order.getStatus());
        orderToUpdate.setTotal(order.getTotal());
        orderToUpdate.setUser(user);
        orderToUpdate.setAddress(order.getAddress());
        orderToUpdate.setPaymentMethod(order.getPaymentMethod());
        return orderRepository.save(orderToUpdate);
    }

    public void delete(long orderId) throws OrderNotFoundException {
       Order order =  orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
         orderRepository.delete(order);
    }
}
