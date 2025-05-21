package org.example.apitiendaaa.integration;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.apitiendaaa.controller.OrderController;
import org.example.apitiendaaa.domain.DTO.OrderOutDTO;
import org.example.apitiendaaa.domain.Order;
import org.example.apitiendaaa.domain.User;
import org.example.apitiendaaa.exception.ErrorResponse;
import org.example.apitiendaaa.exception.OrderNotFoundException;
import org.example.apitiendaaa.exception.UserNotFoundException;
import org.example.apitiendaaa.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectmapper;

    @MockitoBean
    private OrderService orderService;


    @Test
    public void testGetAllReturnOk() throws Exception {
        String url = "/orders";
        List<OrderOutDTO> orderList = List.of( new OrderOutDTO(1L,"PENDING", 10.00F, 1, "address", LocalDate.now().toString(), "VISA"),
                new OrderOutDTO(2L,"SHIPPED", 10.00F, 1, "address", LocalDate.now().toString(), "PAYPAL"));

        when(orderService.getAll("","","")).thenReturn(orderList);

        MvcResult result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();


        List<OrderOutDTO> expectedList = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(expectedList);
        assertEquals(2, expectedList.size());
        assertEquals(orderList.get(0).getId(), expectedList.get(0).getId());

        }

    @Test
    public void testGetAllByStatusReturnOk() throws Exception {
        String url = "/orders";
        List<OrderOutDTO> orderList = List.of( new OrderOutDTO(1L,"PENDING", 10.00F, 1, "address", LocalDate.now().toString(), "VISA"),
                new OrderOutDTO(2L,"PENDING", 10.00F, 1, "address", LocalDate.now().toString(), "PAYPAL"));

        when(orderService.getAll("PENDING","","")).thenReturn(orderList);

        MvcResult result = mockMvc.perform(get(url).queryParam("status", "PENDING")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();


        List<OrderOutDTO> expectedList = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(expectedList);
        assertEquals(2, expectedList.size());
        assertEquals(orderList.get(0).getId(), expectedList.get(0).getId());
        assertEquals(orderList.get(0).getStatus(), expectedList.get(0).getStatus());

    }

    @Test
    public void testGetAlByPaymentMethodlReturnOk() throws Exception {
        String url = "/orders";
        List<OrderOutDTO> orderList = List.of( new OrderOutDTO(1L,"PENDING", 10.00F, 1, "address", LocalDate.now().toString(), "VISA"),
                new OrderOutDTO(2L,"SHIPPED", 10.00F, 1, "address", LocalDate.now().toString(), "VISA"));

        when(orderService.getAll("","VISA","")).thenReturn(orderList);

        MvcResult result = mockMvc.perform(get(url).queryParam("paymentMethod", "VISA")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();


        List<OrderOutDTO> expectedList = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(expectedList);
        assertEquals(2, expectedList.size());
        assertEquals(orderList.get(0).getId(), expectedList.get(0).getId());
        assertEquals(orderList.get(0).getPaymentMethod(), expectedList.get(0).getPaymentMethod());

    }

    @Test
    public void testGetAllByCreationDateReturnOk() throws Exception {
        String url = "/orders";
        List<OrderOutDTO> orderList = List.of( new OrderOutDTO(1L,"PENDING", 10.00F, 1, "address", LocalDate.now().toString(), "VISA"),
                new OrderOutDTO(2L,"SHIPPED", 10.00F, 1, "address", LocalDate.now().toString(), "PAYPAL"));

        when(orderService.getAll("","",LocalDate.now().toString())).thenReturn(orderList);

        MvcResult result = mockMvc.perform(get(url).queryParam("creationDate", LocalDate.now().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();


        List<OrderOutDTO> expectedList = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(expectedList);
        assertEquals(2, expectedList.size());
        assertEquals(orderList.get(0).getId(), expectedList.get(0).getId());
        assertEquals(orderList.get(0).getCreationDate(), expectedList.get(0).getCreationDate());

    }

    @Test
    public void testAddOrderReturnOk() throws Exception {
        String url = "/users/{userId}/orders";
        long userId = 1L;
        User user = new User(userId, "Juan", "email@email.com", null, true, "address", "666666", LocalDate.now(), "latitude", "longitude", null);
        Order order= new Order(1L,"PENDING", 10.00F, "address", LocalDate.now(), "VISA", user, null);
        user.setOrders(List.of(order));


        when(orderService.add(userId, order)).thenReturn(order);

        String body = objectmapper.writeValueAsString(order);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(url, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        Order expectedOrder = objectmapper.readValue(jsonResult, Order.class);

        assertNotNull(expectedOrder);

        assertEquals(order.getId(), expectedOrder.getId());
        assertEquals(order.getStatus(), expectedOrder.getStatus());
        assertEquals(201, result.getResponse().getStatus());

    }

    @Test
    public void testAddOrderReturnValidationError() throws Exception {
        String url = "/users/{userId}/orders";
        long userId = 1L;
        User user = new User(userId, "Juan", "email@email.com", null, true, "address", "666666", LocalDate.now(), "latitude", "longitude", null);
        Order order= new Order(1L,null, 10.00F, "address", LocalDate.now(), "VISA", user, null);
        user.setOrders(List.of(order));


        String body = objectmapper.writeValueAsString(order);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(url, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectmapper.readValue(jsonResult, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals("Bad Request", errorResponse.getMessage());
        assertEquals(400, result.getResponse().getStatus());

    }


    @Test
    public void testAddOrderReturnUserNotFound() throws Exception {
        String url = "/users/{userId}/orders";
        long userId = 1L;
        Order order= new Order(1L,"PENDING", 10.00F, "address", LocalDate.now(), "VISA", null, null);



        when(orderService.add(userId, order)).thenThrow(new UserNotFoundException());

        String body = objectmapper.writeValueAsString(order);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(url, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectmapper.readValue(jsonResult, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals("El usuario no existe", errorResponse.getMessage());
        assertEquals(404, result.getResponse().getStatus());

    }

    @Test
    public void testGetOrdersByUserReturnOk() throws Exception {
        String url = "/users/{userId}/orders";
        long userId = 1L;
        List<OrderOutDTO> orderList = List.of( new OrderOutDTO(1L,"PENDING", 10.00F, 1, "address", LocalDate.now().toString(), "VISA"),
                new OrderOutDTO(2L,"SHIPPED", 10.00F, 1, "address", LocalDate.now().toString(), "PAYPAL"));

        when(orderService.getOrdersByUser(userId)).thenReturn(orderList);

        MvcResult result = mockMvc.perform(get(url, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        List<OrderOutDTO> expectedList = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(expectedList);
        assertEquals(2, expectedList.size());
        assertEquals(orderList.get(0).getId(), expectedList.get(0).getId());
        assertEquals(200, result.getResponse().getStatus());


    }

    @Test
    public void testGetOrdersByUserReturnUserNotFound() throws Exception {
        String url = "/users/{userId}/orders";
        long userId = 1L;


        when(orderService.getOrdersByUser(userId)).thenThrow(new UserNotFoundException());

        MvcResult result = mockMvc.perform(get(url, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectmapper.readValue(jsonResult, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals("El usuario no existe", errorResponse.getMessage());
        assertEquals(404, result.getResponse().getStatus());

    }

    @Test
    public void testGetOrderReturnOk() throws Exception {
        String url = "/orders/{orderId}";
        long orderId = 1L;
        Order order = new Order(orderId, "PENDING", 10.00F, "address", LocalDate.now(), "VISA", null, null);

        when(orderService.get(orderId)).thenReturn(order);

        MvcResult result = mockMvc.perform(get(url, orderId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        Order expectedOrder = objectmapper.readValue(jsonResult, Order.class);

        assertNotNull(expectedOrder);
        assertEquals(order.getId(), expectedOrder.getId());
        assertEquals(200, result.getResponse().getStatus());

    }

    @Test
    public void testGetOrderReturnOrderNotFound() throws Exception {
        String url = "/orders/{orderId}";
        long orderId = 1L;

        when(orderService.get(orderId)).thenThrow(new OrderNotFoundException());

        MvcResult result = mockMvc.perform(get(url, orderId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectmapper.readValue(jsonResult, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals("Pedido no encontrado", errorResponse.getMessage());
        assertEquals(404, result.getResponse().getStatus());

    }

    @Test
    public void testUpdateOrderReturnOk() throws Exception {
        String url = "/users/{userId}/orders/{orderId}";
        long userId = 1L;
        long orderId = 1L;
        User user = new User(userId, "Juan", "email@email.com", null, true, "address", "666666", LocalDate.now(), "latitude", "longitude", null);
        Order order= new Order(orderId,"PENDING", 10.00F, "address", LocalDate.now(), "VISA", user, null);
        user.setOrders(List.of(order));


        when(orderService.update(eq(userId), eq(orderId), any(Order.class))).thenReturn(order);

        String body = objectmapper.writeValueAsString(order);

        MvcResult result = mockMvc.perform(put(url, userId, orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();


        Order expectedOrder = objectmapper.readValue(jsonResult, Order.class);

        assertNotNull(expectedOrder);
        assertEquals(order.getId(), expectedOrder.getId());
        assertEquals(order.getStatus(), expectedOrder.getStatus());
        assertEquals(200, result.getResponse().getStatus());


    }

    @Test
    public void testUpdateOrderReturnUserNotFound() throws Exception {
        String url = "/users/{userId}/orders/{orderId}";
        long userId = 1L;
        long orderId = 1L;
        Order ordertoUpdate = new Order(orderId,"PENDING", 10.00F, "address", LocalDate.now(), "VISA", null, null);


        when(orderService.update(eq(userId), eq(orderId), any(Order.class))).thenThrow(new UserNotFoundException());

        String body = objectmapper.writeValueAsString(ordertoUpdate);

        MvcResult result = mockMvc.perform(put(url, userId, orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectmapper.readValue(jsonResult, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals("El usuario no existe", errorResponse.getMessage());
        assertEquals(404, result.getResponse().getStatus());


    }

    @Test
    public void testUpdateOrderReturnOrderNotFound() throws Exception {
        String url = "/users/{userId}/orders/{orderId}";
        long userId = 1L;
        long orderId = 1L;
        Order ordertoUpdate = new Order(orderId,"PENDING", 10.00F, "address", LocalDate.now(), "VISA", null, null);


        when(orderService.update(eq(userId), eq(orderId), any(Order.class))).thenThrow(new OrderNotFoundException());

        String body = objectmapper.writeValueAsString(ordertoUpdate);

        MvcResult result = mockMvc.perform(put(url, userId, orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectmapper.readValue(jsonResult, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals("Pedido no encontrado", errorResponse.getMessage());
        assertEquals(404, result.getResponse().getStatus());


    }

    @Test
    public void testUpdateOrderReturnValidationError() throws Exception {
        String url = "/users/{userId}/orders/{orderId}";
        long userId = 1L;
        long orderId = 1L;
        Order ordertoUpdate = new Order(orderId, null, 10.00F, "address", LocalDate.now(), "VISA", null, null);

        String body = objectmapper.writeValueAsString(ordertoUpdate);

        MvcResult result = mockMvc.perform(put(url, userId, orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectmapper.readValue(jsonResult, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals("Bad Request", errorResponse.getMessage());
        assertEquals(400, result.getResponse().getStatus());

    }

    @Test
    public void testDeleteOrderReturnOk() throws Exception {
        String url = "/orders/{orderId}";
        long orderId = 1L;

        MvcResult result = mockMvc.perform(delete(url, orderId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(204, result.getResponse().getStatus());
    }

    @Test
    public void testDeleteOrderReturnNotFound() throws Exception{
        String url = "/orders/{orderId}";
        long orderId = 1L;

        doThrow(new OrderNotFoundException()).when(orderService).delete(orderId);

        MvcResult result = mockMvc.perform(delete(url, orderId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectmapper.readValue(jsonResult, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals("Pedido no encontrado", errorResponse.getMessage());
        assertEquals(404, result.getResponse().getStatus());

    }



    }


