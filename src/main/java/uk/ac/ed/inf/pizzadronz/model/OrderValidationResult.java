package uk.ac.ed.inf.pizzadronz.model;

import lombok.Getter;
import lombok.Setter;
import uk.ac.ed.inf.pizzadronz.constant.OrderStatus;
import uk.ac.ed.inf.pizzadronz.constant.OrderValidationCode;

@Getter
@Setter
public class OrderValidationResult {

    private OrderStatus orderStatus;
    private OrderValidationCode orderValidationCode;

    public OrderValidationResult (OrderStatus orderStatus, OrderValidationCode orderValidationCode) {
        this.orderStatus = orderStatus;
        this.orderValidationCode = orderValidationCode;
    }

}
