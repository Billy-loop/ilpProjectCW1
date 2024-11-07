package uk.ac.ed.inf.pizzadronz.model;
import uk.ac.ed.inf.pizzadronz.constant.*;

public class OrderValidationResult {
    private OrderStatus orderStatus;
    private OrderValidationCode orderValidationCode;


    public OrderValidationResult(OrderValidationCode orderValidationCode, OrderStatus orderStatus) {
        this.orderValidationCode = orderValidationCode;
        this.orderStatus = orderStatus;
    }

    public OrderValidationCode getOrderValidationCode() {
        return orderValidationCode;
    }
    public void setOrderValidationCode(OrderValidationCode orderValidationCode) {
        this.orderValidationCode = orderValidationCode;
    }
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
