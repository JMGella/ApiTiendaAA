package org.example.apitiendaaa.service;


import org.example.apitiendaaa.domain.Category;
import org.example.apitiendaaa.domain.DTO.ProductOutDTO;
import org.example.apitiendaaa.domain.Product;
import org.example.apitiendaaa.exception.CategoryNotFoundException;
import org.example.apitiendaaa.exception.ProductNotFoundException;
import org.example.apitiendaaa.repository.CategoryRepository;
import org.example.apitiendaaa.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;


    public List<ProductOutDTO> getAll(String name, double price, long categoryid) throws CategoryNotFoundException {

        List<Product> productList;
        if(name.isEmpty() && price == 0 && categoryid == 0) {
            productList = productRepository.findAll();
        } else if(name.isEmpty() && price == 0) {
            Category category = categoryRepository.findById(categoryid).orElseThrow(CategoryNotFoundException::new);
            productList = productRepository.findByCategory(category);
        } else if(name.isEmpty() && categoryid == 0) {
            productList = productRepository.findByPrice(price);
        } else if(price == 0 && categoryid == 0) {
            productList = productRepository.findByName(name);
        } else if(name.isEmpty()) {
            productList = productRepository.findByPriceAndCategory(price, categoryid);
        } else if(price == 0) {
            productList = productRepository.findByNameAndCategory(name, categoryid);
        } else if(categoryid == 0) {
            productList = productRepository.findByNameAndPrice(name, price);
        } else {
            productList = productRepository.findByNameAndPriceAndCategory(name, price, categoryid);
        }
        return modelMapper.map(productList, new TypeToken<List<ProductOutDTO>>() {}.getType());
    }

    public Product add(long categoryId, Product product) throws CategoryNotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        product.setCategory(category);
        return productRepository.save(product);

    }

    public Product get(long productId) throws CategoryNotFoundException {
        return productRepository.findById(productId).orElseThrow(CategoryNotFoundException::new);
    }

    public Product update(long categoryId, long productId, Product product) throws CategoryNotFoundException, ProductNotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        Product productToUpdate = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        productToUpdate.setName(product.getName());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setCategory(category);
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setActive(product.isActive());
        productToUpdate.setCreationDate(product.getCreationDate());

        return productRepository.save(product);
    }


    public void delete(long productId) throws ProductNotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        productRepository.delete(product);
    }
}
