package org.example.apitiendaaa.service;

import org.example.apitiendaaa.domain.DTO.OrderDetailInDTO;
import org.example.apitiendaaa.domain.DTO.OrderDetailOutDTO;
import org.example.apitiendaaa.domain.Order;
import org.example.apitiendaaa.domain.OrderDetail;
import org.example.apitiendaaa.domain.Product;
import org.example.apitiendaaa.domain.User;
import org.example.apitiendaaa.exception.OrderDetailNotFoundException;
import org.example.apitiendaaa.exception.OrderNotFoundException;
import org.example.apitiendaaa.exception.ProductNotFoundException;
import org.example.apitiendaaa.exception.UserNotFoundException;
import org.example.apitiendaaa.repository.OrderDetailRepository;
import org.example.apitiendaaa.repository.OrderRepository;
import org.example.apitiendaaa.repository.ProductRepository;
import org.example.apitiendaaa.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<OrderDetailOutDTO> findByOrder(long orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        return modelMapper.map(orderDetails, new org.modelmapper.TypeToken<List<OrderDetailOutDTO>>() {}.getType());
    }

    public OrderDetail add(long userId, long orderId, OrderDetailInDTO orderDetailIn) throws OrderNotFoundException, ProductNotFoundException, UserNotFoundException {
        validateUserAndOrder(userId, orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        Product product = productRepository.findById(orderDetailIn.getProductId()).orElseThrow(ProductNotFoundException::new);
        OrderDetail orderDetailToSave = new OrderDetail();
        orderDetailToSave.setQuantity(orderDetailIn.getQuantity());
        orderDetailToSave.setDiscount(orderDetailIn.getDiscount());
        orderDetailToSave.setProduct(product);
        float subtotal = product.getPrice() * orderDetailIn.getQuantity();
        if (orderDetailIn.getDiscount() > 0) {
            subtotal = subtotal - (subtotal * (orderDetailIn.getDiscount() / 100));
        }
        orderDetailToSave.setSubtotal(subtotal);
        orderDetailToSave.setCreationDate(LocalDate.now());

        if(order.getTotal() == null) {
            order.setTotal(0f);
        }
        order.setTotal(order.getTotal() + orderDetailToSave.getSubtotal());
        orderRepository.save(order);
        orderDetailToSave.setOrder(order);
        orderDetailRepository.save(orderDetailToSave);

        return orderDetailToSave;

    }




    public List<OrderDetailOutDTO> getAll(long productId, long orderId, long discount) {
        List<OrderDetail> orderDetails;

        if (productId == 0 && orderId == 0 && discount== 0) {
            orderDetails = (List<OrderDetail>) orderDetailRepository.findAll();
        } else if (productId == 0 && orderId == 0) {
            orderDetails = orderDetailRepository.findByDiscount(discount);
        } else if (productId == 0 && discount == 0) {
            orderDetails = orderDetailRepository.findByOrderId(orderId);
        } else if (orderId == 0) {
            orderDetails = orderDetailRepository.findByProductId(productId);
        } else if (productId == 0) {
            orderDetails = orderDetailRepository.findByOrderIdAndDiscount(orderId, discount);
        } else if (discount == 0) {
            orderDetails = orderDetailRepository.findByProductIdAndOrderId(productId, orderId);
        } else {
            orderDetails = orderDetailRepository.findByProductIdAndOrderIdAndDiscount(productId, orderId, discount);
        }
        return modelMapper.map(orderDetails, new TypeToken<List<OrderDetailOutDTO>>() {}.getType());
    }

    public OrderDetail update(long userId, long orderId, long detailId, OrderDetailInDTO orderDetailIn) throws OrderNotFoundException, ProductNotFoundException, UserNotFoundException {
        validateUserAndOrder(userId, orderId);
        OrderDetail orderDetail = orderDetailRepository.findById(detailId).orElseThrow(OrderNotFoundException::new);
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        Product product = productRepository.findById(orderDetailIn.getProductId()).orElseThrow(ProductNotFoundException::new);
        orderDetail.setProduct(product);
        orderDetail.setOrder(order);
        orderDetail.setQuantity(orderDetailIn.getQuantity());
        orderDetail.setDiscount(orderDetailIn.getDiscount());
        orderDetail.setSubtotal(orderDetailIn.getQuantity() * orderDetailIn.getDiscount() * product.getPrice());
        return orderDetailRepository.save(orderDetail);
    }


    public void delete(long userId, long orderId, long detailId) throws UserNotFoundException, OrderNotFoundException, OrderDetailNotFoundException {
        validateUserAndOrder(userId, orderId);
        OrderDetail orderDetail = orderDetailRepository.findById(detailId).orElseThrow(OrderDetailNotFoundException::new);
        orderDetailRepository.delete(orderDetail);
    }



    private void validateUserAndOrder (long userId, long orderId) throws UserNotFoundException, OrderNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if (order.getUser().getId() != user.getId()) {
            throw new UserNotFoundException("No se ha encontrado la orden para el usuario indicado");
        }
    }
}
