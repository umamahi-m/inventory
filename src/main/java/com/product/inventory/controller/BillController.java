package com.product.inventory.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.product.inventory.model.Bill;
import com.product.inventory.model.Product;
import com.product.inventory.model.ProductService;

@RestController
public class BillController {
	@Autowired
	ProductService productService;
	
	@PostMapping("api/generate-bill")
	public ResponseEntity<?> generateBill(@RequestBody List<Product> productsList) {
		// Fetch products from database based on productIds
		List<Product> products = new ArrayList<Product>();
		try {
			products = productService.getProductsByIds(productsList);
		}catch(Exception e) {
			return new ResponseEntity<>("No products found",HttpStatus.BAD_REQUEST);
		}
		
		if(products == null || products.size() == 0) {
			return new ResponseEntity<>("No products found",HttpStatus.BAD_REQUEST);
		}
		
		for (Product product : products) {
			//if multiple products are not available need to send the error with list of products
			if(!product.getStatus().equalsIgnoreCase("Available")) {
				return new ResponseEntity<>("Product " + product.getName()+" is not available currently.Please remove it to proceed further",HttpStatus.BAD_REQUEST);
			}
			Product requestedProduct = productsList.stream().filter(p -> p.getName().equalsIgnoreCase(product.getName())).findFirst().get();
			if(requestedProduct.getQuantity() > product.getQuantity()) {
				return new ResponseEntity<>("Product " + product.getName()+" is not available with the required quantity. Available quantity is : "+product.getQuantity(),HttpStatus.BAD_REQUEST);
			}
		}

		// Calculate total price with discounts
		double subtotal=0,discounts=0,jacketPrice=0,shippingFees=0;
		long topsCount = 0, jacketsCount = 0;

		for (Product product : products) {
			Product requestedProduct = productsList.stream().filter(p -> p.getName().equalsIgnoreCase(product.getName())).findFirst().get();

			//reducing the quantity of product after purchase/ bill generation
			product.setQuantity(product.getQuantity() - requestedProduct.getQuantity());
			productService.updateProduct(product.getId(), product);

			subtotal +=  requestedProduct.getQuantity() * product.getPrice();
			shippingFees += (product.getWeight() * 10 * requestedProduct.getQuantity() * product.getShippingInfo().getRate());
			// Apply 10% off on Shoes -> 90% of the actual value
			if (product.getName().equalsIgnoreCase("Shoes")) {
				discounts += product.getPrice() * 0.1; 
			}else  if(product.getName().equalsIgnoreCase("T-shirt") || product.getName().equalsIgnoreCase("Shirt")){
				topsCount += requestedProduct.getQuantity();
			}else if(product.getName().equalsIgnoreCase("Jacket")) {
				jacketPrice = product.getPrice();
				jacketsCount += requestedProduct.getQuantity();
			}
		}

		// Buy any two tops (T-shirt or Shirts) and get any jacket at half price
		if (topsCount >= 2 && jacketsCount > 0) {
			// Apply half price for jacket
			discounts += jacketPrice * 0.5;
		}

		// Apply shipping discount
		// Buy two or more items and get a maximum of 100 INR off on shipping fees
		if (products.size() >=2 && shippingFees > 100.0) {
			discounts += 100; 
		}

		// Calculate total price with discounts and shipping fees
		double totalPriceAfterDiscountsAndShipping = (subtotal - discounts) + shippingFees;

		// Apply tax (18% GST)
		double tax = totalPriceAfterDiscountsAndShipping * 0.18;
		double totalPrice = totalPriceAfterDiscountsAndShipping + tax;

		return new ResponseEntity<>(new Bill(roundOfValue(subtotal), roundOfValue(shippingFees), roundOfValue(tax),roundOfValue(totalPrice), roundOfValue(discounts)),HttpStatus.OK);
	}

	private static double roundOfValue(double value) {
		return Math.round(value * 100D) / 100D;
	}
}
