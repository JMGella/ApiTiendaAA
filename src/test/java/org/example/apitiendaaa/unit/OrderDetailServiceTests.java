package org.example.apitiendaaa.unit;

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
import org.example.apitiendaaa.service.OrderDetailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderDetailServiceTests {
    @InjectMocks
    private OrderDetailService orderDetailService;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;


    @Mock
    private ModelMapper modelMapper;


    @Test
    public void testAdd() throws OrderNotFoundException, UserNotFoundException, ProductNotFoundException {
        long userId = 1L;
        long orderId = 1L;
        OrderDetailInDTO orderDetailInDTO = new OrderDetailInDTO(1, 10f, 1L);
        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);
        Order order = new Order(orderId, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", user , null);
        Product product = new Product(orderDetailInDTO.getProductId(), "Product1", "Description1", 10.0f, LocalDate.now(), true, "image1", null, null);

        float subtotal = product.getPrice() * orderDetailInDTO.getQuantity();
        if (orderDetailInDTO.getDiscount()>0){
            subtotal = subtotal - (subtotal * (orderDetailInDTO.getDiscount() / 100));
        }

        OrderDetail orderDetailOut = new OrderDetail(1L, orderDetailInDTO.getQuantity(), subtotal, orderDetailInDTO.getDiscount(), LocalDate.now(), product, order);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(productRepository.findById(orderDetailInDTO.getProductId())).thenReturn(java.util.Optional.of(product));
        when(orderDetailRepository.save(any(OrderDetail.class))).thenReturn(orderDetailOut);

        OrderDetail result = orderDetailService.add(userId, orderId, orderDetailInDTO);

        assertEquals(orderDetailInDTO.getQuantity(), result.getQuantity());
        assertEquals(orderDetailInDTO.getDiscount(), result.getDiscount());
        assertEquals(orderDetailInDTO.getProductId(), result.getProduct().getId());
        assertEquals(orderId, result.getOrder().getId());
        assertEquals(userId, result.getOrder().getUser().getId());


        verify(orderRepository, times(2)).findById(orderId);
        verify(productRepository, times(1)).findById(orderDetailInDTO.getProductId());
        verify(orderDetailRepository, times(1)).save(any(OrderDetail.class));

    }


    @Test
    public void testAddUserNotFound(){
        long userId = 1L;
        long orderId = 1L;
        OrderDetailInDTO orderDetailInDTO = new OrderDetailInDTO(1, 10f, 1L);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            orderDetailService.add(userId, orderId, orderDetailInDTO);
        });

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testAddOrderNotFound(){
        long userId = 1L;
        long orderId = 1L;
        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);
        OrderDetailInDTO orderDetailInDTO = new OrderDetailInDTO(1, 10f, 1L);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderDetailService.add(userId, orderId, orderDetailInDTO);
        });

        verify(orderRepository, times(1)).findById(orderId);
        verify(userRepository, times(1)).findById(userId);

    }

    @Test
    public void testAddProductNotFound(){
        long userId = 1L;
        long orderId = 1L;
        OrderDetailInDTO orderDetailInDTO = new OrderDetailInDTO(1, 10f, 1L);
        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);
        Order order = new Order(orderId, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", user , null);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(productRepository.findById(orderDetailInDTO.getProductId())).thenReturn(java.util.Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            orderDetailService.add(userId, orderId, orderDetailInDTO);
        });

        verify(orderRepository, times(2)).findById(orderId);
        verify(userRepository, times(1)).findById(userId);
        verify(productRepository, times(1)).findById(orderDetailInDTO.getProductId());

    }

    @Test
    public void testGetAll() throws OrderNotFoundException, UserNotFoundException {
        long userId = 1L;
        long orderId = 1L;
        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);
        Order order = new Order(orderId, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", user , null);
        List<OrderDetail> orderDetailList = List.of(
                new OrderDetail(1L, 2, 20f, 0f, LocalDate.now(), null, order),
                new OrderDetail(2L, 3, 30f, 0f, LocalDate.now(), null, order)
        );
        List<OrderDetailOutDTO> oderDetailOutDTOList = List.of(
                new OrderDetailOutDTO(1L, orderId, 1L, "Product", 2, 0f, 20f),
                new OrderDetailOutDTO(2L, orderId, 2L, "Product2", 3, 0f, 20f)
        );

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(orderDetailRepository.findByOrderId(orderId)).thenReturn(orderDetailList);

        when(modelMapper.map(orderDetailList, new org.modelmapper.TypeToken<List<OrderDetailOutDTO>>() {}.getType())).thenReturn(oderDetailOutDTOList);

        List<OrderDetailOutDTO> result = orderDetailService.getAll(userId, orderId, null, null, null);

        assertEquals(2, result.size());
        assertEquals(orderId, result.get(0).getOrderId());
        assertEquals(1L, result.get(0).getProductId());
        assertEquals(2, result.get(0).getQuantity());
        assertEquals(0f, result.get(0).getDiscount());
        assertEquals(20f, result.get(0).getSubtotal());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderDetailRepository, times(1)).findByOrderId(orderId);

    }

    @Test
    public void testGetAllOrderNotFound() {
        long userId = 1L;
        long orderId = 1L;
        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.empty());
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        assertThrows(OrderNotFoundException.class, () -> {
            orderDetailService.getAll(userId, orderId, null, null, null);
        });

        verify(orderRepository, times(1)).findById(orderId);
        verify(userRepository, times(1)).findById(userId);

    }

    @Test
    public void testGetAllUserNotFound() {
        long userId = 1L;
        long orderId = 1L;

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            orderDetailService.getAll(userId, orderId, null, null, null);
        });

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testGetAllDiscount() throws OrderNotFoundException, UserNotFoundException {
        long userId = 1L;
        long orderId = 1L;
        float discount = 20f;
        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);
        Order order = new Order(orderId, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", user , null);
        List<OrderDetail> orderDetailList = List.of(
                new OrderDetail(1L, 2, 20f, 20f, LocalDate.now(), null, order),
                new OrderDetail(2L, 3, 30f, 20f, LocalDate.now(), null, order)
        );
        List<OrderDetailOutDTO> oderDetailOutDTOList = List.of(
                new OrderDetailOutDTO(1L, orderId, 1L, "Product", 2, 20f, 20f),
                new OrderDetailOutDTO(2L, orderId, 2L, "Product2", 3, 20f, 20f)
        );

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(orderDetailRepository.findByOrderIdAndDiscount(orderId,discount )).thenReturn(orderDetailList);

        when(modelMapper.map(orderDetailList, new org.modelmapper.TypeToken<List<OrderDetailOutDTO>>() {}.getType())).thenReturn(oderDetailOutDTOList);

        List<OrderDetailOutDTO> result = orderDetailService.getAll(userId, orderId, discount, null, null);

        assertEquals(2, result.size());
        assertEquals(orderId, result.get(0).getOrderId());
        assertEquals(1L, result.get(0).getProductId());
        assertEquals(2, result.get(0).getQuantity());
        assertEquals(discount, result.get(0).getDiscount());
        assertEquals(discount, result.get(1).getDiscount());


        verify(orderRepository, times(1)).findById(orderId);
        verify(orderDetailRepository, times(1)).findByOrderIdAndDiscount(orderId,discount);

    }

    @Test
    public void testGetAllQuantity() throws OrderNotFoundException, UserNotFoundException {
        long userId = 1L;
        long orderId = 1L;
        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);
        Order order = new Order(orderId, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", user , null);
        List<OrderDetail> orderDetailList = List.of(
                new OrderDetail(1L, 2, 20f, 0f, LocalDate.now(), null, order),
                new OrderDetail(2L, 2, 30f, 0f, LocalDate.now(), null, order)
        );
        List<OrderDetailOutDTO> oderDetailOutDTOList = List.of(
                new OrderDetailOutDTO(1L, orderId, 1L, "Product", 2, 0f, 20f),
                new OrderDetailOutDTO(2L, orderId, 2L, "Product2", 2, 0f, 20f)
        );

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(orderDetailRepository.findByOrderIdAndQuantity(orderId, 2F)).thenReturn(orderDetailList);

        when(modelMapper.map(orderDetailList, new org.modelmapper.TypeToken<List<OrderDetailOutDTO>>() {}.getType())).thenReturn(oderDetailOutDTOList);

        List<OrderDetailOutDTO> result = orderDetailService.getAll(userId, orderId, null, 2F, null);

        assertEquals(2, result.size());
        assertEquals(orderId, result.get(0).getOrderId());
        assertEquals(1L, result.get(0).getProductId());
        assertEquals(2, result.get(0).getQuantity());
        assertEquals(2, result.get(1).getQuantity());
        assertEquals(0f, result.get(0).getDiscount());
        assertEquals(20f, result.get(0).getSubtotal());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderDetailRepository, times(1)).findByOrderIdAndQuantity(orderId, 2F);

    }

    @Test
    public void testGetAllSubtotal() throws OrderNotFoundException, UserNotFoundException {
        long userId = 1L;
        long orderId = 1L;
        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);
        Order order = new Order(orderId, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", user , null);
        List<OrderDetail> orderDetailList = List.of(
                new OrderDetail(1L, 2, 20f, 0f, LocalDate.now(), null, order),
                new OrderDetail(2L, 3, 20f, 0f, LocalDate.now(), null, order)
        );
        List<OrderDetailOutDTO> oderDetailOutDTOList = List.of(
                new OrderDetailOutDTO(1L, orderId, 1L, "Product", 2, 0f, 20f),
                new OrderDetailOutDTO(2L, orderId, 2L, "Product2", 3, 0f, 20f)
        );

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(orderDetailRepository.findByOrderIdAndSubtotal(orderId, 20f)).thenReturn(orderDetailList);

        when(modelMapper.map(orderDetailList, new org.modelmapper.TypeToken<List<OrderDetailOutDTO>>() {}.getType())).thenReturn(oderDetailOutDTOList);

        List<OrderDetailOutDTO> result = orderDetailService.getAll(userId, orderId, null, null, 20f);

        assertEquals(2, result.size());
        assertEquals(orderId, result.get(0).getOrderId());
        assertEquals(1L, result.get(0).getProductId());
        assertEquals(2, result.get(0).getQuantity());
        assertEquals(0f, result.get(0).getDiscount());
        assertEquals(20f, result.get(0).getSubtotal());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderDetailRepository, times(1)).findByOrderIdAndSubtotal(orderId, 20f);

    }

    @Test
    public void testUpdate() throws OrderNotFoundException, UserNotFoundException, ProductNotFoundException {
        long userId = 1L;
        long orderId = 1L;
        long orderDetailId = 1L;
        OrderDetailInDTO orderDetailInDTO = new OrderDetailInDTO(1, 10f, 1L);

        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);
        Order order = new Order(orderId, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", user, null);
        Product product = new Product(orderDetailInDTO.getProductId(), "Product1", "Description1", 10.0f, LocalDate.now(), true, "image1", null, null);

        OrderDetail orderDetail = new OrderDetail(orderDetailId, 2, 20f, 0f, LocalDate.now(), product , order);

        List<OrderDetail> orderDetailList = List.of(orderDetail);

        float subtotal = product.getPrice() * orderDetailInDTO.getQuantity();
        OrderDetail orderDetailToUpdate = new OrderDetail(orderDetailId,  orderDetailInDTO.getQuantity(), subtotal, orderDetailInDTO.getDiscount(), LocalDate.now(), product , order);
        OrderDetailOutDTO orderDetailOutDTO = new OrderDetailOutDTO(orderDetailId, orderId, orderDetailToUpdate.getProduct().getId(), orderDetailToUpdate.getProduct().getName(), orderDetailToUpdate.getQuantity(), orderDetailToUpdate.getDiscount(), orderDetailToUpdate.getSubtotal());



        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderDetailRepository.findById(orderDetailId)).thenReturn(Optional.of(orderDetail));
        when(orderDetailRepository.findByOrderId(orderId)).thenReturn(orderDetailList);
        when(productRepository.findById(orderDetailInDTO.getProductId())).thenReturn(Optional.of(product));
        when(orderDetailRepository.save(any(OrderDetail.class))).thenReturn(orderDetailToUpdate);

        when(modelMapper.map(any(OrderDetail.class), eq(OrderDetailOutDTO.class))).thenReturn(orderDetailOutDTO);



        OrderDetailOutDTO result = orderDetailService.update(userId, orderId, orderDetailId, orderDetailInDTO);



        assertEquals(orderDetailId, result.getId());
        assertEquals(orderId, result.getOrderId());
        assertEquals(orderDetailInDTO.getProductId(), result.getProductId());
        assertEquals(orderDetailInDTO.getQuantity(), result.getQuantity());
        assertEquals(orderDetailInDTO.getDiscount(), result.getDiscount());
        assertEquals(subtotal, result.getSubtotal());

        verify(orderRepository, times(2)).findById(orderId);
        verify(orderDetailRepository, times(1)).findById(orderDetailId);
        verify(orderDetailRepository, times(1)).findByOrderId(orderId);


    }

    @Test
    public void testUpdateOrderNotFound() {
        long userId = 1L;
        long orderId = 1L;
        long orderDetailId = 1L;
        OrderDetailInDTO orderDetailInDTO = new OrderDetailInDTO(1, 10f, 1L);


        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderDetailService.update(userId, orderId, orderDetailId, orderDetailInDTO);
        });

        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    public void testUpdateUserNotFound() {
        long userId = 1L;
        long orderId = 1L;
        long orderDetailId = 1L;
        OrderDetailInDTO orderDetailInDTO = new OrderDetailInDTO(1, 10f, 1L);



        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            orderDetailService.update(userId, orderId, orderDetailId, orderDetailInDTO);
        });

        verify(userRepository, times(1)).findById(orderId);
    }


    @Test
    public void testUpdateProductNotFound() {
        long userId = 1L;
        long orderId = 1L;
        long orderDetailId = 1L;
        OrderDetailInDTO orderDetailInDTO = new OrderDetailInDTO(1, 10f, 1L);

        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);
        Order order = new Order(orderId, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", user, null);

        OrderDetail orderDetail = new OrderDetail(orderDetailId, 2, 20f, 0f, LocalDate.now(), null , order);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderDetailRepository.findById(orderDetailId)).thenReturn(Optional.of(orderDetail));
        when(productRepository.findById(orderDetailInDTO.getProductId())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            orderDetailService.update(userId, orderId, orderDetailId, orderDetailInDTO);
        });

        verify(orderRepository, times(2)).findById(orderId);
        verify(userRepository, times(1)).findById(userId);
        verify(productRepository, times(1)).findById(orderDetailInDTO.getProductId());
    }

    @Test
    public void testDelete() throws UserNotFoundException, OrderNotFoundException, OrderDetailNotFoundException {
        long userId = 1L;
        long orderId = 1L;
        long orderDetailId = 1L;
        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);
        Order order = new Order(orderId, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", user, null);
        OrderDetail orderDetail = new OrderDetail(orderDetailId, 2, 20f, 0f, LocalDate.now(), null , order);
        order.setTotal(order.getTotal() - orderDetail.getSubtotal());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderDetailRepository.findById(orderDetailId)).thenReturn(Optional.of(orderDetail));
        when(orderRepository.save(order)).thenReturn(order);

        orderDetailService.delete(userId, orderId, orderDetailId);

        verify(orderDetailRepository, times(1)).delete(orderDetail);
        verify(orderRepository, times(1)).save(order);

    }

    @Test
    public void testDeleteUserNotFound() {
        long userId = 1L;
        long orderId = 1L;
        long orderDetailId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            orderDetailService.delete(userId, orderId, orderDetailId);
        });

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testDeleteOrderNotFound() {
        long userId = 1L;
        long orderId = 1L;
        long orderDetailId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderDetailService.delete(userId, orderId, orderDetailId);
        });

        verify(orderRepository, times(1)).findById(orderId);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testDeleteOrderDetailNotFound(){
        long userId = 1L;
        long orderId = 1L;
        long orderDetailId = 1L;
        Order order = new Order(orderId, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", new User(), null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderDetailRepository.findById(orderDetailId)).thenReturn(Optional.empty());

        assertThrows(OrderDetailNotFoundException.class, () -> {
            orderDetailService.delete(userId, orderId, orderDetailId);
        });

        verify(orderDetailRepository, times(1)).findById(orderDetailId);
        verify(orderRepository, times(2)).findById(orderId);
        verify(userRepository, times(1)).findById(userId);

    }

    @Test
    public void testValidateUserAndOrder() throws UserNotFoundException, OrderNotFoundException {
        long userId = 1L;
        long orderId = 1L;
        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);
        Order order = new Order(orderId, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", user, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderDetailService.validateUserAndOrder(userId, orderId);

        verify(userRepository, times(1)).findById(userId);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    public void testValidateUserAndOrder_UserNotFound() {
        long userId = 1L;
        long orderId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            orderDetailService.validateUserAndOrder(userId, orderId);
        });

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testValidateUserAndOrder_OrderNotFound(){
        long userId = 1L;
        long orderId = 1L;
        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderDetailService.validateUserAndOrder(userId, orderId);
        });

        verify(orderRepository, times(1)).findById(orderId);
        verify(userRepository, times(1)).findById(userId);

    }



}
