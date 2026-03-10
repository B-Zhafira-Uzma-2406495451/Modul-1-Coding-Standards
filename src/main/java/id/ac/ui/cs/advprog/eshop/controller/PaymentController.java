package id.ac.ui.cs.advprog.eshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/detail")
    public String showPaymentDetailForm() {
        return "payment/detail-form";
    }

    @GetMapping("/detail/{paymentId}")
    public String showPaymentDetails(@PathVariable("paymentId") String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("paymentId", paymentId);
        model.addAttribute("payment", payment);
        return "payment/detail";
    }

    @GetMapping("/admin/list")
    public String showAllPayments(Model model) {
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        return "payment/admin-list";
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String showAdminPaymentDetails(@PathVariable("paymentId") String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("paymentId", paymentId);
        model.addAttribute("payment", payment);
        return "payment/admin-detail";
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String setPaymentStatus(@PathVariable("paymentId") String paymentId,
                                   @RequestParam("status") String status) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment != null) {
            paymentService.setStatus(payment, status);
        }
        return "redirect:/payment/admin/detail/" + paymentId;
    }
}