package org.example.apitiendaaa.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.apitiendaaa.controller.ProductController;
import org.example.apitiendaaa.domain.DTO.ProductInDTO;
import org.example.apitiendaaa.domain.DTO.ProductOutDTO;
import org.example.apitiendaaa.domain.Product;
import org.example.apitiendaaa.exception.CategoryNotFoundException;
import org.example.apitiendaaa.exception.ErrorResponse;
import org.example.apitiendaaa.exception.ProductNotFoundException;
import org.example.apitiendaaa.service.ProductService;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectmapper;
    @MockitoBean
    private ProductService productService;


    @Test
    public void testGetAllProductsReturnOk() throws Exception {

        String url = "/products";

        List<ProductOutDTO> productOutDTOS = List.of(new ProductOutDTO(1, "product1", 10, 10, LocalDate.now(), "description2"),
                new ProductOutDTO(1, "product2", 20, 20, LocalDate.now(),"description2"));

        when(productService.getAll("", "", null)).thenReturn(productOutDTOS);

        MvcResult result = mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<ProductOutDTO> productResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(productResult);
        assertEquals(2, productResult.size());
        assertEquals("product1", productResult.get(0).getName());



    }

    @Test
    public void testGetAllProductsByNameReturnOk() throws Exception {
        String url = "/products";

        List<ProductOutDTO> productOutDTOS = List.of(new ProductOutDTO(1, "product1", 10, 10, LocalDate.now(), "description2"),
                new ProductOutDTO(1, "product1", 20, 20, LocalDate.now(),"description2"));

        when(productService.getAll("product1", "", null)).thenReturn(productOutDTOS);

        MvcResult result = mockMvc.perform(get(url).queryParam("name", "product1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<ProductOutDTO> productResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(productResult);
        assertEquals(2, productResult.size());
        assertEquals("product1", productResult.get(0).getName());
        assertEquals(productOutDTOS.get(0).getName(), productResult.get(0).getName());


    }

    @Test
    public void testGetAllProductsByPriceReturnOk() throws Exception {

        String url = "/products";

        List<ProductOutDTO> productOutDTOS = List.of(new ProductOutDTO(1, "product1", 10, 10, LocalDate.now(), "description2"),
                new ProductOutDTO(1, "product2", 10, 20, LocalDate.now(),"description2"));

        when(productService.getAll("", "10", null)).thenReturn(productOutDTOS);

        MvcResult result = mockMvc.perform(get(url).queryParam("price", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<ProductOutDTO> productResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(productResult);
        assertEquals(2, productResult.size());
        assertEquals("product1", productResult.get(0).getName());
        assertEquals(productOutDTOS.get(0).getName(), productResult.get(0).getName());
        assertEquals(10, productResult.get(0).getPrice());



    }

    @Test
    public void testGetAllProductsByActiveReturnOk() throws Exception {

        String url = "/products";
        List<ProductOutDTO> productOutDTOS = List.of(new ProductOutDTO(1, "product1", 10, 10, LocalDate.now(), "description2"),
                new ProductOutDTO(1, "product2", 20, 20, LocalDate.now(),"description2"));

        when(productService.getAll("", "", true)).thenReturn(productOutDTOS);

        MvcResult result = mockMvc.perform(get(url).queryParam("active", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<ProductOutDTO> productResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(productResult);
        assertEquals(2, productResult.size());
        assertEquals("product1", productResult.get(0).getName());
        assertEquals(productOutDTOS.get(0).getName(), productResult.get(0).getName());
        assertEquals(10, productResult.get(0).getPrice());



    }


    @Test
    public void getByCategoryReturnOk() throws Exception {
        String url = "/categories/{categoryId}/products";
        long categoryId = 1;
        List<ProductOutDTO> productOutDTOS = List.of(new ProductOutDTO(1, "product1", 10, 10, LocalDate.now(), "description2"),
                new ProductOutDTO(1, "product2", 20, 20, LocalDate.now(),"description2"));
        when(productService.getByCategory(categoryId)).thenReturn(productOutDTOS);

        MvcResult result = mockMvc.perform(get(url, categoryId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        List<ProductOutDTO> productResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(productResult);
        assertEquals(2, productResult.size());
        assertEquals("product1", productResult.get(0).getName());
        assertEquals(productOutDTOS.get(0).getName(), productResult.get(0).getName());
        assertEquals(10, productResult.get(0).getPrice());

    }

    @Test
    public void getByCategoryReturnCategoryNotFound() throws Exception {
        long categoryId = 1;

        String url = "/categories/{categoryId}/products";

        when(productService.getByCategory(categoryId)).thenThrow(new CategoryNotFoundException());

        MvcResult result = mockMvc.perform(get(url, categoryId)
                            .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(errorResult);
        assertEquals("Categoría no encontrada", errorResult.getMessage());
        assertEquals(404, errorResult.getErrorcode());

    }




    @Test

    public void testAddProductReturnOk() throws Exception {
        String url = "/categories/{categoryId}/products";

        long categoryId = 1L;

        Product product = new Product(1, "product1", "description", 10F, LocalDate.now(), true, "image", null, null);

        when(productService.add(1, product)).thenReturn(product);

        String requestBody = objectmapper.writeValueAsString(product);


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(url,categoryId)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn();


        String jsonResult = result.getResponse().getContentAsString();

        Product productResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(productResult);

        assertEquals("product1", productResult.getName());
        assertEquals(10F, productResult.getPrice());
        assertEquals("description", productResult.getDescription());


    }

    @Test
    public void testAddProductCategoryNotFound() throws Exception {

        String url = "/categories/{categoryId}/products";
        Product product = new Product(1, "product1", "description", 10F, LocalDate.now(), true, "image", null, null);

        when(productService.add(1, product)).thenThrow(new CategoryNotFoundException());


        String requestBody = objectmapper.writeValueAsString(product);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(url,1)
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
        String url = "/categories/{categoryId}/products";

        Product product = new Product(1, null, "description", -10F, LocalDate.now(), true, "image", null, null);

        when(productService.add(1, product)).thenReturn(product);

        String requestBody = objectmapper.writeValueAsString(product);


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(url,1)
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
    public void testUpdateProductReturnOk() throws Exception {
        String url = "/categories/{categoryId}/products/{productId}";
        long categoryId = 1L;
        long productId = 1L;
        ProductInDTO productIn = new ProductInDTO("product1", "description", 10F, LocalDate.now(), true);
        Product productOut = new Product(1, "product1", "description", 10F, LocalDate.now(), true, "image", null, null);

        when(productService.update(categoryId, productId, productIn)).thenReturn(productOut);

        String requestBody = objectmapper.writeValueAsString(productIn);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(url, categoryId, productId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        Product productResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(productResult);
        assertEquals(productOut.getName(), productResult.getName());
        assertEquals(productOut.getPrice(), productResult.getPrice());
        assertEquals(productOut.getDescription(), productResult.getDescription());
        assertEquals(200, result.getResponse().getStatus());


    }

    @Test
    public void testUpdateProductReturnProductNotFound() throws Exception {
        String url = "/categories/{categoryId}/products/{productId}";
        long categoryId = 1L;
        long productId = 1L;
        ProductInDTO productIn = new ProductInDTO("product1", "description", 10F, LocalDate.now(), true);


        when(productService.update(categoryId, productId, productIn)).thenThrow(new ProductNotFoundException());

        String requestBody = objectmapper.writeValueAsString(productIn);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(url, categoryId, productId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getErrorcode());
        assertEquals("Producto no encontrado", errorResponse.getMessage());

    }

    @Test
    public void testUpdateProductReturnValidationError() throws Exception {
        String url = "/categories/{categoryId}/products/{productId}";
        long categoryId = 1L;
        long productId = 1L;
        ProductInDTO productIn = new ProductInDTO(null, "description", 10F, LocalDate.now(), true);


        String requestBody = objectmapper.writeValueAsString(productIn);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(url, categoryId, productId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getErrorcode());
        assertEquals("Bad Request", errorResponse.getMessage());
    }

    @Test
    public void testDeleteProductReturnOk() throws Exception {

        String url = "/products/{productId}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(url, 1))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(204, result.getResponse().getStatus());

    }

    @Test
    public void testDeleteProductNotFound() throws Exception {

        String url = "/products/{productId}";

        doThrow(new ProductNotFoundException()).when(productService).delete(1L);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(url, 1))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(errorResult);
        assertEquals("Producto no encontrado", errorResult.getMessage());
        assertEquals(404, errorResult.getErrorcode());

    }
}
