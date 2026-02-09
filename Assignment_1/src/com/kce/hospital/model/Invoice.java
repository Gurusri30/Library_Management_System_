package com.kce.hospital.model;

public class Invoice {
    private TestOrder order;
    private double total;
    private boolean paid;

    public Invoice(TestOrder order) {
        this.order = order;
        this.total = 0;
        for (TestOrderItem item : order.getItems()) {
            total += item.getTest().getPrice();
        }
    }

    public double getTotal() {
        return total;
    }

    public boolean isPaid() {
        return paid;
    }

    public void makePayment(double amount) {
        if (amount >= total) {
            paid = true;
        }
    }

    // ✅ Add this getter method
    public TestOrder getOrder() {
        return order;
    }
}
