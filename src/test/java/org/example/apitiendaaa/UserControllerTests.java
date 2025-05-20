package org.example.apitiendaaa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.apitiendaaa.controller.UserController;
import org.example.apitiendaaa.domain.DTO.UserOutDTO;
import org.example.apitiendaaa.domain.User;
import org.example.apitiendaaa.exception.ErrorResponse;
import org.example.apitiendaaa.exception.UserNotFoundException;
import org.example.apitiendaaa.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectmapper;
    @MockitoBean
    private UserService userService;

    @Test
    public void testGetAllReturnOk() throws Exception {
    String endpointUrl = "/users";
    List <UserOutDTO> users = List.of(new UserOutDTO(1, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude"),
            new UserOutDTO(2, "Pedro", "email", null, true, "address", "phone",  null, "latitude", "longitude"));

     when(userService.getAll("","",false)).thenReturn(users);


        MvcResult result = mockMvc.perform(get(endpointUrl)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<UserOutDTO> userResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(userResult);
        assertEquals(2, userResult.size());
        assertEquals("Juan", userResult.get(0).getName());
        assertEquals("Pedro", userResult.get(1).getName());
    }



    @Test
    public void testGetAllByNameReturnOk() throws Exception {
        String endpointUrl = "/users";
        List <UserOutDTO> users = List.of(new UserOutDTO(1, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude"),
                new UserOutDTO(2, "Juan", "email", null, true, "address", "phone",  null, "latitude", "longitude"));

        when(userService.getAll("Juan","",false)).thenReturn(users);


        MvcResult result = mockMvc.perform(get(endpointUrl).queryParam("name", "Juan")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        List<UserOutDTO> userResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(userResult);
        assertEquals(2, userResult.size());
        assertEquals("Juan", userResult.get(0).getName());
        assertEquals("Juan", userResult.get(1).getName());
    }

    @Test
    public void testGetAllByEmailReturnOk() throws Exception {
        String endpointUrl = "/users";
        List <UserOutDTO> users = List.of(new UserOutDTO(1, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude"),
                new UserOutDTO(2, "Pedro", "email", null, true, "address", "phone",  null, "latitude", "longitude"));

        when(userService.getAll("", "email", false)).thenReturn(users);

        MvcResult result = mockMvc.perform(get(endpointUrl).queryParam("email", "email")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        List<UserOutDTO> userResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(userResult);
        assertEquals(2, userResult.size());
        assertEquals("Juan", userResult.get(0).getName());
        assertEquals("Pedro", userResult.get(1).getName());
    }

    @Test
    public void testGetAllByActiveReturnOk() throws Exception {
        String endpointUrl = "/users";
        List <UserOutDTO> users = List.of(new UserOutDTO(1, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude"),
                new UserOutDTO(2, "Pedro", "email", null, true, "address", "phone",  null, "latitude", "longitude"));

        when(userService.getAll("", "", true)).thenReturn(users);

        MvcResult result = mockMvc.perform(get(endpointUrl).queryParam("active", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        List<UserOutDTO> userResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(userResult);
        assertEquals(2, userResult.size());
        assertEquals("Juan", userResult.get(0).getName());
        assertEquals("Pedro", userResult.get(1).getName());
    }




    @Test
    public void testGetUserReturnOk() throws Exception {
        String endpointUrl = "/users/{userId}";
        User user = new User(1, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);

        when(userService.get(1)).thenReturn(user);


        MvcResult result = mockMvc.perform(get(endpointUrl, "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        User userResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(userResult);

        assertEquals("Juan", userResult.getName());


    }

    @Test
    public void testGetUserReturnNotFound() throws Exception {
        String endpointUrl = "/users/{userId}";


        when(userService.get(1)).thenThrow(new UserNotFoundException());


        MvcResult result = mockMvc.perform(get(endpointUrl, "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});
        
        assertNotNull(errorResult);

        assertEquals(404, errorResult.getErrorcode());
        assertEquals("El usuario no existe", errorResult.getMessage());

    }


    @Test
    public void testAddUserReturnOk() throws Exception {
        String endpointUrl = "/users";
        User user = new User(1, "Juan", "email@email.com", null, true, "address", "6666666", null, "latitude", "longitude", null);

        when(userService.add(user)).thenReturn(user);

        String body = objectmapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(post(endpointUrl)
                .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                .content(body))
                 .andExpect(status().isCreated())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        User userResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(userResult);
        assertEquals(201, result.getResponse().getStatus());
        assertEquals("Juan", userResult.getName());
        assertEquals("email@email.com", userResult.getEmail());
        assertEquals("address", userResult.getAddress());

    }

    @Test
    public void testAddUserReturnBadRequest() throws Exception{
        String endpointUrl = "/users";
        User user = new User(1, null, null, null, true, "address", "phone", null, "latitude", "longitude", null);

        String body = objectmapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(post(endpointUrl)
                .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                .content(objectmapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();

        ErrorResponse errorResult = objectmapper.readValue(jsonResult, new TypeReference<>() {});

        assertNotNull(errorResult);
        assertEquals(400, errorResult.getErrorcode());
        assertEquals("Bad Request", errorResult.getMessage());

    }



}
