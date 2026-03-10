package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderService orderService;

    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment(UUID.randomUUID().toString(), method, paymentData, order);

        String initialStatus = "REJECTED";

        if ("VOUCHER".equals(method)) {
            String voucherCode = paymentData.get("voucherCode");
            if (voucherCode != null && voucherCode.length() == 16 && voucherCode.startsWith("ESHOP")) {
                long numericCount = voucherCode.chars().filter(Character::isDigit).count();
                if (numericCount == 8) {
                    initialStatus = "SUCCESS";
                }
            }
        }

        else if ("CASH_ON_DELIVERY".equals(method)) {
            String address = paymentData.get("address");
            String deliveryFee = paymentData.get("deliveryFee");

            if (address != null && !address.trim().isEmpty() &&
                    deliveryFee != null && !deliveryFee.trim().isEmpty()) {
                initialStatus = "SUCCESS";
            }
        }

        payment.setStatus(initialStatus);
        paymentRepository.save(payment);

        setStatus(payment, initialStatus);

        return payment;
    }

    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        paymentRepository.save(payment);

        if ("SUCCESS".equals(status)) {
            orderService.updateStatus(payment.getOrder().getId(), OrderStatus.SUCCESS.getValue());
        } else if ("REJECTED".equals(status)) {
            orderService.updateStatus(payment.getOrder().getId(), OrderStatus.FAILED.getValue());
        }

        return payment;
    }

    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}