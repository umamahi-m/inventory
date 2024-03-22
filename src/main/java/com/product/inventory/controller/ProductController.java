package com.product.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.product.inventory.model.Product;
import com.product.inventory.model.ProductService;
import com.product.inventory.model.ShippingInfo;
import com.product.inventory.model.ShippingInfoService;

@RestController
@RequestMapping("/api")
public class ProductController {
	@Autowired
	ProductService productService;
	@Autowired
	ShippingInfoService shippingInfoService;

	@PostMapping("/products")
	public ResponseEntity<?> addProduct(@RequestBody Product product) {
		ShippingInfo shippingInfo = shippingInfoService.getShippingInfoById(product.getShippingInfoId());
		if(shippingInfo == null) {
			return new ResponseEntity<>("ShippingInfo not found for id: " + product.getShippingInfoId(),HttpStatus.BAD_REQUEST);
		}

		product.setShippingInfo(shippingInfo);

		try {
			// Save the product
			Product newProduct = productService.createProduct(product);
			return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
		}
		catch(DataIntegrityViolationException  e){
			String errorMessage = "Product already present.Try adding different product";
			return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
		}catch(Exception ex) {
			return new ResponseEntity<>(ex.getCause(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/products")
	public Iterable<Product> getProducts(){
		return productService.getAllProducts();
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<?> getShippingInfoById(int id) {
		Product product =  productService.getProductById(id);
		if(product == null) {
			return new ResponseEntity<>("Invalid product id.Check and try again", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(product, HttpStatus.OK);
	}
	
	@PutMapping("/products/{id}")
	public ResponseEntity<?> updateProduct(@RequestBody Product product,@PathVariable("id") int id) {
		Product updatedProduct = productService.updateProduct(id, product);
		if(updatedProduct != null) {
			return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
		}else {
			return new ResponseEntity<>("The product you are trying to update does not exist. Please make sure you are providing the correct product ID", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable("id") int id) {
		try {
			productService.deleteProduct(id);
			return new ResponseEntity<>("Product deleted successfully",HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Error while deleting the product",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
