package org.example.apitiendaaa.service;


import org.example.apitiendaaa.domain.Category;
import org.example.apitiendaaa.domain.DTO.ProductInDTO;
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

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;


    public List<ProductOutDTO> getAll(String name, String price, Boolean active) {
        List<Product> productList;
        Float priceFloat = null;
        if (!price.isEmpty()){
           priceFloat = Float.parseFloat(price);
        }
       if(name.isEmpty() && price.isEmpty() && active == null) {
            productList = productRepository.findAll();

        } else if(name.isEmpty() && price.isEmpty()) {
            productList = productRepository.findByActive(active);

        } else if(name.isEmpty() && active == null) {
            productList = productRepository.findByPrice(priceFloat);

        } else if(price.isEmpty() && active == null) {
            productList = productRepository.findByName(name);
        } else if(name.isEmpty()) {
            productList = productRepository.findByPriceAndActive(priceFloat, active);
        } else if(price.isEmpty()) {
            productList = productRepository.findByNameAndActive(name, active);
        } else if(active == null) {
            productList = productRepository.findByNameAndPrice(name, priceFloat);
        } else {
            productList = productRepository.findByNameAndPriceAndActive(name, priceFloat, active);
        }
        return modelMapper.map(productList, new TypeToken<List<ProductOutDTO>>() {}.getType());
    }

    public Product add(long categoryId, Product product) throws CategoryNotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        product.setCategory(category);
        product.setCreationDate(LocalDate.now());
        return productRepository.save(product);

    }

    public ProductOutDTO get(long productId) throws ProductNotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        return modelMapper.map(product, ProductOutDTO.class);
    }

    public Product update(long categoryId, long productId, ProductInDTO product) throws CategoryNotFoundException, ProductNotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        Product productToUpdate = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        if(product.getName() != null){
            productToUpdate.setName(product.getName());
        }
        if(product.getPrice() != null){
            productToUpdate.setPrice(product.getPrice());
        }
        if (product.getDescription() != null){
            productToUpdate.setDescription(product.getDescription());
        }
        if (product.getActive() != null) {
            productToUpdate.setActive(product.getActive());
        }
        return productRepository.save(productToUpdate);
    }


    public void delete(long productId) throws ProductNotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        productRepository.delete(product);
    }

    public List<ProductOutDTO> getByCategory(long categoryId) throws CategoryNotFoundException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        List<Product> products = productRepository.findByCategory(category);
        return modelMapper.map(products, new TypeToken<List<ProductOutDTO>>() {}.getType());
    }
}
