package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testEdit_ShouldUpdateExistingProduct() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        updatedProduct.setProductName("Sampo Cap Bambang Baru");
        updatedProduct.setProductQuantity(200);

        Product result = productRepository.edit(updatedProduct);

        assertNotNull(result);
        assertEquals("Sampo Cap Bambang Baru", result.getProductName());
        assertEquals(200, result.getProductQuantity());
    }

    @Test
    void testEdit_ShouldReturnNullIfProductNotFound() {
        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId("id-tidak-ada");

        Product result = productRepository.edit(nonExistentProduct);

        assertNull(result);
    }

    @Test
    void testEdit_ShouldNotUpdateIfIdIsEmpty() {
        Product product = new Product();
        product.setProductId("valid-id");
        productRepository.create(product);

        Product emptyIdProduct = new Product();
        emptyIdProduct.setProductId("");

        Product result = productRepository.edit(emptyIdProduct);

        assertNull(result);
        Iterator<Product> it = productRepository.findAll();
        assertEquals("valid-id", it.next().getProductId());
    }

    @Test
    void testDelete_ShouldRemoveProductCorrectly() {
        Product product = new Product();
        product.setProductId("id-hapus");
        productRepository.create(product);

        productRepository.delete("id-hapus");

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDelete_ShouldDoNothingIfIdNotFound() {
        Product product = new Product();
        product.setProductId("id-tetap-ada");
        productRepository.create(product);

        productRepository.delete("id-salah");

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        assertEquals("id-tetap-ada", productIterator.next().getProductId());
    }

    @Test
    void testDelete_ShouldNotThrowExceptionOnEmptyRepository() {
        assertDoesNotThrow(() -> {
            productRepository.delete("id-sembarang");
        });

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindById_ProductExists() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);
        Product foundProduct = productRepository.findById("eb558e9f-1c39-460e-8860-71af6af63bd6");
        assertNotNull(foundProduct);
        assertEquals(product.getProductId(), foundProduct.getProductId());
        assertEquals(product.getProductName(), foundProduct.getProductName());
        assertEquals(product.getProductQuantity(), foundProduct.getProductQuantity());
    }

    @Test
    void testFindById_ProductDoesNotExist() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        Product foundProduct = productRepository.findById("id-yang-tidak-terdaftar");
        assertNull(foundProduct);
    }

    @Test
    void testFindById_MultipleProducts() {
        Product product1 = new Product();
        product1.setProductId("id-001");
        product1.setProductName("Sampo Cap Bambang");
        productRepository.create(product1);
        Product product2 = new Product();
        product2.setProductId("id-002");
        product2.setProductName("Sampo Cap Usep");
        productRepository.create(product2);
        Product foundProduct = productRepository.findById("id-002");
        assertNotNull(foundProduct);
        assertEquals("id-002", foundProduct.getProductId());
        assertEquals("Sampo Cap Usep", foundProduct.getProductName());
    }

    @Test
    void testFindById_EmptyRepository() {
        Product foundProduct = productRepository.findById("any-id");
        assertNull(foundProduct);
    }
}