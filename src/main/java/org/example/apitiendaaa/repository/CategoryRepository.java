package org.example.apitiendaaa.repository;

import org.example.apitiendaaa.domain.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

public List<Category> findAll();

    List<Category> findByCreationDate(String creationDate);

    List<Category> findByActive(Boolean active);

    List<Category> findByName(String name);

    List<Category> findByActiveAndCreationDate(Boolean active, String creationDate);

    List<Category> findByNameAndCreationDate(String name, String creationDate);

    List<Category> findByNameAndActive(String name, Boolean active);

    List<Category> findByNameAndActiveAndCreationDate(String name, Boolean active, String creationDate);
}
