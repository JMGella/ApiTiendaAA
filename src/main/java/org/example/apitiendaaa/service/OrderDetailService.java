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




    public List<OrderDetailOutDTO> getAll(long userId, long orderId, Float discount, Float quantity, Float subtotal) throws OrderNotFoundException, UserNotFoundException {
        validateUserAndOrder(userId, orderId);

            List<OrderDetail> orderDetails;

        if (discount != null && quantity != null && subtotal != null){
            orderDetails = orderDetailRepository.findByOrderIdAndDiscountAndQuantityAndSubtotal(orderId, discount, quantity, subtotal);
        } else if (discount != null && quantity != null) {
            orderDetails = orderDetailRepository.findByOrderIdAndDiscountAndQuantity(orderId, discount, quantity);
        } else if (discount != null && subtotal != null) {
            orderDetails = orderDetailRepository.findByOrderIdAndDiscountAndSubtotal(orderId, discount, subtotal);
        } else if (quantity != null && subtotal != null) {
            orderDetails = orderDetailRepository.findByOrderIdAndQuantityAndSubtotal(orderId, quantity, subtotal);
        } else if (discount != null) {
            orderDetails = orderDetailRepository.findByOrderIdAndDiscount(orderId, discount);
        } else if (quantity != null) {
            orderDetails = orderDetailRepository.findByOrderIdAndQuantity(orderId, quantity);
        } else if (subtotal != null) {
            orderDetails = orderDetailRepository.findByOrderIdAndSubtotal(orderId, subtotal);
        } else {
            orderDetails = orderDetailRepository.findByOrderId(orderId);
        }

        return modelMapper.map(orderDetails, new TypeToken<List<OrderDetailOutDTO>>() {}.getType());
    }

    public OrderDetailOutDTO update(long userId, long orderId, long detailId, OrderDetailInDTO orderDetailIn) throws OrderNotFoundException, ProductNotFoundException, UserNotFoundException {
        validateUserAndOrder(userId, orderId);
        OrderDetail orderDetail = orderDetailRepository.findById(detailId).orElseThrow(OrderNotFoundException::new);
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        Product product = null;
        if (orderDetailIn.getProductId()>0) {
            product = productRepository.findById(orderDetailIn.getProductId()).orElseThrow(ProductNotFoundException::new);
            orderDetail.setProduct(product);
        } else {
            product = orderDetail.getProduct();
        }
        orderDetail.setOrder(order);
        if (orderDetailIn.getQuantity() >= 0)  {
            orderDetail.setQuantity(orderDetailIn.getQuantity());
        }
        if (orderDetailIn.getDiscount() != null) {
            orderDetail.setDiscount(orderDetailIn.getDiscount());
        }
        if (orderDetailIn.getQuantity() >= 0 || orderDetailIn.getDiscount() != null || orderDetailIn.getProductId() > 0) {

            float subtotal = product.getPrice() * orderDetail.getQuantity();
            if (orderDetailIn.getDiscount() != null) {
                subtotal = subtotal - (subtotal * (orderDetail.getDiscount() / 100));
            }
            orderDetail.setSubtotal(subtotal);

            List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
            Float total = 0f;
            for( OrderDetail od : orderDetailList) {
                total = total + od.getSubtotal();
            }
            order.setTotal(total);
        }

        orderDetailRepository.save(orderDetail);

        return modelMapper.map(orderDetail, OrderDetailOutDTO.class);
    }


    public void delete(long userId, long orderId, long detailId) throws UserNotFoundException, OrderNotFoundException, OrderDetailNotFoundException {
        validateUserAndOrder(userId, orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        OrderDetail orderDetail = orderDetailRepository.findById(detailId).orElseThrow(OrderDetailNotFoundException::new);
        order.setTotal(order.getTotal() - orderDetail.getSubtotal());
        orderRepository.save(order);
        orderDetailRepository.delete(orderDetail);
    }



    public void validateUserAndOrder (long userId, long orderId) throws UserNotFoundException, OrderNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if (order.getUser().getId() != user.getId()) {
            throw new UserNotFoundException("No se ha encontrado la orden para el usuario indicado");
        }
    }
}
