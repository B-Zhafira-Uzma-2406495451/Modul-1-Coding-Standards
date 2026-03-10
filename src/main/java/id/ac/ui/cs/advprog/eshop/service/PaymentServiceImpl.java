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
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderService orderService;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment(UUID.randomUUID().toString(), method, paymentData, order);

        String initialStatus = "REJECTED"; // Set default ke rejected jika validasi gagal

        // Semua mahasiswa wajib mengimplementasikan Voucher Code
        if ("VOUCHER".equals(method)) {
            String voucherCode = paymentData.get("voucherCode");
            if (voucherCode != null && voucherCode.length() == 16 && voucherCode.startsWith("ESHOP")) {
                long numericCount = voucherCode.chars().filter(Character::isDigit).count();
                if (numericCount == 8) {
                    initialStatus = "SUCCESS";
                }
            }
        }
        // Implementasi Cash on Delivery (NPM Ganjil)
        else if ("CASH_ON_DELIVERY".equals(method)) {
            String address = paymentData.get("address");
            String deliveryFee = paymentData.get("deliveryFee");

            // Cek apakah address dan deliveryFee tidak null dan tidak kosong
            if (address != null && !address.trim().isEmpty() &&
                    deliveryFee != null && !deliveryFee.trim().isEmpty()) {
                initialStatus = "SUCCESS";
            }
        }

        // Set status pembayaran dan simpan otomatis ke repository
        payment.setStatus(initialStatus);
        paymentRepository.save(payment);

        // Memastikan status pesanan (Order) juga ter-update mengikuti aturan pembayaran
        setStatus(payment, initialStatus);

        return payment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        paymentRepository.save(payment);

        // Jika payment SUCCESS, order menjadi SUCCESS. Jika REJECTED, order menjadi FAILED.
        if ("SUCCESS".equals(status)) {
            orderService.updateStatus(payment.getOrder().getId(), OrderStatus.SUCCESS.getValue());
        } else if ("REJECTED".equals(status)) {
            orderService.updateStatus(payment.getOrder().getId(), OrderStatus.FAILED.getValue());
        }

        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}