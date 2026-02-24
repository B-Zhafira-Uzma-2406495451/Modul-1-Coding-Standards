package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void testCreateProductPage() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void testCreateProductPage_WrongMethod() throws Exception {
        mockMvc.perform(put("/product/create").param("isInvalid", "true"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testCreateProductPage_WithUnrelatedQueryParams() throws Exception {
        mockMvc.perform(get("/product/create").param("randomParam", "hacker"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"));
    }

    @Test
    void testCreateProductPost() throws Exception {
        when(service.create(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/create")
                        .flashAttr("product", product))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));
    }

    @Test
    void testCreateProductPost_ServiceThrowsException() {
        when(service.create(any(Product.class))).thenThrow(new RuntimeException("Database error"));

        try {
            mockMvc.perform(post("/product/create").flashAttr("product", product));
        } catch (Exception e) {
            assert e.getCause() instanceof RuntimeException;
        }
    }

    @Test
    void testCreateProductPost_EmptyProductObject() throws Exception {
        mockMvc.perform(post("/product/create")
                        .flashAttr("product", new Product()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));
    }

    @Test
    void testProductListPage() throws Exception {
        List<Product> products = List.of(product);
        when(service.findAll()).thenReturn(products);
        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", products));
    }

    @Test
    void testProductListPage_Negative_ServiceError() {
        when(service.findAll()).thenThrow(new RuntimeException("Cannot connect to DB"));

        try {
            mockMvc.perform(get("/product/list"));
        } catch (Exception e) {
            assert e.getCause() instanceof RuntimeException;
        }
    }

    @Test
    void testProductListPage_EmptyList() throws Exception {
        when(service.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attribute("products", new ArrayList<>()));
    }

    @Test
    void testEditProductPage() throws Exception {
        when(service.findById(product.getProductId())).thenReturn(product);

        mockMvc.perform(get("/product/edit/" + product.getProductId()))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void testEditProductPage_ProductNotFound() {
        when(service.findById("invalid-id")).thenThrow(new IllegalArgumentException("Product not found"));

        try {
            mockMvc.perform(get("/product/edit/invalid-id"));
        } catch (Exception e) {
            assert e.getCause() instanceof IllegalArgumentException;
        }
    }

    @Test
    void testEditProductPage_SpecialCharactersInId() throws Exception {
        String weirdId = "@!#$%^&*()";
        when(service.findById(weirdId)).thenReturn(product);

        mockMvc.perform(get("/product/edit/{id}", weirdId))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"));
    }

    @Test
    void testEditProductPost() throws Exception {
        when(service.edit(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/edit")
                        .flashAttr("product", product))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));
    }

    @Test
    void testEditProductPost_ServiceFails() {
        when(service.edit(any(Product.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        try {
            mockMvc.perform(post("/product/edit").flashAttr("product", product));
        } catch (Exception e) {
            assert e.getCause() instanceof IllegalArgumentException;
        }
    }

    @Test
    void testEditProductPost_MissingId() throws Exception {
        Product productNoId = new Product();
        productNoId.setProductName("No ID Product");

        mockMvc.perform(post("/product/edit")
                        .flashAttr("product", productNoId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Mockito.doNothing().when(service).deleteProductById(product.getProductId());

        mockMvc.perform(get("/product/delete/" + product.getProductId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    @Test
    void testDeleteProduct_IdNotFound() {
        doThrow(new RuntimeException("Product not found")).when(service).deleteProductById("ghost-id");

        try {
            mockMvc.perform(get("/product/delete/ghost-id"));
        } catch (Exception e) {
            assert e.getCause() instanceof RuntimeException;
        }
    }

    @Test
    void testDeleteProduct_EmptyId() throws Exception {
        mockMvc.perform(get("/product/delete/"))
                .andExpect(status().is4xxClientError());
    }
}