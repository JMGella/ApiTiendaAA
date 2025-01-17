package org.example.apitiendaaa.service;

import org.example.apitiendaaa.domain.DTO.UserOutDTO;
import org.example.apitiendaaa.domain.User;
import org.example.apitiendaaa.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public User add(User user) {
        return userRepository.save(user);
    }

    public List<UserOutDTO> getAll() {
        List<User> users = userRepository.findAll();
        List <UserOutDTO> usersDTO = modelMapper.map(users, List.class);
        return usersDTO;
    }



}
