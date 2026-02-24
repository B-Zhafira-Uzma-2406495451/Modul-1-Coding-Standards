package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl service;

    @Mock
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void testCreateProduct() {
        when(productRepository.create(product)).thenReturn(product);

        Product savedProduct = service.create(product);

        assertEquals(product.getProductId(), savedProduct.getProductId());
        verify(productRepository, times(1)).create(product);
    }

    @Test
    void testCreateProduct_RepositoryThrowsException() {
        when(productRepository.create(any(Product.class))).thenThrow(new RuntimeException("DB Error"));

        assertThrows(RuntimeException.class, () -> service.create(product));
        verify(productRepository, times(1)).create(product);
    }

    @Test
    void testCreateProduct_NullProduct() {
        when(productRepository.create(null)).thenReturn(null);

        Product savedProduct = service.create(null);

        assertNull(savedProduct);
        verify(productRepository, times(1)).create(null);
    }

    @Test
    void testFindAll() {
        List<Product> productList = List.of(product);
        Iterator<Product> productIterator = productList.iterator();
        when(productRepository.findAll()).thenReturn(productIterator);

        List<Product> allProducts = service.findAll();

        assertFalse(allProducts.isEmpty());
        assertEquals(1, allProducts.size());
        assertEquals(product.getProductId(), allProducts.get(0).getProductId());
    }

    @Test
    void testFindAll_RepositoryThrowsException() {
        when(productRepository.findAll()).thenThrow(new RuntimeException("DB Connection Lost"));

        assertThrows(RuntimeException.class, () -> service.findAll());
    }

    @Test
    void testFindAll_EmptyList() {
        Iterator<Product> emptyIterator = Collections.emptyIterator();
        when(productRepository.findAll()).thenReturn(emptyIterator);

        List<Product> allProducts = service.findAll();

        assertTrue(allProducts.isEmpty());
    }

    @Test
    void testFindById() {
        when(productRepository.findById(product.getProductId())).thenReturn(product);

        Product foundProduct = service.findById(product.getProductId());

        assertNotNull(foundProduct);
        assertEquals(product.getProductId(), foundProduct.getProductId());
    }

    @Test
    void testFindById_ProductNotFound() {
        when(productRepository.findById("invalid-id")).thenReturn(null);

        Product foundProduct = service.findById("invalid-id");

        assertNull(foundProduct);
    }

    @Test
    void testFindById_NullId() {
        when(productRepository.findById(null)).thenReturn(null);

        Product foundProduct = service.findById(null);

        assertNull(foundProduct);
    }

    @Test
    void testEditProduct() {
        when(productRepository.edit(product)).thenReturn(product);

        Product editedProduct = service.edit(product);

        assertNotNull(editedProduct);
        assertEquals(product.getProductId(), editedProduct.getProductId());
    }

    @Test
    void testEditProduct_RepositoryThrowsException() {
        when(productRepository.edit(any(Product.class))).thenThrow(new IllegalArgumentException("Invalid Data"));

        assertThrows(IllegalArgumentException.class, () -> service.edit(product));
    }

    @Test
    void testEditProduct_NullProduct() {
        when(productRepository.edit(null)).thenReturn(null);

        Product editedProduct = service.edit(null);

        assertNull(editedProduct);
    }

    @Test
    void testDeleteProductById() {
        doNothing().when(productRepository).delete(product.getProductId());

        assertDoesNotThrow(() -> service.deleteProductById(product.getProductId()));
        verify(productRepository, times(1)).delete(product.getProductId());
    }

    @Test
    void testDeleteProductById_RepositoryThrowsException() {
        doThrow(new RuntimeException("Product not found")).when(productRepository).delete("ghost-id");

        assertThrows(RuntimeException.class, () -> service.deleteProductById("ghost-id"));
        verify(productRepository, times(1)).delete("ghost-id");
    }

    @Test
    void testDeleteProductById_NullId() {
        doNothing().when(productRepository).delete(null);

        assertDoesNotThrow(() -> service.deleteProductById(null));
        verify(productRepository, times(1)).delete(null);
    }
}