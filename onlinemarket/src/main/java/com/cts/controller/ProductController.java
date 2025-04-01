package com.cts.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cts.dto.ProductDTO;
import com.cts.entity.Products;
import com.cts.service.ProductService;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:3000")
@RequestMapping("/api/auth")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	//API call for adding new Product
	@PostMapping("/addProduct")
	public ResponseEntity<Products>  createNewProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam MultipartFile imageFile) throws IOException
	{
	 return ResponseEntity.ok(productService.addProduct(name, description, imageFile));
	
	}
	
	//API call for viewing all product
	@GetMapping("/viewAllProducts")
	public ResponseEntity<?> viewAllProducts(){
		List<ProductDTO> products = productService.viewAllProducts();
		if(products == null || products.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("No products available to display.");
		}
		return ResponseEntity.ok(products);
	}
	
	//API call for viewing specific product using specific Product id
	@GetMapping("/viewProductDetails/{productId}")
	public Products viewProductDetails(@PathVariable int productId){
		return productService.viewProductById(productId);
	}
	
	
	//API call for removing specific product using Product id
	@DeleteMapping("/removeProduct/{productId}")
	public String removeProduct(@PathVariable int productId) {
		productService.removeProduct(productId);
		return "Product deleted successfully";
	}
	
	
	//API call for fetching image of specific product using id
	@GetMapping("product/image/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable int id){
		byte[] image = productService.getProductImage(id);
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_PNG)
				.body(image);
	}
	
}
