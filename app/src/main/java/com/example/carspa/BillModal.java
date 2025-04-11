package com.example.carspa;

public class BillModal {
    private String saleId;
    private String billNo;
    private String billDate;
    private double totalAmount;
    private String customerName;
    private String customerPhone;

    public BillModal() { } // Required for Firebase

    public BillModal(String saleId, String billNo, String billDate, double totalAmount,String customerName,String customerPhone) {
        this.saleId = saleId;
        this.billNo = billNo;
        this.billDate = billDate;
        this.totalAmount = totalAmount;
        this.customerName=customerName;
        this.customerPhone=customerPhone;
    }

    public String getSaleId() { return saleId; }
    public void setSaleId(String saleId) { this.saleId = saleId; }

    public String getBillNo() { return billNo; }
    public String getBillDate() { return billDate; }
    public double getTotalAmount() { return totalAmount; }

    public String getCustomerName() { return customerName; }

    public String getCustomerPhone() { return customerPhone; }
}
