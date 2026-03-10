package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create")
    public String createOrderPage(Model model) {
    }

    @PostMapping("/create")
    public String createOrderPost(@ModelAttribute Order order) {
    }

    @GetMapping("/history")
    public String historyOrderPage() {
    }

    @PostMapping("/history")
    public String historyOrderPost(@RequestParam String author, Model model) {
    }

    @GetMapping("/pay/{orderId}")
    public String paymentOrderPage(@PathVariable String orderId, Model model) {
    }

    @PostMapping("/pay/{orderId}")
    public String paymentOrderPost(@PathVariable String orderId, Model model) {
    }
}