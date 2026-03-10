package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;

@WebMvcTest(PaymentController.class)
@ImportAutoConfiguration(exclude = ThymeleafAutoConfiguration.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    private Payment mockPayment;

    @BeforeEach
    void setUp() {
        mockPayment = Mockito.mock(Payment.class);
    }

    @Test
    public void testShowPaymentDetailForm() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/detail-form"));
    }

    @Test
    public void testShowPaymentDetails() throws Exception {
        String paymentId = "PAY-12345";
        Mockito.when(paymentService.getPayment(paymentId)).thenReturn(mockPayment);

        mockMvc.perform(get("/payment/detail/" + paymentId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("paymentId", "payment"))
                .andExpect(view().name("payment/detail"));
    }

    @Test
    public void testShowAllPayments() throws Exception {
        Payment mockPayment2 = Mockito.mock(Payment.class);
        List<Payment> mockPayments = Arrays.asList(mockPayment, mockPayment2);

        Mockito.when(paymentService.getAllPayments()).thenReturn(mockPayments);

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("payments"))
                .andExpect(view().name("payment/admin-list"));
    }

    @Test
    public void testShowAdminPaymentDetails() throws Exception {
        String paymentId = "PAY-98765";
        Mockito.when(paymentService.getPayment(paymentId)).thenReturn(mockPayment);

        mockMvc.perform(get("/payment/admin/detail/" + paymentId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("paymentId", "payment"))
                .andExpect(view().name("payment/admin-detail"));
    }

    @Test
    public void testSetPaymentStatusAccepted() throws Exception {
        String paymentId = "PAY-11111";
        Mockito.when(paymentService.getPayment(paymentId)).thenReturn(mockPayment);

        mockMvc.perform(post("/payment/admin/set-status/" + paymentId)
                        .param("status", "ACCEPTED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/detail/" + paymentId));

        Mockito.verify(paymentService).setStatus(mockPayment, "ACCEPTED");
    }

    @Test
    public void testSetPaymentStatus_PaymentNotFound() throws Exception {
        String paymentId = "PAY-NULL";
        Mockito.when(paymentService.getPayment(paymentId)).thenReturn(null);

        mockMvc.perform(post("/payment/admin/set-status/" + paymentId)
                        .param("status", "ACCEPTED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/detail/" + paymentId));

        Mockito.verify(paymentService, Mockito.never()).setStatus(any(Payment.class), any(String.class));
    }
}