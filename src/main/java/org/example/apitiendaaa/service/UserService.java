package org.example.apitiendaaa.service;

import org.example.apitiendaaa.domain.DTO.UserOutDTO;
import org.example.apitiendaaa.domain.User;
import org.example.apitiendaaa.exception.UserNotFoundException;
import org.example.apitiendaaa.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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

    public List<UserOutDTO> getAll(String name, String email, boolean active) {
        List<User> users;
        if(name.isEmpty() && email.isEmpty() && active == false) {
            users = userRepository.findAll();
        } else if(name.isEmpty() && email.isEmpty()) {
            users = userRepository.findByActive(active);
        } else if(name.isEmpty() && active == false) {
            users = userRepository.findByEmail(email);
        } else if(email.isEmpty() && active == false) {
            users = userRepository.findByName(name);
        } else if(name.isEmpty()) {
            users = userRepository.findByEmailAndActive(email, active);
        } else if(email.isEmpty()) {
            users = userRepository.findByNameAndActive(name, active);
        } else if(active == false) {
            users = userRepository.findByNameAndEmail(name, email);
        } else {
            users = userRepository.findByNameAndEmailAndActive(name, email, active);
        }
        List <UserOutDTO> usersDTO = modelMapper.map(users,new TypeToken<List<UserOutDTO>>() {}.getType());
        return usersDTO;
    }

    public User get(long userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

    }

    public User update(long userId, User user) throws UserNotFoundException {

        User userToUpdate = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(userToUpdate != null) {
            userToUpdate.setName(user.getName());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setBirthDate(user.getBirthDate());
            userToUpdate.setAddress(user.getAddress());
            userToUpdate.setPhone(user.getPhone());
            userToUpdate.setCreationDate(user.getCreationDate());
            userToUpdate.setActive(user.isActive());
            return userRepository.save(userToUpdate);
        }
        return null;
    }

    public void delete(long userId) throws UserNotFoundException {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(userId);
    }

}
