package co.stutzen.shopzen.bo;

public class OrderBO {

    private String orderId;

    private String orderDate;

    private double amount;

    private String status;

    private String payment;

    private int mainid;

    public OrderBO(String id, String date, double amount, String status, String delivery, int mainid){
        this.orderId = id;
        this.orderDate = date;
        this.amount = amount;
        this.status = status;
        this.payment = delivery;
        this.mainid = mainid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public int getMainid() {
        return mainid;
    }

    public void setMainid(int mainid) {
        this.mainid = mainid;
    }
}
