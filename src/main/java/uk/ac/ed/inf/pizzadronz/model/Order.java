package uk.ac.ed.inf.pizzadronz.model;

import java.util.ArrayList;

public class Order {
    private String orderNo;
    private String orderDate;
    private int priceTotalInPence;
    private ArrayList<Pizza> pizzasInOrder;
    private CardInfo creditCardInformation;

    public Order(String orderNo, String orderDate, int priceTotalInPence,
                 ArrayList<Pizza> pizzasInOrder, CardInfo creditCardInformation) {
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.priceTotalInPence = priceTotalInPence;
        this.pizzasInOrder = pizzasInOrder;
        this.creditCardInformation = creditCardInformation;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    public int getPriceTotalInPence() {
        return priceTotalInPence;
    }
    public void setPriceTotalInPence(int priceTotalInPence) {
        this.priceTotalInPence = priceTotalInPence;
    }
    public ArrayList<Pizza> getPizzasInOrder() {
        return pizzasInOrder;
    }
    public void setPizzasInOrder(ArrayList<Pizza> pizzasInOrder) {
        this.pizzasInOrder = pizzasInOrder;
    }
    public CardInfo getCreditCardInformation() {
        return creditCardInformation;
    }
    public void setCreditCardInformation(CardInfo creditCardInformation) {
        this.creditCardInformation = creditCardInformation;
    }
}
