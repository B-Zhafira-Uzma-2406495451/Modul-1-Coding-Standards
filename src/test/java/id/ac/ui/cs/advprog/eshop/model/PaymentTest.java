package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        this.order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");

        this.paymentData = new HashMap<>();
        this.paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testCreatePaymentWithId() {
        Payment payment = new Payment("payment-123", "VOUCHER", this.paymentData, this.order);

        assertEquals("payment-123", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals(this.paymentData, payment.getPaymentData());
        assertEquals(this.order, payment.getOrder());
        assertEquals("WAITING_PAYMENT", payment.getStatus()); // Harus WAITING_PAYMENT di awal
    }

    @Test
    void testCreatePaymentWithNullIdGeneratesUuid() {
        Payment payment = new Payment(null, "VOUCHER", this.paymentData, this.order);

        assertNotNull(payment.getId());
        assertNotEquals("", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals("WAITING_PAYMENT", payment.getStatus());
    }

    @Test
    void testSetStatus() {
        Payment payment = new Payment("payment-123", "VOUCHER", this.paymentData, this.order);

        payment.setStatus("SUCCESS");
        assertEquals("SUCCESS", payment.getStatus());

        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.getStatus());
    }
}