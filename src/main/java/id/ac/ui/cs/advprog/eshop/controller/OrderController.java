package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    // Mengganti @Autowired field injection dengan Constructor injection (Best Practice)
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/create")
    public String createOrderPage() { // Parameter 'model' yang tidak terpakai sudah dihapus
        return "order/createOrder";
    }

    @PostMapping("/create")
    public String createOrderPost(@ModelAttribute Order order) {
        orderService.createOrder(order);
        return "redirect:/order/history";
    }

    @GetMapping("/history")
    public String historyOrderPage() {
        return "order/historyOrder";
    }

    @PostMapping("/history")
    public String historyOrderPost(@RequestParam String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        return "order/historyOrder";
    }

    @GetMapping("/pay/{orderId}")
    public String paymentOrderPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "order/payOrder";
    }

    @PostMapping("/pay/{orderId}")
    public String paymentOrderPost(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);

        String generatedPaymentId = "PAYMENT-" + orderId;

        model.addAttribute("order", order);
        model.addAttribute("paymentId", generatedPaymentId);

        return "order/paymentResult";
    }
}