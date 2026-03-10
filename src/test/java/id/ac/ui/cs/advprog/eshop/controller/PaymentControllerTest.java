package id.ac.ui.cs.advprog.eshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testShowPaymentDetailForm() {
    }

    @Test
    public void testShowPaymentDetails() throws Exception {
        String paymentId = "PAY-12345";
        mockMvc.perform(get("/payment/detail/" + paymentId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("paymentId"))
                .andExpect(model().attribute("paymentId", paymentId))
                .andExpect(view().name("payment/detail"));
    }

    @Test
    public void testShowAllPayments() throws Exception {
        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/admin-list"));
    }

    @Test
    public void testShowAdminPaymentDetails() throws Exception {
        String paymentId = "PAY-98765";
        mockMvc.perform(get("/payment/admin/detail/" + paymentId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("paymentId"))
                .andExpect(model().attribute("paymentId", paymentId))
                .andExpect(view().name("payment/admin-detail"));
    }

    @Test
    public void testSetPaymentStatusAccepted() throws Exception {
        String paymentId = "PAY-11111";
        mockMvc.perform(post("/payment/admin/set-status/" + paymentId)
                        .param("status", "ACCEPTED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/detail/" + paymentId));
    }

    @Test
    public void testSetPaymentStatusRejected() throws Exception {
        String paymentId = "PAY-22222";
        mockMvc.perform(post("/payment/admin/set-status/" + paymentId)
                        .param("status", "REJECTED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/detail/" + paymentId));
    }

    @Test
    public void testSetPaymentStatus_Unhappy_MissingStatusParam() throws Exception {
        String paymentId = "PAY-33333";

        mockMvc.perform(post("/payment/admin/set-status/" + paymentId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSetPaymentStatus_Unhappy_WrongHttpMethod() throws Exception {
        String paymentId = "PAY-44444";

        mockMvc.perform(get("/payment/admin/set-status/" + paymentId)
                        .param("status", "ACCEPTED"))
                .andExpect(status().isMethodNotAllowed());
    }
}
