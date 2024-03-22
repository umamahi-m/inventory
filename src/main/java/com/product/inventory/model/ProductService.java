package com.product.inventory.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductsRepository productRepository;

    //create a new product
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    
    // update an existing product
    public Product updateProduct(int id, Product product) {
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setShippingInfo(product.getShippingInfo());
            return productRepository.save(existingProduct);
        } else {
            return null; 
        }
    }
    
    // Delete an existing product
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    // Get a product by ID
    public Product getProductById(int id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.orElse(null); // Return null if product not found
    }
    
    // Get a product by name
    public Product getProductByName(String name) {
        Optional<Product> productOptional = productRepository.getByName(name.toLowerCase());
        return productOptional.orElse(null); // Return null if product not found
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

	public List<Product> getProductsByIds(List<Product> productsList) {
		List<Product> products = new ArrayList<Product>();
		for(Product product : productsList) {
			if(product.getId() != 0) {
				products.add(productRepository.findById(product.getId()).get());
			}
			if(product.getName() != null) {
				products.add(productRepository.getByName(product.getName()).get());
			}
		}
		return products;
	}
}
