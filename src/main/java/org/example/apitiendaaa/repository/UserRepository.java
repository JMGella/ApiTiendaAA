package org.example.apitiendaaa.repository;

import org.example.apitiendaaa.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

    public List<User> findAll();

    List<User> findByActive(boolean active);

    List<User> findByEmail(String email);

    List<User> findByName(String name);

    List<User> findByNameAndEmail(String name, String email);

    List<User> findByNameAndActive(String name, boolean active);

    List<User> findByEmailAndActive(String email, boolean active);

    List<User> findByNameAndEmailAndActive(String name, String email, boolean active);


}
