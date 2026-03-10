package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("13652556-012a-4c07-b546-54eb1396d79b", products, 1708560000L, "Safira Sudrajat");
    }

    @Test
    void testCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/createOrder"));
    }

    @Test
    void testCreateOrderPostSuccess() throws Exception {
        Mockito.doReturn(order).when(orderService).createOrder(any(Order.class));

        mockMvc.perform(post("/order/create")
                        .flashAttr("order", order))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history"));

        Mockito.verify(orderService, Mockito.times(1)).createOrder(any(Order.class));
    }

    @Test
    void testHistoryOrderPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/historyOrder"));
    }

    @Test
    void testHistoryOrderPostSuccess() throws Exception {
        List<Order> orders = Arrays.asList(order);
        Mockito.doReturn(orders).when(orderService).findAllByAuthor("Safira Sudrajat");

        mockMvc.perform(post("/order/history")
                        .param("author", "Safira Sudrajat"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/historyOrder"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", orders));
    }

    @Test
    void testPaymentOrderPageSuccess() throws Exception {
        Mockito.doReturn(order).when(orderService).findById(order.getId());

        mockMvc.perform(get("/order/pay/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("order/payOrder"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attribute("order", order));
    }

    @Test
    void testPaymentOrderPostSuccess() throws Exception {
        Mockito.doReturn(order).when(orderService).findById(order.getId());

        mockMvc.perform(post("/order/pay/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("order/paymentResult"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("paymentId"))
                .andExpect(model().attribute("paymentId", "PAYMENT-" + order.getId()));
    }

    @Test
    void testHistoryOrderPost_MissingAuthorParameter() throws Exception {
        mockMvc.perform(post("/order/history"))
                .andExpect(status().isBadRequest());
        Mockito.verify(orderService, Mockito.never()).findAllByAuthor(any());
    }

    @Test
    void testPaymentOrderPage_OrderIdNotFound() throws Exception {
        String invalidOrderId = "ID-TIDAK-VALID";
        Mockito.doReturn(null).when(orderService).findById(invalidOrderId);

        mockMvc.perform(get("/order/pay/" + invalidOrderId))
                .andExpect(status().isOk())
                .andExpect(view().name("order/payOrder"))
                .andExpect(model().attributeDoesNotExist("order"));

        Mockito.verify(orderService, Mockito.times(1)).findById(invalidOrderId);
    }
}