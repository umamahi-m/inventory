package com.product.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.inventory.model.Bill;
import com.product.inventory.model.Product;
import com.product.inventory.model.ProductService;

@Service
public class BillingService {

	@Autowired
   ProductService productService;

	public Bill generateBill(List<Product> productsList) {
		// Fetch products from database based on productIds
		List<Product> products = productService.getProductsByIds(productsList);

		// Calculate total price with discounts
		double subtotal=0,discounts=0,jacketPrice=0;
		long topsCount = 0, jacketsCount = 0;

		for (Product product : products) {
			subtotal +=  product.getPrice();
			// Apply 10% off on Shoes -> 90% of the actual value
			if (product.getName().equalsIgnoreCase("Shoes")) {
				discounts += product.getPrice() * 0.1; 
			}else {
				if(product.getName().equalsIgnoreCase("T-shirt") || product.getName().equalsIgnoreCase("Shirt")){
					topsCount++;
				}else if(product.getName().equalsIgnoreCase("Jacket")) {
					jacketPrice = product.getPrice();
					jacketsCount++;
				}
			}
		}

		// Buy any two tops (T-shirt or Shirts) and get any jacket at half price
		if (topsCount >= 2 && jacketsCount > 0) {
			// Apply half price for jacket
			discounts += jacketPrice * 0.5;
		}

		// Calculate shipping fees
		double shippingFees = calculateShippingFees(products);

		// Apply shipping discount
		// Buy two or more items and get a maximum of 100 INR off on shipping fees
		if (products.size() >=2 && shippingFees > 100.0) {
			discounts += 100; 
		}

		// Calculate total price with discounts and shipping fees
		double totalPriceAfterDiscountsAndShipping = (subtotal - discounts) + shippingFees;

		double tax = applyTax(totalPriceAfterDiscountsAndShipping);
		// Apply tax (18% GST)
		double totalPrice = totalPriceAfterDiscountsAndShipping + tax;

		// Return the bill
		return new Bill(totalPrice, shippingFees, tax,discounts,totalPrice);
	}

	private static double calculateShippingFees(List<Product> products) {
		// Calculate total weight
		double shippingFees = 0.0;
		for (Product product : products) {
			shippingFees += (product.getWeight() * product.getShippingInfo().getRate());
		}

		return shippingFees;
	}

	private static double applyTax(double totalPrice) {
		// Apply 18% GST
		return totalPrice * 0.18; // 18% tax
	}
}

