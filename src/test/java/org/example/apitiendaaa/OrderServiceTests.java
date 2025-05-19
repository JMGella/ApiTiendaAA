package org.example.apitiendaaa;

import org.example.apitiendaaa.domain.DTO.OrderOutDTO;
import org.example.apitiendaaa.domain.Order;
import org.example.apitiendaaa.domain.User;
import org.example.apitiendaaa.exception.OrderNotFoundException;
import org.example.apitiendaaa.exception.UserNotFoundException;
import org.example.apitiendaaa.repository.OrderRepository;
import org.example.apitiendaaa.repository.UserRepository;
import org.example.apitiendaaa.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRepository userRepository;


    @Test
    public void testGetAll(){
        List <Order> orderList = List.of(new Order(1L, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", null , null),
                new Order(2L, "COMPLETED", 200f, "address", LocalDate.now(), "CREDIT_CARD", null , null));


        List<OrderOutDTO> orderOutDTOList = List.of(new OrderOutDTO(1L, "PENDING", 100f, 1, "address", LocalDate.now().toString(), "PAYPAL"),
              new OrderOutDTO(2L, "COMPLETED", 200f, 2, "address", LocalDate.now().toString(), "CREDIT_CARD"));

        when(orderRepository.findAll()).thenReturn(orderList);
        when(modelMapper.map(orderList, new TypeToken<List<OrderOutDTO>>() {}.getType())).thenReturn(orderOutDTOList);

        List <OrderOutDTO> result = orderService.getAll("", "", "");

        assertEquals(2, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
        assertEquals("COMPLETED", result.get(1).getStatus());
        assertEquals(100f, result.get(0).getTotal());

        verify(orderRepository, times(1)).findAll();

    }

    @Test
    public void testGetAllStatus(){
        List <Order> orderList = List.of(new Order(1L, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", null , null),
                new Order(2L, "PENDING", 200f, "address", LocalDate.now(), "CREDIT_CARD", null , null));


        List<OrderOutDTO> orderOutDTOList = List.of(new OrderOutDTO(1L, "PENDING", 100f, 1, "address", LocalDate.now().toString(), "PAYPAL"),
                new OrderOutDTO(2L, "PENDING", 200f, 2, "address", LocalDate.now().toString(), "CREDIT_CARD"));

        when(orderRepository.findByStatus("PENDING")).thenReturn(orderList);
        when(modelMapper.map(orderList, new TypeToken<List<OrderOutDTO>>() {}.getType())).thenReturn(orderOutDTOList);

        List <OrderOutDTO> result = orderService.getAll("PENDING", "", "");

        assertEquals(2, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
        assertEquals("PENDING", result.get(1).getStatus());
        assertEquals(100f, result.get(0).getTotal());

        verify(orderRepository, times(1)).findByStatus("PENDING");

    }

    @Test
    public void testGetAllPaymentMethod(){
        List <Order> orderList = List.of(new Order(1L, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", null , null),
                new Order(2L, "COMPLETED", 200f, "address", LocalDate.now(), "PAYPAL", null , null));


        List<OrderOutDTO> orderOutDTOList = List.of(new OrderOutDTO(1L, "PENDING", 100f, 1, "address", LocalDate.now().toString(), "PAYPAL"),
                new OrderOutDTO(2L, "COMPLETED", 200f, 2, "address", LocalDate.now().toString(), "PAYPAL"));

        when(orderRepository.findByPaymentMethod("PAYPAL")).thenReturn(orderList);
        when(modelMapper.map(orderList, new TypeToken<List<OrderOutDTO>>() {}.getType())).thenReturn(orderOutDTOList);

        List <OrderOutDTO> result = orderService.getAll("", "PAYPAL", "");

        assertEquals(2, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
        assertEquals("COMPLETED", result.get(1).getStatus());
        assertEquals("PAYPAL", result.get(0).getPaymentMethod());
        assertEquals("PAYPAL", result.get(1).getPaymentMethod());
        assertEquals(100f, result.get(0).getTotal());

        verify(orderRepository, times(1)).findByPaymentMethod("PAYPAL");

    }

    @Test
    public void testGetAllCreationDate(){
        List <Order> orderList = List.of(new Order(1L, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", null , null),
                new Order(2L, "COMPLETED", 200f, "address", LocalDate.now(), "CREDIT_CARD", null , null));


        List<OrderOutDTO> orderOutDTOList = List.of(new OrderOutDTO(1L, "PENDING", 100f, 1, "address", LocalDate.now().toString(), "PAYPAL"),
                new OrderOutDTO(2L, "COMPLETED", 200f, 2, "address", LocalDate.now().toString(), "CREDIT_CARD"));

        when(orderRepository.findByCreationDate(LocalDate.now())).thenReturn(orderList);
        when(modelMapper.map(orderList, new TypeToken<List<OrderOutDTO>>() {}.getType())).thenReturn(orderOutDTOList);

        List <OrderOutDTO> result = orderService.getAll("", "", LocalDate.now().toString());

        assertEquals(2, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
        assertEquals("COMPLETED", result.get(1).getStatus());
        assertEquals(100f, result.get(0).getTotal());
        assertEquals(LocalDate.now().toString(), result.get(0).getCreationDate());

        verify(orderRepository, times(1)).findByCreationDate(LocalDate.now());

    }

    @Test
    public void testAdd() throws UserNotFoundException {
        long userId = 1L;
        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        Order order = new Order(1L, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", user , null);

        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.add(userId, order);

        assertEquals(userId, result.getUser().getId());
        assertEquals("PENDING", result.getStatus());
        assertEquals(100f, result.getTotal());
        assertEquals("address", result.getAddress());


        verify(userRepository, times(1)).findById(userId);
        verify(orderRepository, times(1)).save(order);

    }

    @Test
    public void testAddUserNotFoundException() {
        long userId = 1L;
        Order order = new Order(1L, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", null , null);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            orderService.add(userId, order);
        });

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testGetOrdersByUser() throws UserNotFoundException {
        long userId = 1L;
        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));


        List<Order> orderList = List.of(new Order(1L, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", user , null),
                new Order(2L, "COMPLETED", 200f, "address", LocalDate.now(), "CREDIT_CARD", user , null));

       List<OrderOutDTO> orderOutDTOList = List.of(new OrderOutDTO(1L, "PENDING", 100f, (int) userId, "address", LocalDate.now().toString(), "PAYPAL"),
                new OrderOutDTO(2L, "COMPLETED", 200f, (int) userId, "address", LocalDate.now().toString(), "CREDIT_CARD"));


        when(orderRepository.findByUser(user)).thenReturn(orderList);
        when(modelMapper.map(orderList, new TypeToken<List<OrderOutDTO>>() {}.getType())).thenReturn(orderOutDTOList);

        List<OrderOutDTO> result = orderService.getOrdersByUser(userId);

        assertEquals(2, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
        assertEquals("COMPLETED", result.get(1).getStatus());
        assertEquals(100f, result.get(0).getTotal());
        assertEquals(userId, result.get(0).getUserId());

        verify(userRepository, times(1)).findById(userId);
        verify(orderRepository, times(1)).findByUser(user);
    }

    @Test
    public void testGetOrdersByUserNotFound() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            orderService.getOrdersByUser(userId);
        });

        verify(userRepository, times(1)).findById(userId);
    }


    @Test
    public void testGet() throws OrderNotFoundException {
        Order order = new Order(1L, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", null , null);

        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));

        Order result = orderService.get(1L);

        assertEquals("PENDING", result.getStatus());
        assertEquals(100f, result.getTotal());
        assertEquals("address", result.getAddress());

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetNotFound() {
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.get(1L);
        });

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdate() throws UserNotFoundException, OrderNotFoundException {
        long userId = 1L;
        long orderId = 1L;
        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        Order order = new Order(orderId, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", user , null);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        Order result = orderService.update(userId, orderId, order);

        assertEquals(userId, result.getUser().getId());
        assertEquals("PENDING", result.getStatus());
        assertEquals(100f, result.getTotal());
        assertEquals("address", result.getAddress());
        assertEquals("PAYPAL", result.getPaymentMethod());

        verify(userRepository, times(1)).findById(userId);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(order);


    }

    @Test
    public void testUpdateUserNotFound() {
        long userId = 1L;
        long orderId = 1L;
        Order order = new Order(1L, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", null , null);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            orderService.update(userId, orderId, order);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(orderRepository, never()).findById(orderId);

    }

    @Test
    public void testUpdateOrderNotFound(){
        long userId = 1L;
        long orderId =1L;
        User user = new User(userId, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        Order order = new Order(1L, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", null , null);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.update(userId, orderId, order);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(orderRepository, times(1)).findById(1L);

    }

    @Test
    public void testDelete() throws OrderNotFoundException {
        long orderId = 1L;
        Order order = new Order(orderId, "PENDING", 100f, "address", LocalDate.now(), "PAYPAL", null , null);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));

        orderService.delete(orderId);

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    public void testDeleteNotFound(){
        long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.delete(orderId);
        });

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).delete(any(Order.class));
    }


}
