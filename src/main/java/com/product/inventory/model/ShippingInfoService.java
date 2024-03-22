package com.product.inventory.model;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingInfoService {

	@Autowired
	private ShippingInfoRepository shippingInfoRepository;

	// Create a new ShippingInfo
	public ShippingInfo createShippingInfo(ShippingInfo shippingInfo) {
		return shippingInfoRepository.save(shippingInfo);
	}

	// Update an existing ShippingInfo
	public ShippingInfo updateShippingInfo(int id, ShippingInfo shippingInfo) {
		Optional<ShippingInfo> existingShippingInfoOptional = shippingInfoRepository.findById(id);
		if (existingShippingInfoOptional.isPresent()) {
			ShippingInfo existingShippingInfo = existingShippingInfoOptional.get();
			existingShippingInfo.setCountryCode(shippingInfo.getCountryCode());
			existingShippingInfo.setRate(shippingInfo.getRate());
			return shippingInfoRepository.save(existingShippingInfo);
		} else {
			return null; // ShippingInfo with given ID not found
		}
	}

	// Delete an existing ShippingInfo
	public void deleteShippingInfo(int id) {
		shippingInfoRepository.deleteById(id);
	}

	// Get a ShippingInfo by ID
	public ShippingInfo getShippingInfoById(int id) {
		Optional<ShippingInfo> shippingInfoOptional = shippingInfoRepository.findById(id);
		return shippingInfoOptional.orElse(null); // Return null if ShippingInfo not found
	}

	// Get all ShippingInfo
	public List<ShippingInfo> getAllShippingInfo() {
		return shippingInfoRepository.findAll();
	}

	// Check if the given country code exists in the ShippingInfo table
	public boolean countryCodeExists(String countryCode) {
		return shippingInfoRepository.existsByCountryCode(countryCode);
	}


}
