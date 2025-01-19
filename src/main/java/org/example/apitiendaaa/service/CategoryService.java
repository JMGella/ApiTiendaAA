package org.example.apitiendaaa.service;

import org.example.apitiendaaa.domain.Category;
import org.example.apitiendaaa.domain.DTO.CategoryOutDTO;
import org.example.apitiendaaa.exception.CategoryNotFoundException;
import org.example.apitiendaaa.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Category add(Category category) {
        return categoryRepository.save(category);
    }

    public List<CategoryOutDTO> getAll(String name, Boolean active, String creationDate){
        List<Category> categories;
        LocalDate creationLocalDate = LocalDate.parse(creationDate);
        if (name.isEmpty() && active == null && creationDate.isEmpty()){
          categories = categoryRepository.findAll();

        }
        else if (name.isEmpty() && active == null){
           categories = categoryRepository.findByCreationDate(creationLocalDate);

        }
        else if (name.isEmpty() && creationDate.isEmpty()){
             categories = categoryRepository.findByActive(active);

        }
        else if (active == null && creationDate.isEmpty()){
            categories = categoryRepository.findByName(name);

        }
        else if (name.isEmpty()){
           categories = categoryRepository.findByActiveAndCreationDate(active, creationLocalDate);

        }
        else if (active == null){
            categories = categoryRepository.findByNameAndCreationDate(name, creationLocalDate);

        }
        else if (creationDate.isEmpty()){
            categories = categoryRepository.findByNameAndActive(name, active);


        }
        else{
             categories = categoryRepository.findByNameAndActiveAndCreationDate(name, active, creationLocalDate);

        }
        return modelMapper.map(categories, new TypeToken<List<CategoryOutDTO>>() {}.getType());

    }

    public Category get(long categoryId) throws CategoryNotFoundException {
        return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    }

    public Category update(long categoryId, Category category) throws CategoryNotFoundException {
        Category categoryToUpdate = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);

        if (categoryToUpdate != null) {
            categoryToUpdate.setName(category.getName());
            categoryToUpdate.setDescription(category.getDescription());
            categoryToUpdate.setCreationDate(category.getCreationDate());
            categoryToUpdate.setActive(category.isActive());
            categoryToUpdate.setImage(category.getImage());

            return categoryRepository.save(categoryToUpdate);
        }
        return null;
    }

    public void delete(long categoryId) throws CategoryNotFoundException {
        categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        categoryRepository.deleteById(categoryId);
    }
}
