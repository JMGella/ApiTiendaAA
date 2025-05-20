package org.example.apitiendaaa.unit;

import org.example.apitiendaaa.domain.Category;
import org.example.apitiendaaa.domain.DTO.CategoryOutDTO;
import org.example.apitiendaaa.exception.CategoryNotFoundException;
import org.example.apitiendaaa.repository.CategoryRepository;
import org.example.apitiendaaa.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testAdd() {
        Category category = new Category(1L, "Electronics", "Devices and gadgets", LocalDate.now(), true, "image.jpg", null);

        when(categoryRepository.save(category)).thenReturn(category);

        categoryService.add(category);

        assertEquals("Electronics", category.getName());
        assertEquals("Devices and gadgets", category.getDescription());
        assertEquals("image.jpg", category.getImage());

        verify(categoryRepository, times(1)).save(category);

    }

    @Test
    public void testGetAll(){

        List<Category> categoryList = List.of(new Category(1L, "Electronics", "Devices and gadgets", LocalDate.now(), true, "image.jpg", null),
                new Category(2L, "Clothing", "Apparel and accessories", LocalDate.now(), true, "image.jpg", null));

        List<CategoryOutDTO> categoryOutDTOList = List.of(new CategoryOutDTO(1L, "Electronics", "Devices and gadgets",  true, LocalDate.now()),
                new CategoryOutDTO(2L, "Clothing", "Apparel and accessories", true, LocalDate.now()));

        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(modelMapper.map(categoryList, new TypeToken<List<CategoryOutDTO>>() {}.getType())).thenReturn(categoryOutDTOList);

        categoryService.getAll("", null, "");

        assertEquals(2, categoryList.size());
        assertEquals(2, categoryOutDTOList.size());
        assertEquals("Electronics", categoryList.get(0).getName());
        assertEquals("Clothing", categoryList.get(1).getName());
        assertEquals("Devices and gadgets", categoryList.get(0).getDescription());
        assertEquals("Apparel and accessories", categoryList.get(1).getDescription());

        verify(categoryRepository, times(1)).findAll();


    }

    @Test
    public void testGetAllName(){

        List<Category> categoryList = List.of(new Category(1L, "Electronics", "Devices and gadgets", LocalDate.now(), true, "image.jpg", null));

        List<CategoryOutDTO> categoryOutDTOList = List.of(new CategoryOutDTO(1L, "Electronics", "Devices and gadgets",  true, LocalDate.now()));

        when(categoryRepository.findByName("Electronics")).thenReturn(categoryList);
        when(modelMapper.map(categoryList, new TypeToken<List<CategoryOutDTO>>() {}.getType())).thenReturn(categoryOutDTOList);

        categoryService.getAll("Electronics", null, "");

        assertEquals(1, categoryList.size());
        assertEquals(1, categoryOutDTOList.size());
        assertEquals("Electronics", categoryList.get(0).getName());
        assertEquals("Electronics", categoryOutDTOList.get(0).getName());
        assertEquals("Devices and gadgets", categoryList.get(0).getDescription());
        assertEquals("Devices and gadgets", categoryOutDTOList.get(0).getDescription());

        verify(categoryRepository, times(1)).findByName("Electronics");

    }

    @Test
    public void testGetAllCreationDate(){

        List<Category> categoryList = List.of(new Category(1L, "Electronics", "Devices and gadgets", LocalDate.now(), true, "image.jpg", null));

        List<CategoryOutDTO> categoryOutDTOList = List.of(new CategoryOutDTO(1L, "Electronics", "Devices and gadgets",  true, LocalDate.now()));

        when(categoryRepository.findByCreationDate(LocalDate.now())).thenReturn(categoryList);
        when(modelMapper.map(categoryList, new TypeToken<List<CategoryOutDTO>>() {}.getType())).thenReturn(categoryOutDTOList);

        categoryService.getAll("", null, LocalDate.now().toString());

        assertEquals(1, categoryList.size());
        assertEquals(1, categoryOutDTOList.size());
        assertEquals("Electronics", categoryList.get(0).getName());
        assertEquals("Electronics", categoryOutDTOList.get(0).getName());
        assertEquals("Devices and gadgets", categoryList.get(0).getDescription());
        assertEquals("Devices and gadgets", categoryOutDTOList.get(0).getDescription());

        verify(categoryRepository, times(1)).findByCreationDate(LocalDate.now());

    }

    @Test
    public void testGetAllActive(){

        List<Category> categoryList = List.of(new Category(1L, "Electronics", "Devices and gadgets", LocalDate.now(), true, "image.jpg", null));

        List<CategoryOutDTO> categoryOutDTOList = List.of(new CategoryOutDTO(1L, "Electronics", "Devices and gadgets",  true, LocalDate.now()));

        when(categoryRepository.findByActive(true)).thenReturn(categoryList);
        when(modelMapper.map(categoryList, new TypeToken<List<CategoryOutDTO>>() {}.getType())).thenReturn(categoryOutDTOList);

        categoryService.getAll("", true,"");

        assertEquals(1, categoryList.size());
        assertEquals(1, categoryOutDTOList.size());
        assertEquals("Electronics", categoryList.get(0).getName());
        assertEquals("Electronics", categoryOutDTOList.get(0).getName());
        assertEquals("Devices and gadgets", categoryList.get(0).getDescription());
        assertEquals("Devices and gadgets", categoryOutDTOList.get(0).getDescription());

        verify(categoryRepository, times(1)).findByActive(true);

    }

    @Test
    public void testGet() throws CategoryNotFoundException {
        Category category = new Category(1L, "Electronics", "Devices and gadgets", LocalDate.now(), true, "image.jpg", null);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category actualCategory = categoryService.get(1L);

        assertEquals("Electronics", actualCategory.getName());
        assertEquals("Devices and gadgets", actualCategory.getDescription());
        assertEquals("image.jpg", actualCategory.getImage());

        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetCategoryNotFound(){
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.get(1L);
        });

        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdate() throws CategoryNotFoundException {
        Category category = new Category(1L, "Electronics", "Devices and gadgets", LocalDate.now(), true, "image.jpg", null);
        Category updatedCategory = new Category(1L, "Updated Electronics", "Updated description", LocalDate.now(), true, "updated_image.jpg", null);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);

        Category actualCategory = categoryService.update(1L, updatedCategory);

        assertEquals("Updated Electronics", actualCategory.getName());
        assertEquals("Updated description", actualCategory.getDescription());
        assertEquals("updated_image.jpg", actualCategory.getImage());

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(updatedCategory);
    }

    @Test
    public void testUpdateCategoryNotFound () {
        Category updatedCategory = new Category(1L, "Updated Electronics", "Updated description", LocalDate.now(), true, "updated_image.jpg", null);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.update(1L, updatedCategory);
        });

        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testDelete() throws CategoryNotFoundException {
        Category category = new Category();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.delete(1L);

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteCategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.delete(1L);
        });

        verify(categoryRepository, times(1)).findById(1L);
    }



}
