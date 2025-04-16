//package com.cts.service;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.springframework.web.multipart.MultipartFile;
//
//import com.cts.dto.ProductDTO;
//import com.cts.dto.ProductRatingSubscriptionDTO;
//import com.cts.entity.ProductSubscription;
//import com.cts.entity.Products;
//import com.cts.entity.ReviewsAndRatings;
//import com.cts.exception.InvalidProductException;
//
//public interface ProductService {
//	public List<ProductDTO> viewAllProducts();
//	
//	public Products viewProductById(int id);
//	
//	public Products addProduct(String name, String description, MultipartFile imageFile) throws IOException;
//
//	public Products updateProduct(int productId, String name, String description, MultipartFile productImage) throws InvalidProductException, IOException;
//
//	public byte[] getProductImage(int id);
//	
//	public void removeProduct(int productId);
//
//	 public Products addSubscription(int userId,int productId);
//
//	 public Products removeSubscription(int userId,int productId);
//
//	 public List<ProductSubscription> getSubscriptionList(int productId);
//
//	 public List<ProductRatingSubscriptionDTO> getProductSubscriptionList(int userId);
//	
//	//public List<ProductSubscription> getSubscriptionsByEmail(String email);
//
//	//public List<ReviewsAndRatings> getReviewsByEmail(String email);
//	
//	public List<ProductRatingSubscriptionDTO> findTopSubscribedProduct();
//	
//	public List<ProductRatingSubscriptionDTO> findTopRatedProducts();
//	
//}

package com.cts.service;
 
import java.io.IOException;

import java.util.List;
 
import org.springframework.web.multipart.MultipartFile;
 
//import com.cts.dto.ProductDTO;

//import com.cts.dto.ProductRatingSubscriptionDTO;

import com.cts.dto.ProductViewDTO;

import com.cts.entity.ProductSubscription;

import com.cts.entity.Products;

import com.cts.entity.ReviewsAndRatings;

import com.cts.exception.InvalidProductException;
 
public interface ProductService {

	public List<ProductViewDTO> viewAllProducts();

	public ProductViewDTO viewProductById(int id);

	public Products addProduct(String name, String description, MultipartFile imageFile) throws IOException;
 
 
	public byte[] getProductImage(int id);
	public byte[] getProductImageByName(String name);
 
	 public Products addSubscription(int userId,int productId);
 
	 public Products removeSubscription(int userId,int productId);
 
	 public List<ProductSubscription> getSubscriptionList(int productId);
 
	 public List<ProductViewDTO> getProductSubscriptionList(int userId);

	//public List<ProductSubscription> getSubscriptionsByEmail(String email);
 
	 public Products updateProduct (String name, String upName, String description, MultipartFile imageFile) throws InvalidProductException, IOException;
 
	//public List<ReviewsAndRatings> getReviewsByEmail(String email);

//	public List<ProductRatingSubscriptionDTO> findTopSubscribedProduct();

	public List<ProductViewDTO> findTopSubscribedProduct();

	public List<ProductViewDTO> findTopRatedProducts();

}

 
