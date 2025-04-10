package com.cts.service;

import java.util.List;

import com.cts.dto.ProductDTO;

public interface SearchService {
	public List<ProductDTO> searchProductByName(String name);
	
	public List<ProductDTO> searchProductBySubsCount(int count);

	public List<ProductDTO> searchProductByRating(double rating);

	public List<ProductDTO> searchProductBySubsCountAndRating(int count, double rating);
	
	public List<ProductDTO> searchProductByNameSubsRating(String name, int count, double rating);
}
