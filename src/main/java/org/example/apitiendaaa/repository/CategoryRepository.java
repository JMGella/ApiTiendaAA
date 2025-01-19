package org.example.apitiendaaa.repository;

import org.example.apitiendaaa.domain.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

public List<Category> findAll();

    List<Category> findByCreationDate(LocalDate creationDate);

    List<Category> findByActive(Boolean active);

    List<Category> findByName(String name);

    List<Category> findByActiveAndCreationDate(Boolean active, LocalDate creationDate);

    List<Category> findByNameAndCreationDate(String name, LocalDate creationDate);

    List<Category> findByNameAndActive(String name, Boolean active);

    List<Category> findByNameAndActiveAndCreationDate(String name, Boolean active, LocalDate creationDate);
}
