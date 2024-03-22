package com.product.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.product.inventory.model.Product;
import com.product.inventory.model.ProductService;
import com.product.inventory.model.ShippingInfo;
import com.product.inventory.model.ShippingInfoService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ShippingInfoController {

	@Autowired
	private ShippingInfoService shippingInfoService;
	@Autowired
	ProductService productService;
	
	// Create ShippingInfo
	@PostMapping("/shipping-info")
	public ResponseEntity<?> createShippingInfo(@RequestBody ShippingInfo shippingInfo) {
		try {
			ShippingInfo newShippingInfo = shippingInfoService.createShippingInfo(shippingInfo);
			return new ResponseEntity<>(newShippingInfo, HttpStatus.CREATED);
		}
		catch(DataIntegrityViolationException  e){
			String errorMessage = "Duplicate country code. Country code must be unique.";
			return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
		}catch(Exception ex) {
			return new ResponseEntity<>(ex.getCause(), HttpStatus.BAD_REQUEST);
		}
	}

	// Update ShippingInfo
	@PutMapping("/shipping-info/{id}")
	public ResponseEntity<ShippingInfo> updateShippingInfo(@PathVariable int id, @RequestBody ShippingInfo shippingInfo) {
		ShippingInfo updatedShippingInfo = shippingInfoService.updateShippingInfo(id, shippingInfo);
		if (updatedShippingInfo != null) {
			return new ResponseEntity<>(updatedShippingInfo, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// Get All ShippingInfo
	@GetMapping("/shipping-info")
	public ResponseEntity<List<ShippingInfo>> getAllShippingInfo() {
		List<ShippingInfo> shippingInfos = shippingInfoService.getAllShippingInfo();
		return new ResponseEntity<>(shippingInfos, HttpStatus.OK);
	}

	// Get ShippingInfo by ID
	@GetMapping("/shipping-info/{id}")
	public ResponseEntity<?> getShippingInfoById(@PathVariable int id) {
		ShippingInfo shippingInfo = shippingInfoService.getShippingInfoById(id);
		if (shippingInfo != null) {
			return new ResponseEntity<>(shippingInfo, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Invalid shipping info ID.Check and try again",HttpStatus.NOT_FOUND);
		} 
	}

	// Delete ShippingInfo
	@DeleteMapping("/shipping-info/{id}")
	public ResponseEntity<?> deleteShippingInfo(@PathVariable int id) {
		ShippingInfo shippingInfo = shippingInfoService.getShippingInfoById(id);

		//mark the product as unavailable before deletion of shipping country to avoid deletion of child records.
		List<Product> products = shippingInfo.getProducts();
		for(Product product : products) {
			product.setShippingInfo(null);
			product.setStatus("Unavailable");
			productService.updateProduct(product.getId(), product);
		}

		shippingInfo.setProducts(null);
		shippingInfoService.updateShippingInfo(id, shippingInfo);
		shippingInfoService.deleteShippingInfo(id);
		return new ResponseEntity<>("Record deleted successfully",HttpStatus.OK);
	}
}
