package org.example.apitiendaaa.repository;

import org.example.apitiendaaa.domain.Category;
import org.example.apitiendaaa.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    public List<Product> findAll();

    List<Product> findByCategory(Category category);

    List<Product> findByPrice(double price);

    List<Product> findByName(String name);

    List<Product> findByNameAndPrice(String name, double price);


    List<Product> findByActive(Boolean active);

    List<Product> findByPriceAndActive(double price, Boolean active);

    List<Product> findByNameAndActive(String name, Boolean active);

    List<Product> findByNameAndPriceAndActive(String name, double price, Boolean active);
}
