package org.example.apitiendaaa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.apitiendaaa.controller.ProductController;
import org.example.apitiendaaa.domain.DTO.ProductOutDTO;
import org.example.apitiendaaa.domain.Product;
import org.example.apitiendaaa.exception.CategoryNotFoundException;
import org.example.apitiendaaa.exception.ErrorResponse;
import org.example.apitiendaaa.service.ProductService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectmapper;
    @MockBean
    private ProductService productService;


    @Test
    public void testGetAllProductsReturnOk() throws Exception {
        List<ProductOutDTO> productOutDTOS = List.of(new ProductOutDTO(1, "product1", 10, 10, LocalDate.now(), "description2"),
                new ProductOutDTO(1, "product2", 20, 20, LocalDate.now(),"description2"));

        when(productService.getAll("", "", null)).thenReturn(productOutDTOS);

        MvcResult result = mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<ProductOutDTO> productResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(productResult);
        assertEquals(2, productResult.size());
        assertEquals("product1", productResult.get(0).getName());



    }

    @Test

    public void testAddProductReturnOk() throws Exception {

        Product product = new Product(1, "product1", "description", 10F, LocalDate.now(), true, "image", null, null);

        when(productService.add(1, product)).thenReturn(product);

        String requestBody = objectmapper.writeValueAsString(product);


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/categories/{categoryId}/products",1)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(null))
                .andReturn();


        String jsonResult = result.getResponse().getContentAsString();

        Product productResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(productResult);

        assertEquals("product1", productResult.getName());
        assertEquals(10F, productResult.getPrice());


    }

    @Test
    public void testAddProductCategoryNotFound() throws Exception {

        Product product = new Product(1, "product1", "description", 10F, LocalDate.now(), true, "image", null, null);

        when(productService.add(1, product)).thenThrow(new CategoryNotFoundException());


        String requestBody = objectmapper.writeValueAsString(product);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/categories/{categoryId}/products",1)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(errorResult);

        assertEquals("Categoría no encontrada", errorResult.getMessage());
        assertEquals(404, errorResult.getErrorcode());



    }

    @Test
    public void testAddProductValidationError() throws Exception {

        Product product = new Product(1, null, "description", -10F, LocalDate.now(), true, "image", null, null);

        when(productService.add(1, product)).thenReturn(product);

        String requestBody = objectmapper.writeValueAsString(product);


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/categories/{categoryId}/products",1)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonresponse = result.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectmapper.readValue(jsonresponse, new TypeReference<>() {});

        assertNotNull(errorResponse);

        assertEquals(400, errorResponse.getErrorcode());
        assertEquals("Bad Request", errorResponse.getMessage());
        assertEquals("El precio debe ser mayor a 0", errorResponse.getErrorMessages().get("price"));


    }

    @Test
    public void testDeleteProductReturnOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{productId}", 1))
                .andExpect(status().isNoContent())
                .andReturn();

    }

    @Test
    @Disabled
    public void testDeleteProductNotFound() throws Exception {


    }
}
