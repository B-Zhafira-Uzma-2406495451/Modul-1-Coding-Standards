package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderService orderService;

    List<Order> orders;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        orders = new ArrayList<>();
        Order order1 = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");
        orders.add(order1);
    }

    @Test
    void testAddPaymentVoucherSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment result = paymentService.addPayment(orders.get(0), "VOUCHER", paymentData);

        assertEquals("SUCCESS", result.getStatus());
        verify(paymentRepository, atLeastOnce()).save(any(Payment.class));
        verify(orderService, times(1)).updateStatus(orders.get(0).getId(), OrderStatus.SUCCESS.getValue());
    }

    @Test
    void testAddPaymentVoucherRejectedInvalidLength() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP123");

        Payment result = paymentService.addPayment(orders.get(0), "VOUCHER", paymentData);

        assertEquals("REJECTED", result.getStatus());
        verify(orderService, times(1)).updateStatus(orders.get(0).getId(), OrderStatus.FAILED.getValue());
    }

    @Test
    void testAddPaymentVoucherRejectedInvalidFormat() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "PROMO1234ABC5678");

        Payment result = paymentService.addPayment(orders.get(0), "VOUCHER", paymentData);

        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentCODSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jalan Margonda Raya");
        paymentData.put("deliveryFee", "10000");

        Payment result = paymentService.addPayment(orders.get(0), "CASH_ON_DELIVERY", paymentData);

        assertEquals("SUCCESS", result.getStatus());
        verify(paymentRepository, atLeastOnce()).save(any(Payment.class));
        verify(orderService, times(1)).updateStatus(orders.get(0).getId(), OrderStatus.SUCCESS.getValue());
    }

    @Test
    void testAddPaymentCODRejectedEmptyAddress() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "");
        paymentData.put("deliveryFee", "10000");

        Payment result = paymentService.addPayment(orders.get(0), "CASH_ON_DELIVERY", paymentData);

        assertEquals("REJECTED", result.getStatus());
        verify(orderService, times(1)).updateStatus(orders.get(0).getId(), OrderStatus.FAILED.getValue());
    }

    @Test
    void testAddPaymentCODRejectedNullDeliveryFee() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jalan Margonda Raya");

        Payment result = paymentService.addPayment(orders.get(0), "CASH_ON_DELIVERY", paymentData);

        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testSetStatusSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        Payment payment = new Payment("payment-123", "CASH_ON_DELIVERY", paymentData, orders.get(0));

        Payment result = paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", result.getStatus());
        verify(paymentRepository, times(1)).save(payment);
        verify(orderService, times(1)).updateStatus(orders.get(0).getId(), OrderStatus.SUCCESS.getValue());
    }

    @Test
    void testSetStatusRejected() {
        Map<String, String> paymentData = new HashMap<>();
        Payment payment = new Payment("payment-123", "CASH_ON_DELIVERY", paymentData, orders.get(0));

        Payment result = paymentService.setStatus(payment, "REJECTED");

        assertEquals("REJECTED", result.getStatus());
        verify(paymentRepository, times(1)).save(payment);
        verify(orderService, times(1)).updateStatus(orders.get(0).getId(), OrderStatus.FAILED.getValue());
    }

    @Test
    void testGetPayment() {
        Map<String, String> paymentData = new HashMap<>();
        Payment payment = new Payment("payment-123", "VOUCHER", paymentData, orders.get(0));

        doReturn(payment).when(paymentRepository).findById("payment-123");

        Payment result = paymentService.getPayment("payment-123");
        assertNotNull(result);
        assertEquals("payment-123", result.getId());
    }

    @Test
    void testGetAllPayments() {
        Map<String, String> paymentData = new HashMap<>();
        Payment payment = new Payment("payment-123", "VOUCHER", paymentData, orders.get(0));
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment);

        doReturn(paymentList).when(paymentRepository).findAll();

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(1, result.size());
        assertEquals("payment-123", result.get(0).getId());
    }
}