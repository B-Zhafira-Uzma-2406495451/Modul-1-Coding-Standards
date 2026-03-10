package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;
    private Order order;

    public Payment(String id, String method, Map<String, String> paymentData, Order order) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.method = method;
        this.paymentData = paymentData;
        this.order = order;
        this.status = "WAITING_PAYMENT";
    }
}