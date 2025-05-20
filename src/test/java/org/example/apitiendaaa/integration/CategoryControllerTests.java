package org.example.apitiendaaa.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.apitiendaaa.controller.CategoryController;
import org.example.apitiendaaa.domain.Category;
import org.example.apitiendaaa.domain.DTO.CategoryOutDTO;
import org.example.apitiendaaa.exception.CategoryNotFoundException;
import org.example.apitiendaaa.exception.ErrorResponse;
import org.example.apitiendaaa.service.CategoryService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    public void testAddCategoryOk() throws Exception {
        String endpointUrl = "/categories";
        Category category = new Category( 1, "category1", "description", LocalDate.now(), true, "image", null);
        when(categoryService.add(category)).thenReturn(category);

        String body = objectMapper.writeValueAsString(category);

        MvcResult result = mockMvc.perform(post(endpointUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        Category categoryResult = objectMapper.readValue(jsonResult, Category.class);

        assertNotNull(categoryResult);
        assertEquals(category.getName(), categoryResult.getName());
        assertEquals(201, result.getResponse().getStatus());

    }

    @Test
    public void testAddCategoryBadRequest() throws Exception {
        String endpointUrl = "/categories";
        Category category = new Category( 1, null, "description", LocalDate.now(), true, "image", null);


        String body = objectMapper.writeValueAsString(category);

        MvcResult result = mockMvc.perform(post(endpointUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResult = objectMapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(errorResult);
        assertEquals(400, result.getResponse().getStatus());
        assertEquals("Bad Request", errorResult.getMessage());


    }


    @Test
    public void testGetAllCategoriesOk() throws Exception {
        String endpointUrl = "/categories";
        List<CategoryOutDTO> categoryOutDTOList = List.of(new CategoryOutDTO(1, "category1", "description",  true, LocalDate.now()),
                new CategoryOutDTO(2, "category2", "description",  true, LocalDate.now()));


        when(categoryService.getAll("", null, "")).thenReturn(categoryOutDTOList);

        MvcResult result = mockMvc.perform(get(endpointUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        List<CategoryOutDTO> categoriesResult = objectMapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(categoriesResult);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(2, categoriesResult.size());
        assertEquals("category1", categoriesResult.get(0).getName());
        assertEquals("category2", categoriesResult.get(1).getName());
    }

    @Test
    public void testGetAllCategoriesByNameOk() throws Exception {
        String endpointUrl = "/categories";
        List<CategoryOutDTO> categoryOutDTOList = List.of(new CategoryOutDTO(1, "category1", "description",  true, LocalDate.now()),
                new CategoryOutDTO(2, "category1", "description2",  true, LocalDate.now()));


        when(categoryService.getAll("category1", null, "")).thenReturn(categoryOutDTOList);

        MvcResult result = mockMvc.perform(get(endpointUrl)
                        .param("name", "category1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        List<CategoryOutDTO> categoriesResult = objectMapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(categoriesResult);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(2, categoriesResult.size());
        assertEquals("category1", categoriesResult.get(0).getName());
        assertEquals("category1", categoriesResult.get(1).getName());
    }

    @Test
    public void testGetAllCategoriesByActiveOk() throws Exception {
        String endpointUrl = "/categories";
        List<CategoryOutDTO> categoryOutDTOList = List.of(new CategoryOutDTO(1, "category1", "description",  true, LocalDate.now()),
                new CategoryOutDTO(2, "category2", "description",  true, LocalDate.now()));


        when(categoryService.getAll("", true, "")).thenReturn(categoryOutDTOList);

        MvcResult result = mockMvc.perform(get(endpointUrl).param("active", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        List<CategoryOutDTO> categoriesResult = objectMapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(categoriesResult);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(2, categoriesResult.size());
        assertEquals("category1", categoriesResult.get(0).getName());
        assertEquals("category2", categoriesResult.get(1).getName());
        assertEquals(true, categoriesResult.get(0).getActive());
        assertEquals(true, categoriesResult.get(1).getActive());
    }

    @Test
    public void testGetAllCategoriesByCreationDateOk() throws Exception {
        String endpointUrl = "/categories";
        List<CategoryOutDTO> categoryOutDTOList = List.of(new CategoryOutDTO(1, "category1", "description",  true, LocalDate.now()),
                new CategoryOutDTO(2, "category2", "description",  true, LocalDate.now()));


        when(categoryService.getAll("", null, LocalDate.now().toString())).thenReturn(categoryOutDTOList);

        MvcResult result = mockMvc.perform(get(endpointUrl).param("creationDate", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        List<CategoryOutDTO> categoriesResult = objectMapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(categoriesResult);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(2, categoriesResult.size());
        assertEquals("category1", categoriesResult.get(0).getName());
        assertEquals("category2", categoriesResult.get(1).getName());
    }


    @Test
    public void testGetCategoryOk() throws Exception {
        String endpointUrl = "/categories/{categoryId}";
        Category category = new Category( 1, "category1", "description", LocalDate.now(), true, "image", null);

        when(categoryService.get(1)).thenReturn(category);

        MvcResult result = mockMvc.perform(get(endpointUrl, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        Category categoryResult = objectMapper.readValue(jsonResult, Category.class);

        assertNotNull(categoryResult);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("category1", categoryResult.getName());
    }

    @Test
    public void testGetCategoryNotFound() throws Exception {
        String endpointUrl = "/categories/{categoryId}";

        when(categoryService.get(1)).thenThrow(new CategoryNotFoundException());

        MvcResult result = mockMvc.perform(get(endpointUrl, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResult = objectMapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(errorResult);
        assertEquals(404, result.getResponse().getStatus());
        assertEquals("Categoría no encontrada", errorResult.getMessage());
    }

    @Test
    public void testUpdateCategoryOk() throws Exception {
        String endpointUrl = "/categories/{categoryId}";
        Category category = new Category( 1, "category1", "description", LocalDate.now(), true, "image", null);

        when(categoryService.update(1, category)).thenReturn(category);

        String body = objectMapper.writeValueAsString(category);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(endpointUrl, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        Category categoryResult = objectMapper.readValue(jsonResult, Category.class);

        assertNotNull(categoryResult);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("category1", categoryResult.getName());
    }

    @Test
    public void testUpdateCategoryBadRequest() throws Exception {
        String endpointUrl = "/categories/{categoryId}";
        Category category = new Category( 1, null, "description", LocalDate.now(), true, "image", null);

        String body = objectMapper.writeValueAsString(category);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(endpointUrl, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResult = objectMapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(errorResult);
        assertEquals(400, result.getResponse().getStatus());
        assertEquals("Bad Request", errorResult.getMessage());
    }

    @Test
    public void testUpdateCategoryNotFound() throws Exception {
        String endpointUrl = "/categories/{categoryId}";
        Category category = new Category( 1, "category1", "description", LocalDate.now(), true, "image", null);

        when(categoryService.update(1, category)).thenThrow(new CategoryNotFoundException());

        String body = objectMapper.writeValueAsString(category);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(endpointUrl, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResult = objectMapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(errorResult);
        assertEquals(404, result.getResponse().getStatus());
        assertEquals("Categoría no encontrada", errorResult.getMessage());
    }

    @Test
    public void testDeleteCategoryOk() throws Exception {
        String endpointUrl = "/categories/{categoryId}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(endpointUrl, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(204, result.getResponse().getStatus());


    }

    @Test
    public void testDeleteCategoryNotFound() throws Exception {
        String endpointUrl = "/categories/{categoryId}";

        doThrow(new CategoryNotFoundException()).when(categoryService).delete(1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(endpointUrl, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResult = objectMapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(errorResult);
        assertEquals(404, result.getResponse().getStatus());
        assertEquals("Categoría no encontrada", errorResult.getMessage());
    }




}
