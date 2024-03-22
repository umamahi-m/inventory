package com.product.inventory.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "shippinginfo",path="shippinginfo")
public interface ShippingInfoRepository extends JpaRepository<ShippingInfo, Integer>{
    boolean existsByCountryCode(String countryCode);
}

