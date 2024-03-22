package com.product.inventory.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bill {
    @JsonProperty("subtotal")
    private double subtotal;

    @JsonProperty("shipping_fees")
    private double shippingFees;

    @JsonProperty("GST")
    private double gst;

    @JsonProperty("discount")
    private double discount;
    
    @JsonProperty("total")
    private double totalPrice;

    public Bill(double subtotal, double shippingFees, double gst,double totalPrice, double discount) {
        this.subtotal = subtotal;
        this.shippingFees = shippingFees;
        this.gst = gst;
        this.discount = discount;
        this.totalPrice = totalPrice;
    }
}
