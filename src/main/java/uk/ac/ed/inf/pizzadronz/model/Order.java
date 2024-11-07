package uk.ac.ed.inf.pizzadronz.model;

import lombok.Getter;
import lombok.Setter;
import uk.ac.ed.inf.pizzadronz.constant.OrderStatus;
import uk.ac.ed.inf.pizzadronz.constant.OrderValidationCode;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
public class Order {
    private OrderStatus orderStatus = OrderStatus.UNDEFINED;
    private OrderValidationCode orderValidationCode = OrderValidationCode.UNDEFINED;
    private String orderNo;
    private LocalDate orderDate;
    private int priceTotalInPence;
    private List<Pizza> pizzasInOrder;
    private CardInfo creditCardInformation;
}
