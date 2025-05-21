package org.example.apitiendaaa.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.apitiendaaa.controller.OrderDetailController;
import org.example.apitiendaaa.domain.DTO.OrderDetailInDTO;
import org.example.apitiendaaa.domain.DTO.OrderDetailOutDTO;
import org.example.apitiendaaa.domain.Order;
import org.example.apitiendaaa.domain.OrderDetail;
import org.example.apitiendaaa.domain.Product;
import org.example.apitiendaaa.domain.User;
import org.example.apitiendaaa.exception.*;
import org.example.apitiendaaa.service.OrderDetailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderDetailController.class)
public class OrderDetailControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private OrderDetailService orderDetailService;

    @Test
    public void testAddOrderDetailReturnOk() throws  Exception{
        String url = "/users/{userId}/orders/{orderId}/details";
        long userId = 1L;
        long orderId = 1L;
        OrderDetailInDTO orderDetailIn = new OrderDetailInDTO(1,0f, 1L);
        Product product = new Product(orderDetailIn.getProductId(), null, "description", 10.00F, LocalDate.now(), true, "image", null, null);
        float subtotal = orderDetailIn.getQuantity() * product.getPrice();
        User user = new User(userId, "Juan", "email@email.com" , null, true, "address", "6666666", null, "latitude", "longitude", null);
        Order order = new Order(orderId, "PENDING", subtotal, "address", LocalDate.now(), "VISA", user, null);
        OrderDetail orderDetail = new OrderDetail(1L, orderDetailIn.getQuantity(), subtotal , orderDetailIn.getDiscount(), LocalDate.now(), product, order);

        when(orderDetailService.add(userId, orderId, orderDetailIn)).thenReturn(orderDetail);

        String body = objectMapper.writeValueAsString(orderDetailIn);


        MvcResult result = mockMvc.perform(post(url, userId, orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        OrderDetail expectedOrderDetail = objectMapper.readValue(response, OrderDetail.class);

        assertEquals(orderDetail.getId(), expectedOrderDetail.getId());
        assertEquals(orderDetail.getQuantity(), expectedOrderDetail.getQuantity());
        assertEquals(orderDetail.getSubtotal(), expectedOrderDetail.getSubtotal());
        assertEquals(201, result.getResponse().getStatus());


    }

    @Test
    public void testAddOrderDetailReturnOrderNotFound() throws  Exception{
        String url = "/users/{userId}/orders/{orderId}/details";
        long userId = 1L;
        long orderId = 1L;
        OrderDetailInDTO orderDetailIn = new OrderDetailInDTO(1,0f, 1L);




        when(orderDetailService.add(userId, orderId, orderDetailIn)).thenThrow(new OrderNotFoundException());

        String body = objectMapper.writeValueAsString(orderDetailIn);


        MvcResult result = mockMvc.perform(post(url, userId, orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(404, errorResponse.getErrorcode());
        assertEquals("Pedido no encontrado", errorResponse.getMessage());


    }

    @Test
    public void testAddOrderDetailReturnUserNotFound() throws  Exception{
        String url = "/users/{userId}/orders/{orderId}/details";
        long userId = 1L;
        long orderId = 1L;
        OrderDetailInDTO orderDetailIn = new OrderDetailInDTO(1,0f, 1L);


        when(orderDetailService.add(userId, orderId, orderDetailIn)).thenThrow(new UserNotFoundException());

        String body = objectMapper.writeValueAsString(orderDetailIn);


        MvcResult result = mockMvc.perform(post(url, userId, orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(404, errorResponse.getErrorcode());
        assertEquals("El usuario no existe", errorResponse.getMessage());

    }

    @Test
    public void testAddOrderDetailReturnProductNotFound() throws  Exception{
        String url = "/users/{userId}/orders/{orderId}/details";
        long userId = 1L;
        long orderId = 1L;
        OrderDetailInDTO orderDetailIn = new OrderDetailInDTO(1,0f, 1L);


        when(orderDetailService.add(userId, orderId, orderDetailIn)).thenThrow(new ProductNotFoundException());

        String body = objectMapper.writeValueAsString(orderDetailIn);


        MvcResult result = mockMvc.perform(post(url, userId, orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(404, errorResponse.getErrorcode());
        assertEquals("Producto no encontrado", errorResponse.getMessage());

    }

    @Test
    public void testAddOrderDetailReturnValidationError() throws  Exception{
        String url = "/users/{userId}/orders/{orderId}/details";
        long userId = 1L;
        long orderId = 1L;
        OrderDetailInDTO orderDetailIn = new OrderDetailInDTO(1,0f, null);


        String body = objectMapper.writeValueAsString(orderDetailIn);


        MvcResult result = mockMvc.perform(post(url, userId, orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(400, errorResponse.getErrorcode());
        assertEquals("Bad Request", errorResponse.getMessage());

    }

    @Test
    public void testGetDetailsReturnOk() throws Exception {
        String url = "/users/{userId}/orders/{orderId}/details";
        long userId = 1L;
        long orderId = 1L;
        List<OrderDetailOutDTO> orderDetails = List.of(new OrderDetailOutDTO(1L, orderId, 1L, "Product 1", 2, 0f, 20f),
                new OrderDetailOutDTO(2L, orderId, 2L, "Product 2", 1, 0f, 10f));

        when(orderDetailService.getAll(userId, orderId, null, null, null)).thenReturn(orderDetails);

        MvcResult result = mockMvc.perform((get(url, userId, orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        List<OrderDetailOutDTO> expectedOrderDetails = objectMapper.readValue(response, new TypeReference<>() {});

        assertEquals(orderDetails.size(), expectedOrderDetails.size());
        assertEquals(orderDetails.get(0).getId(), expectedOrderDetails.get(0).getId());
        assertEquals(orderDetails.get(0).getQuantity(), expectedOrderDetails.get(0).getQuantity());
        assertEquals(200, result.getResponse().getStatus());


    }

    @Test
    public void testGetDetailsByDiscountReturnOk() throws Exception {
        String url = "/users/{userId}/orders/{orderId}/details";
        long userId = 1L;
        long orderId = 1L;
        List<OrderDetailOutDTO> orderDetails = List.of(new OrderDetailOutDTO(1L, orderId, 1L, "Product 1", 2, 10f, 20f),
                new OrderDetailOutDTO(2L, orderId, 2L, "Product 2", 1, 10f, 10f));

        when(orderDetailService.getAll(userId, orderId, 10f, null, null)).thenReturn(orderDetails);

        MvcResult result = mockMvc.perform((get(url, userId, orderId).queryParam("discount", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        List<OrderDetailOutDTO> expectedOrderDetails = objectMapper.readValue(response, new TypeReference<>() {});

        assertEquals(orderDetails.size(), expectedOrderDetails.size());
        assertEquals(orderDetails.get(0).getId(), expectedOrderDetails.get(0).getId());
        assertEquals(orderDetails.get(0).getQuantity(), expectedOrderDetails.get(0).getQuantity());
        assertEquals(orderDetails.get(0).getDiscount(), expectedOrderDetails.get(0).getDiscount());
        assertEquals(200, result.getResponse().getStatus());


    }

    @Test
    public void testGetDetailsByQuantityReturnOk() throws Exception {
        String url = "/users/{userId}/orders/{orderId}/details";
        long userId = 1L;
        long orderId = 1L;
        List<OrderDetailOutDTO> orderDetails = List.of(new OrderDetailOutDTO(1L, orderId, 1L, "Product 1", 2, 0f, 20f),
                new OrderDetailOutDTO(2L, orderId, 2L, "Product 2", 2, 0f, 10f));

        when(orderDetailService.getAll(userId, orderId, null, 2f, null)).thenReturn(orderDetails);

        MvcResult result = mockMvc.perform((get(url, userId, orderId).queryParam("quantity", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        List<OrderDetailOutDTO> expectedOrderDetails = objectMapper.readValue(response, new TypeReference<>() {});

        assertEquals(orderDetails.size(), expectedOrderDetails.size());
        assertEquals(orderDetails.get(0).getId(), expectedOrderDetails.get(0).getId());
        assertEquals(orderDetails.get(0).getQuantity(), expectedOrderDetails.get(0).getQuantity());
        assertEquals(200, result.getResponse().getStatus());


    }

    @Test
    public void testGetDetailsBySubtotalReturnOk() throws Exception {
        String url = "/users/{userId}/orders/{orderId}/details";
        long userId = 1L;
        long orderId = 1L;
        List<OrderDetailOutDTO> orderDetails = List.of(new OrderDetailOutDTO(1L, orderId, 1L, "Product 1", 2, 0f, 20f),
                new OrderDetailOutDTO(2L, orderId, 2L, "Product 2", 1, 0f, 20f));

        when(orderDetailService.getAll(userId, orderId, null, null, 20f)).thenReturn(orderDetails);

        MvcResult result = mockMvc.perform((get(url, userId, orderId).queryParam("subtotal", "20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        List<OrderDetailOutDTO> expectedOrderDetails = objectMapper.readValue(response, new TypeReference<>() {});

        assertEquals(orderDetails.size(), expectedOrderDetails.size());
        assertEquals(orderDetails.get(0).getId(), expectedOrderDetails.get(0).getId());
        assertEquals(orderDetails.get(0).getQuantity(), expectedOrderDetails.get(0).getQuantity());
        assertEquals(200, result.getResponse().getStatus());


    }

    @Test
    public void testGetDetailsReturnOrderNotFound() throws Exception {
        String url = "/users/{userId}/orders/{orderId}/details";
        long userId = 1L;
        long orderId = 1L;

        when(orderDetailService.getAll(userId, orderId, null, null, null)).thenThrow(new OrderNotFoundException());

        MvcResult result = mockMvc.perform((get(url, userId, orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)))
                .andExpect(status().isNotFound())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(404, errorResponse.getErrorcode());
        assertEquals("Pedido no encontrado", errorResponse.getMessage());


    }

    @Test
    public void testGetDetailsReturnUserNotFound() throws Exception {
        String url = "/users/{userId}/orders/{orderId}/details";
        long userId = 1L;
        long orderId = 1L;

        when(orderDetailService.getAll(userId, orderId, null, null, null)).thenThrow(new UserNotFoundException());

        MvcResult result = mockMvc.perform((get(url, userId, orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)))
                .andExpect(status().isNotFound())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(404, errorResponse.getErrorcode());
        assertEquals("El usuario no existe", errorResponse.getMessage());


    }

    @Test
    public void testUpdateOrderDetailReturnOk() throws Exception {
        String url = "/users/{userId}/orders/{orderId}/details/{detailId}";
        long userId = 1L;
        long orderId = 1L;
        long detailId = 1L;
        OrderDetailInDTO orderDetailIn = new OrderDetailInDTO(1,0f, 1L);
        Product product = new Product(orderDetailIn.getProductId(), null, "description", 10.00F, LocalDate.now(), true, "image", null, null);
        float subtotal = orderDetailIn.getQuantity() * product.getPrice();
        OrderDetailOutDTO orderDetailOut = new OrderDetailOutDTO(detailId, orderId, 1L, "Product 1", orderDetailIn.getQuantity(), orderDetailIn.getDiscount(), subtotal);

        when(orderDetailService.update(userId, orderId, detailId, orderDetailIn)).thenReturn(orderDetailOut);

        String body = objectMapper.writeValueAsString(orderDetailIn);

        MvcResult result = mockMvc.perform(put(url, userId, orderId, detailId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        OrderDetailOutDTO expectedOrderDetail = objectMapper.readValue(response, OrderDetailOutDTO.class);
        assertEquals(orderDetailOut.getId(), expectedOrderDetail.getId());
        assertEquals(orderDetailOut.getQuantity(), expectedOrderDetail.getQuantity());
        assertEquals(orderDetailOut.getSubtotal(), expectedOrderDetail.getSubtotal());
        assertEquals(200, result.getResponse().getStatus());

    }

    @Test
    public void testUpdateOrderDetailReturnOrderNotFound() throws Exception {
        String url = "/users/{userId}/orders/{orderId}/details/{detailId}";
        long userId = 1L;
        long orderId = 1L;
        long detailId = 1L;
        OrderDetailInDTO orderDetailIn = new OrderDetailInDTO(1,0f, 1L);


        when(orderDetailService.update(userId, orderId, detailId, orderDetailIn)).thenThrow(new OrderNotFoundException());

        String body = objectMapper.writeValueAsString(orderDetailIn);

        MvcResult result = mockMvc.perform(put(url, userId, orderId, detailId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);
        assertEquals(404, errorResponse.getErrorcode());
        assertEquals("Pedido no encontrado", errorResponse.getMessage());
    }

    @Test
    public void testUpdateOrderDetailReturnUserNotFound() throws Exception {
        String url = "/users/{userId}/orders/{orderId}/details/{detailId}";
        long userId = 1L;
        long orderId = 1L;
        long detailId = 1L;
        OrderDetailInDTO orderDetailIn = new OrderDetailInDTO(1,0f, 1L);


        when(orderDetailService.update(userId, orderId, detailId, orderDetailIn)).thenThrow(new UserNotFoundException());

        String body = objectMapper.writeValueAsString(orderDetailIn);

        MvcResult result = mockMvc.perform(put(url, userId, orderId, detailId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);
        assertEquals(404, errorResponse.getErrorcode());
        assertEquals("El usuario no existe", errorResponse.getMessage());
    }

    @Test
    public void testUpdateOrderDetailReturnProductNotFound() throws Exception {
        String url = "/users/{userId}/orders/{orderId}/details/{detailId}";
        long userId = 1L;
        long orderId = 1L;
        long detailId = 1L;
        OrderDetailInDTO orderDetailIn = new OrderDetailInDTO(1,0f, 1L);


        when(orderDetailService.update(userId, orderId, detailId, orderDetailIn)).thenThrow(new ProductNotFoundException());

        String body = objectMapper.writeValueAsString(orderDetailIn);

        MvcResult result = mockMvc.perform(put(url, userId, orderId, detailId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);
        assertEquals(404, errorResponse.getErrorcode());
        assertEquals("Producto no encontrado", errorResponse.getMessage());
    }

    @Test
    public void testUpdateOrderDetailReturnValidationError() throws Exception {
        String url = "/users/{userId}/orders/{orderId}/details/{detailId}";
        long userId = 1L;
        long orderId = 1L;
        long detailId = 1L;
        OrderDetailInDTO orderDetailIn = new OrderDetailInDTO(1,0f, null);

        String body = objectMapper.writeValueAsString(orderDetailIn);

        MvcResult result = mockMvc.perform(put(url, userId, orderId, detailId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(400, errorResponse.getErrorcode());
        assertEquals("Bad Request", errorResponse.getMessage());

    }



    @Test
    public void testDeleteOrderDetailReturnOk() throws Exception {
        String url = "/users/{userId}/orders/{orderId}/details/{detailId}";
        long userId = 1L;
        long orderId = 1L;
        long detailId = 1L;

        MvcResult result = mockMvc.perform(delete(url, userId, orderId, detailId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(204, result.getResponse().getStatus());

    }

    @Test
    public void testDeleteOrderDetailReturnOrderNotFound() throws Exception {
        String url = "/users/{userId}/orders/{orderId}/details/{detailId}";
        long userId = 1L;
        long orderId = 1L;
        long detailId = 1L;

        doThrow(new OrderNotFoundException()).when(orderDetailService).delete(userId, orderId, detailId);

        MvcResult result = mockMvc.perform(delete(url, userId, orderId, detailId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(404, errorResponse.getErrorcode());
        assertEquals("Pedido no encontrado", errorResponse.getMessage());

    }

    @Test
    public void testDeleteOrderDetailReturnUserNotFound() throws Exception {
        String url = "/users/{userId}/orders/{orderId}/details/{detailId}";
        long userId = 1L;
        long orderId = 1L;
        long detailId = 1L;

        doThrow(new UserNotFoundException()).when(orderDetailService).delete(userId, orderId, detailId);

        MvcResult result = mockMvc.perform(delete(url, userId, orderId, detailId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(404, errorResponse.getErrorcode());
        assertEquals("El usuario no existe", errorResponse.getMessage());

    }

    @Test
    public void testDeleteOrderDetailReturnOrderDetailNotFound() throws Exception {
        String url = "/users/{userId}/orders/{orderId}/details/{detailId}";
        long userId = 1L;
        long orderId = 1L;
        long detailId = 1L;

        doThrow(new OrderDetailNotFoundException(detailId)).when(orderDetailService).delete(userId, orderId, detailId);

        MvcResult result = mockMvc.perform(delete(url, userId, orderId, detailId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(404, errorResponse.getErrorcode());
        assertEquals("OrderDetail con id: 1 no encontrado.", errorResponse.getMessage());
    }




}
