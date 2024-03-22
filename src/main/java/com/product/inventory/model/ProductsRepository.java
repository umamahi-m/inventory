package com.product.inventory.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "products",path="products")
public interface ProductsRepository extends JpaRepository<Product, Integer>{

	Optional<Product> findById(Long id);

	void deleteById(Long id);

	Optional<Product> findByName(String name);
	Optional<Product> getByName(String name);
}
