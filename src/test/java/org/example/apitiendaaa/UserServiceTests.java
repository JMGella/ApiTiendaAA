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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        assertEquals("Juan", userOutDTOList.get(0).getName());


        verify(userRepository,times(1)).findByName("Juan");
    }

    @Test
    public void testGetAllEmail(){
        List<User> userlist = List.of(new User(1, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null)
        );

        List<UserOutDTO> userOutDTOList = List.of(new UserOutDTO(1, "Juan", "email", null, true, "address", "phone",null, "latitude", "longitude")
        );

        when(userRepository.findByEmail("email")).thenReturn(userlist);
        when(modelMapper.map(userlist, new TypeToken<List<UserOutDTO>>() {}.getType())).thenReturn(userOutDTOList);

        userService.getAll("", "email", false);

        assertEquals(1, userlist.size());
        assertEquals(1, userOutDTOList.size());
        assertEquals("Juan", userlist.get(0).getName());


        verify(userRepository,times(1)).findByEmail("email");
    }

    @Test
    public void testGetAllActive(){
        List<User> userlist = List.of(new User(1, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null)
        );

        List<UserOutDTO> userOutDTOList = List.of(new UserOutDTO(1, "Juan", "email", null, true, "address", "phone",null, "latitude", "longitude")
        );

        when(userRepository.findByActive(true)).thenReturn(userlist);
        when(modelMapper.map(userlist, new TypeToken<List<UserOutDTO>>() {}.getType())).thenReturn(userOutDTOList);

        userService.getAll("", "",  true);

        assertEquals(1, userlist.size());
        assertEquals(1, userOutDTOList.size());
        assertEquals("Juan", userlist.get(0).getName());


        verify(userRepository,times(1)).findByActive(true);
    }




    @Test
    public void testGet() throws UserNotFoundException {
        User user = new User(1, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User actualUser = userService.get(1L);

        assertEquals("Juan", actualUser.getName());
        assertEquals("email", actualUser.getEmail());

        verify(userRepository,times(1)).findById(1L);


    }

    @Test
    public void testGetNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.get(99L);
        });
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

    @Test
    public void testUpdate() throws UserNotFoundException {
        User existingUser = new User(1, "Juan", "email", null, true, "address", "phone", null, "latitude", "longitude", null);
        User updatedData =  new User(1, "John", "new email", null, true, "new address", "123", null, "latitude", "longitude", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedData)).thenReturn(updatedData);

        User result = userService.update(1L, updatedData);

        assertEquals("John", result.getName());
        assertEquals("new email", result.getEmail());
        assertEquals("new address", result.getAddress());
        assertEquals("123", result.getPhone());

        verify(userRepository).save(existingUser);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdate_UserNotFound() {
        User user = new User();

        when(userRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(123L, user));
    }

    @Test
    public void testDelete() throws UserNotFoundException {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDelete_UserNotFound() {
        when(userRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete(123L));
    }

}

