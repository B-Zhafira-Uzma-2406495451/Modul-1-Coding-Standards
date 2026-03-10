package id.ac.ui.cs.advprog.eshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @GetMapping("/detail")
    public String showPaymentDetailForm() {
    }

    @GetMapping("/detail/{paymentId}")
    public String showPaymentDetails(@PathVariable("paymentId") String paymentId, Model model) {
    }

    @GetMapping("/admin/list")
    public String showAllPayments(Model model) {
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String showAdminPaymentDetails(@PathVariable("paymentId") String paymentId, Model model) {
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String setPaymentStatus(@PathVariable("paymentId") String paymentId,
                                   @RequestParam("status") String status) {
    }
}