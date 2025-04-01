package com.cts.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cts.dto.ProductDTO;
import com.cts.entity.Products;

public interface ProductService {
	public List<ProductDTO> viewAllProducts();
	
	public Products viewProductById(int id);
	
	public Products addProduct(String name, String description, MultipartFile imageFile) throws IOException;
	
	public void removeProduct(int id);

	public byte[] getProductImage(int id);
	
}
