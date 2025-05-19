package org.example.apitiendaaa;

import org.example.apitiendaaa.domain.Category;
import org.example.apitiendaaa.domain.DTO.ProductInDTO;
import org.example.apitiendaaa.domain.DTO.ProductOutDTO;
import org.example.apitiendaaa.domain.Product;
import org.example.apitiendaaa.exception.CategoryNotFoundException;
import org.example.apitiendaaa.exception.ProductNotFoundException;
import org.example.apitiendaaa.repository.CategoryRepository;
import org.example.apitiendaaa.repository.ProductRepository;
import org.example.apitiendaaa.service.CategoryService;
import org.example.apitiendaaa.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testGetAll(){
        List<Product> productList = List.of(new Product(1L, "Product1", "Description1", 10.0f, LocalDate.now(), true, "image1", null, null),
                new Product(2L, "Product2", "Description2", 20.0f, LocalDate.now(), true, "image2", null, null));

        List<ProductOutDTO> productOutDTOList = List.of(new ProductOutDTO(1L, "Product1", 10.0f,  1L, LocalDate.now(), "Description"),
                new ProductOutDTO(2L, "Product2", 20.0f,  2L, LocalDate.now(), "Description2"));

        when(productRepository.findAll()).thenReturn(productList);
        when(modelMapper.map(productList, new org.modelmapper.TypeToken<List<ProductOutDTO>>() {}.getType())).thenReturn(productOutDTOList);

        List<ProductOutDTO> result = productService.getAll("", "", null);

        assertEquals(2, result.size());
        assertEquals("Product1", result.get(0).getName());
        assertEquals("Product2", result.get(1).getName());
        assertEquals(10.0f, result.get(0).getPrice());
        assertEquals(20.0f, result.get(1).getPrice());

        verify(productRepository, times(1)).findAll();


    }

    @Test
    public void testGetAllName(){
        List<Product> productList = List.of(new Product(1L, "Product1", "Description1", 10.0f, LocalDate.now(), true, "image1", null, null),
                new Product(2L, "Product1", "Description2", 20.0f, LocalDate.now(), true, "image2", null, null));

        List<ProductOutDTO> productOutDTOList = List.of(new ProductOutDTO(1L, "Product1", 10.0f,  1L, LocalDate.now(), "Description"),
                new ProductOutDTO(2L, "Product1", 20.0f,  2L, LocalDate.now(), "Description2"));

        when(productRepository.findByName("Product1")).thenReturn(productList);
        when(modelMapper.map(productList, new org.modelmapper.TypeToken<List<ProductOutDTO>>() {}.getType())).thenReturn(productOutDTOList);

        List<ProductOutDTO> result = productService.getAll("Product1", "", null);

        assertEquals(2, result.size());
        assertEquals("Product1", result.get(0).getName());
        assertEquals("Product1", result.get(1).getName());
        assertEquals(10.0f, result.get(0).getPrice());
        assertEquals(20.0f, result.get(1).getPrice());

        verify(productRepository, times(1)).findByName("Product1");


    }

    @Test
    public void testGetAllPrice(){
        List<Product> productList = List.of(new Product(1L, "Product1", "Description1", 10.0f, LocalDate.now(), true, "image1", null, null),
                new Product(2L, "Product2", "Description2", 10.0f, LocalDate.now(), true, "image2", null, null));

        List<ProductOutDTO> productOutDTOList = List.of(new ProductOutDTO(1L, "Product1", 10.0f,  1L, LocalDate.now(), "Description"),
                new ProductOutDTO(2L, "Product2", 10.0f,  2L, LocalDate.now(), "Description2"));

        when(productRepository.findByPrice(10.0f)).thenReturn(productList);
        when(modelMapper.map(productList, new org.modelmapper.TypeToken<List<ProductOutDTO>>() {}.getType())).thenReturn(productOutDTOList);

        List<ProductOutDTO> result = productService.getAll("", "10.0", null);

        assertEquals(2, result.size());
        assertEquals("Product1", result.get(0).getName());
        assertEquals("Product2", result.get(1).getName());
        assertEquals(10.0f, result.get(0).getPrice());
        assertEquals(10.0f, result.get(1).getPrice());

        verify(productRepository, times(1)).findByPrice(10.0f);


    }

    @Test
    public void testGetAllActive(){
        List<Product> productList = List.of(new Product(1L, "Product1", "Description1", 10.0f, LocalDate.now(), true, "image1", null, null),
                new Product(2L, "Product2", "Description2", 20.0f, LocalDate.now(), true, "image2", null, null));

        List<ProductOutDTO> productOutDTOList = List.of(new ProductOutDTO(1L, "Product1", 10.0f,  1L, LocalDate.now(), "Description"),
                new ProductOutDTO(2L, "Product2", 20.0f,  2L, LocalDate.now(), "Description2"));

        when(productRepository.findByActive(true)).thenReturn(productList);
        when(modelMapper.map(productList, new org.modelmapper.TypeToken<List<ProductOutDTO>>() {}.getType())).thenReturn(productOutDTOList);

        List<ProductOutDTO> result = productService.getAll("", "", true);

        assertEquals(2, result.size());
        assertEquals("Product1", result.get(0).getName());
        assertEquals("Product2", result.get(1).getName());
        assertEquals(10.0f, result.get(0).getPrice());
        assertEquals(20.0f, result.get(1).getPrice());

        verify(productRepository, times(1)).findByActive(true);


    }

    @Test
    public void testAdd() throws CategoryNotFoundException {
        Category category = new Category(1L, "Category1", "Description1", LocalDate.now(), true, null, null);

        Product product = new Product(1L, "Product1", "Description1", 10.0f, LocalDate.now(), true, "image1", category, null);
        Product savedProduct = new Product(1L, "Product1", "Description1", 10.0f, LocalDate.now(), true, "image1", category, null);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        when(productRepository.save(product)).thenReturn(savedProduct);

        Product result = productService.add(1L, product);

        assertEquals("Product1", result.getName());
        assertEquals(10.0f, result.getPrice());
        assertEquals("Description1", result.getDescription());
        assertEquals(category, result.getCategory());

        verify(productRepository, times(1)).save(product);
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testAddCategoryNotFound(){
        Category category = new Category(1L, "Category1", "Description1", LocalDate.now(), true, null, null);
        Product product = new Product(1L, "Product1", "Description1", 10.0f, LocalDate.now(), true, "image1", category, null);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            productService.add(1L, product);
        });

        verify(productRepository, never()).save(product);
        verify(categoryRepository, times(1)).findById(1L);

    }

    @Test
    public void testGet() throws ProductNotFoundException {
        Product product = new Product(1L, "Product1", "Description1", 10.0f, LocalDate.now(), true, "image1", null, null);
        ProductOutDTO productOutDTO = new ProductOutDTO(1L, "Product1", 10.0f,  1L, LocalDate.now(), "Description1");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(modelMapper.map(product, ProductOutDTO.class)).thenReturn(productOutDTO);

        ProductOutDTO result = productService.get(1L);

        assertEquals("Product1", result.getName());
        assertEquals(10.0f, result.getPrice());
        assertEquals("Description1", result.getDescription());

        verify(productRepository, times(1)).findById(1L);

    }


    @Test
    public void testGetNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.get(99L);
        });

        verify(productRepository, times(1)).findById(99L);
    }

    @Test
    public void testUpdate() throws ProductNotFoundException, CategoryNotFoundException {

        long productId = 1L;
        Category category = new Category(1L, "Category1", "Description1", LocalDate.now(), true, null, null);

        ProductInDTO productIn = new ProductInDTO( "Product Updated", "Description updated", 20.0f, LocalDate.now(), true);

        Product productToUpdate = new Product(productId, productIn.getName(), productIn.getDescription(), productIn.getPrice(), LocalDate.now(), true, "image1", category, null);

        Product updatedProduct = new Product(productId, "Product Updated", "Description updated", 20.0f, LocalDate.now(), true, "image1", category, null);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.findById(productId)).thenReturn(Optional.of(productToUpdate));
        when(productRepository.save(productToUpdate)).thenReturn(updatedProduct);

        Product result = productService.update(1L, productId, productIn);

        assertEquals("Product Updated", result.getName());
        assertEquals("Description updated", result.getDescription());
        assertEquals(20.0f, result.getPrice());
        assertEquals(category, result.getCategory());

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(updatedProduct);
        verify(categoryRepository, times(1)).findById(1L);

    }

    @Test
    public void testUpdateCategoryNotFound() {
        long productId = 1L;
        ProductInDTO productIn = new ProductInDTO( "Product Updated", "Description updated", 20.0f, LocalDate.now(), true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
           productService.update(1L, productId, productIn);
        });

        verify(productRepository, never()).save(any(Product.class));
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateProductNotFound(){
        long productId = 1L;
        Category category = new Category(1L, "Category1", "Description1", LocalDate.now(), true, null, null);
        ProductInDTO productIn = new ProductInDTO( "Product Updated", "Description updated", 20.0f, LocalDate.now(), true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.update(1L, productId, productIn);
        });

        verify(productRepository, times(1)).findById(productId);
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any(Product.class));

    }

    @Test
    public void testDelete() throws ProductNotFoundException {
        long productId = 1L;
        Product product = new Product(productId, "Product1", "Description1", 10.0f, LocalDate.now(), true, "image1", null, null);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.delete(productId);

        verify(productRepository, times(1)).delete(product);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testDeleteProductNotFound() {
        long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.delete(productId);
        });

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    public void testGetByCategory() throws CategoryNotFoundException {
        long categoryId = 1L;
        Category category = new Category(categoryId, "Category1", "Description1", LocalDate.now(), true, null, null);
        List<Product> productList = List.of(
                new Product(1L, "Product1", "Description1", 10.0f, LocalDate.now(), true, "image1", category, null),
                new Product(2L, "Product2", "Description2", 20.0f, LocalDate.now(), true, "image2", category, null)
        );
        List <ProductOutDTO> productOutDTOList = List.of(
                new ProductOutDTO(1L, "Product1", 10.0f, categoryId, LocalDate.now(), "Description1"),
                new ProductOutDTO(2L, "Product2", 20.0f, categoryId, LocalDate.now(), "Description2")
        );

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.findByCategory(category)).thenReturn(productList);
        when(modelMapper.map(productList, new org.modelmapper.TypeToken<List<ProductOutDTO>>() {}.getType())).thenReturn(productOutDTOList);
        List<ProductOutDTO> result = productService.getByCategory(categoryId);

        assertEquals(2, result.size());
        assertEquals("Product1", result.get(0).getName());
        assertEquals("Product2", result.get(1).getName());
        assertEquals(10.0f, result.get(0).getPrice());
        assertEquals(20.0f, result.get(1).getPrice());
        assertEquals(categoryId, result.get(0).getCategoryId());

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(productRepository, times(1)).findByCategory(category);

    }

    @Test
    public void testGetByCategoryNotFound() {
        long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            productService.getByCategory(categoryId);
        });

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(productRepository, never()).findByCategory(any(Category.class));
    }

}





