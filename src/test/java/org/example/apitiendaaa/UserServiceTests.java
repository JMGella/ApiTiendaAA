package org.example.apitiendaaa;

import org.example.apitiendaaa.domain.DTO.UserOutDTO;
import org.example.apitiendaaa.domain.User;
import org.example.apitiendaaa.exception.UserNotFoundException;
import org.example.apitiendaaa.repository.UserRepository;
import org.example.apitiendaaa.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testGetAll(){
        List<User> userlist = List.of(new User(1, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null),
                new User(2, "Pedro", "email", null, true, "address", "phone", null, "latitude", "longitude", null));

        List<UserOutDTO> userOutDTOList = List.of(new UserOutDTO(1, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude"),
                new UserOutDTO(2, "Pedro", "email", null, true, "address", "phone",  null, "latitude", "longitude"));

        when(userRepository.findAll()).thenReturn(userlist);
        when(modelMapper.map(userlist, new TypeToken<List<UserOutDTO>>() {}.getType())).thenReturn(userOutDTOList);

        userService.getAll("", "", false);

        assertEquals(2, userlist.size());
        assertEquals(2, userOutDTOList.size());
        assertEquals("Juan", userlist.get(0).getName());
        assertEquals("Pedro", userlist.get(1).getName());

        verify(userRepository,times(1)).findAll();
    }


    @Test
    public void testGetAllName(){
        List<User> userlist = List.of(new User(1, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null)
                );

        List<UserOutDTO> userOutDTOList = List.of(new UserOutDTO(1, "Juan", "email", null, true, "address", "phone",null, "latitude", "longitude")
                );

        when(userRepository.findByName("Juan")).thenReturn(userlist);
        when(modelMapper.map(userlist, new TypeToken<List<UserOutDTO>>() {}.getType())).thenReturn(userOutDTOList);

        userService.getAll("Juan", "", false);

        assertEquals(1, userlist.size());
        assertEquals(1, userOutDTOList.size());
        assertEquals("Juan", userlist.get(0).getName());


        verify(userRepository,times(1)).findByName("Juan");
    }

    @Test
    public void testGet() throws UserNotFoundException {
        User user = new User(1, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.get(1L);

        assertEquals("Juan", user.getName());
        assertEquals("email", user.getEmail());

        verify(userRepository,times(1)).findById(1L);


    }

    @Test
    public void testAdd(){
        User user = new User(1, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);

        when(userRepository.save(user)).thenReturn(user);

        userService.add(user);

        assertEquals("Juan", user.getName());
        assertEquals("email", user.getEmail());

        verify(userRepository,times(1)).save(user);
    }
}
