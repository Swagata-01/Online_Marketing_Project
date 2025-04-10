package com.cts.controller;

import java.io.IOException;
import java.util.List;
import com.cts.service.ProductServiceImpl;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cts.dto.ProductDTO;
import com.cts.entity.Products;
import com.cts.entity.ReviewsAndRatings;
import com.cts.exception.UserNotFoundException;
import com.cts.entity.ProductSubscription;
import com.cts.service.ProductService;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:3000")
@RequestMapping("/OMP")
public class ProductController {

    private final ProductServiceImpl productServiceImpl;
	
	@Autowired
	ProductService productService;

    ProductController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }
	
	//API call for adding new Product
	@PostMapping("/admin/addProduct")
	public ResponseEntity<Products>  createNewProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam MultipartFile imageFile) throws IOException
	{
	 return ResponseEntity.ok(productService.addProduct(name, description, imageFile));
	
	}
	
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
	
	
	//API call for fetching image of specific product using id
	@GetMapping("product/image/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable int id){
		byte[] image = productService.getProductImage(id);
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_PNG)
				.body(image);
	}
	
	@PutMapping("/admin/updateProduct/{productId}")
    public ResponseEntity<Products> updateProduct(
            @PathVariable int productId,
            @RequestParam(required=false)  String name,
            @RequestParam (required=false) String description,
            @RequestParam (required=false) MultipartFile productImage) throws Exception
            
    {
        return ResponseEntity.ok(productService.updateProduct(productId, name, description, productImage));
    }
	
	@PostMapping("/addSubscription")
    public ResponseEntity<Products> addSubscription(@RequestParam int userId,@RequestParam int productId){
            return ResponseEntity.ok(productServiceImpl.addSubscription(userId,productId));
 
    }
 
    @PutMapping("/removeSubscription")
    public ResponseEntity<Products> removeSubscription(@RequestParam int userId,@RequestParam int productId){
        return ResponseEntity.ok(productServiceImpl.removeSubscription(userId,productId));
    }
 
    @GetMapping("/viewSubscriptionList")
    public ResponseEntity<List<ProductSubscription>> getSubscriptionList(@RequestParam int productId) {
        return ResponseEntity.ok(productServiceImpl.getSubscriptionList(productId));
    }

}
