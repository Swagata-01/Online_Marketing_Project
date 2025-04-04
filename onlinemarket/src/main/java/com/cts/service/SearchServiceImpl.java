package com.cts.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.dto.ProductDTO;
import com.cts.entity.Products;
import com.cts.repository.ProductRepository;

@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	ProductRepository productRepository;
	
	@Override
	public List<ProductDTO> searchProductByName(String name) {
		// TODO Auto-generated method stub
		List<Products> products = productRepository.findByNameContainingIgnoreCase(name);
		
		List<ProductDTO> productDTOs = new ArrayList<>();
	    
	    for (Products product : products) {
	        productDTOs.add(new ProductDTO(product));
	    }
	    return productDTOs;
	}

	@Override
	public List<ProductDTO> searchProductBySubsCount(int count) {
		// TODO Auto-generated method stub
		List<Products> products = productRepository.searchBySubsCount(count);
		
		List<ProductDTO> productDTOs = new ArrayList<>();
	    
	    for (Products product : products) {
	        productDTOs.add(new ProductDTO(product));
	    }
	    return productDTOs;
	}

	@Override
	public List<ProductDTO> searchProductByRating(double rating) {
		// TODO Auto-generated method stub
		List<Products> products = productRepository.searchProductByRating(rating);
		
		List<ProductDTO> productDTOs = new ArrayList<>();
	    
	    for (Products product : products) {
	        productDTOs.add(new ProductDTO(product));
	    }
	    return productDTOs;
	}

	@Override
	public List<ProductDTO> searchProductBySubsCountAndRating(int count, double rating) {
		// TODO Auto-generated method stub
		List<Products> products = productRepository.searchProductBySubsCountAndRating(count,rating);
		
		List<ProductDTO> productDTOs = new ArrayList<>();
	    
	    for (Products product : products) {
	        productDTOs.add(new ProductDTO(product));
	    }
		return productDTOs;
	}

}
