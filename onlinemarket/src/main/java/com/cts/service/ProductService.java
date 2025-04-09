package com.cts.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cts.dto.ProductDTO;
import com.cts.entity.ProductSubscription;
import com.cts.entity.Products;
import com.cts.entity.ReviewsAndRatings;
import com.cts.exception.InvalidProductException;

public interface ProductService {
	public List<ProductDTO> viewAllProducts();
	
	public Products viewProductById(int id);
	
	public Products addProduct(String name, String description, MultipartFile imageFile) throws IOException;
	
	public void removeProduct(int id);

	public byte[] getProductImage(int id);

	 public Products addSubscription(int userId,int productId);

	 public Products removeSubscription(int userId,int productId);

	 public List<ProductSubscription> getSubscriptionList(int productId);

	 public List<ProductDTO> getProductSubscriptionList(int userId);
	

	public Products updateProduct (String name, String description, MultipartFile productImage) throws InvalidProductException, IOException;

	
}
