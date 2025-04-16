//package com.cts.service;
// 
//import com.cts.dto.ProductRatingSubscriptionDTO;
// 
//import java.io.IOException;
//
//import java.time.LocalDateTime;
//
//import java.util.ArrayList;
//
//import java.util.List;
//
//import java.util.Optional;
//
//import java.util.stream.Collectors;
// 
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.stereotype.Service;
//
//import org.springframework.web.multipart.MultipartFile;
// 
//import com.cts.dto.ProductDTO;
//
//import com.cts.entity.ProductSubscription;
//
//import com.cts.entity.Products;
//
//import com.cts.entity.ReviewsAndRatings;
//
//import com.cts.entity.User;
//
//import com.cts.repository.ProductRepository;
//
//import com.cts.repository.ReviewAndRatingRepository;
//
//import com.cts.repository.UserRepository;
//
//import com.cts.exception.*;
//
//import com.cts.mapper.ProductMapper;
// 
//@Service
//
//public class ProductServiceImpl implements ProductService {
//
//    @Autowired
//
//    ProductRepository productRepository;
// 
//    @Autowired
//
//    UserRepository userRepository;
//
//    @Autowired
//
//    ReviewAndRatingRepository reviewAndRatingRepository;
//
//
//    //view all the products
//
//    public List<ProductDTO> viewAllProducts() {
//
//        List<Products> products = productRepository.findAll();
//
//        List<ProductDTO> productDTOs = new ArrayList<>();
//
//        for (Products product : products) {
//
//            productDTOs.add(new ProductDTO(product));
//
//        }
//
//        return productDTOs;
//
//    }
// 
//    //view product by id
//
//    @Override
//
//    public Products viewProductById(int id) {
//
//        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
//
//    }
// 
//    
// 
//    @Override
//    public Products addProduct(String name, String description, MultipartFile imageFile) throws IOException {
//
//        Products product = new Products();
//
//        product.setName(name);
//
//        product.setDescription(description);
//
//        product.setImages(imageFile.getBytes());
//
//        return productRepository.save(product);
//
//    }
//    
//    public void removeProduct(int productId) {
//    	
//    	Products product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
//    	
//    	product.setIsActive(false);
//    	
//    	productRepository.save(product);
//    }
//
//    @Override
//
//    public Products updateProduct(int productId, String name, String description, MultipartFile productImage) throws  InvalidProductException, IOException {
//
//        Products product = productRepository.findById(productId).orElseThrow(() -> new InvalidProductException("Product not found with ID: " + productId));
//
//        if (name != null)product.setName(name);
//
//        if (description != null)product.setDescription(description);
//
//        if (productImage != null)product.setImages(productImage.getBytes());
// 
//        return productRepository.save(product);
//
// 
//    }
// 
//    @Override
//
//    public byte[] getProductImage(int id) {
//
//        Products product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
//
//        return product.getImages();
//
//    }
// 
//    @Override
//
//    public Products addSubscription(int userId, int productId) {
//
//        Products product = productRepository.findById(productId).orElseThrow(() -> new InvalidProductException("Product not found"));
//
//        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
//
//        if (!product.getIsActive()) {
//
//            throw new InvalidProductException("Product is not active");
//
//        }
//
//        Optional<ProductSubscription> existingSubscription = product.getProductSubscriptionList().stream()
//
//            .filter(subscription -> subscription.getUser().getUserID() == userId)
//
//            .findFirst();
//
//        if (existingSubscription.isPresent()) {
//
//             ProductSubscription subscription = existingSubscription.get();
//
//        if (!subscription.isOptIn()) {
//
//            // Reactivate the subscription
//
//            subscription.setOptIn(true);
//
//            subscription.setUpdatedOn(LocalDateTime.now());
//
//            productRepository.save(product);
//
//            return product;
//
//        } else {
//
//            throw new InvalidProductException("Already subscribed to this product");
//
//        }
//
//        }
//
//        ProductSubscription subscription = new ProductSubscription();
//
//        subscription.setUser(user);
//
//        subscription.setProducts(product);
//
//        product.getProductSubscriptionList().add(subscription);
//
//        return productRepository.save(product);
//
//    }
//
//    @Override
//
//    public Products removeSubscription(int userId, int productId) {
//
//        Products product = productRepository.findById(productId).orElseThrow(() -> new InvalidProductException("Product not found"));
//
//        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
//
//        if (!product.getIsActive()) {
//
//            throw new InvalidProductException("Product is not active");
//
//        }
//
//        Optional<ProductSubscription> existingSubscription = product.getProductSubscriptionList().stream()
//
//            .filter(subscription -> subscription.getUser().getUserID() == userId)
//
//            .findFirst();
//
//        if (!existingSubscription.isPresent() || !existingSubscription.get().isOptIn()) {
//
//            throw new InvalidProductException("User has not subscribed to this product");
//
//        }
//
//        // Set optIn to false instead of removing the subscription
//
//        existingSubscription.get().setOptIn(false);
//
//        existingSubscription.get().setUpdatedOn(LocalDateTime.now());
//
//        productRepository.save(product);
//
//        return product;
//
//}
//
//    @Override
//
//    public List<ProductSubscription> getSubscriptionList(int productId) {
//
//        Products product = productRepository.findById(productId)
//
//        .orElseThrow(() -> new InvalidProductException("Product not found"));
//
//    return product.getProductSubscriptionList().stream()
//
//        .filter(ProductSubscription::isOptIn)
//
//        .collect(Collectors.toList());
//
//    }
//
//    @Override
//    public List<ProductRatingSubscriptionDTO> getProductSubscriptionList(int userId){
//
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//
//        return user.getProductSubscriptionList().stream()
//                   .filter(ProductSubscription::isOptIn)
//                   .map(subscription -> {
//                       Products product = subscription.getProducts();
//                       long optInTrueCount = product.getProductSubscriptionList().stream()
//                                                    .filter(ProductSubscription::isOptIn)
//                                                    .count();
//                       return new ProductRatingSubscriptionDTO(ProductMapper.toDTO(product), 0.0, (int) optInTrueCount);
//                   })
//                   .collect(Collectors.toList());
//    }
//        @Override
//
//    public List<ProductRatingSubscriptionDTO> findTopSubscribedProduct() {
//
//    	List<ProductRatingSubscriptionDTO> productSubscriptionList = new ArrayList<>();
//
//    	List<Integer> productIDs = productRepository.findTopSubscribedProduct();
//
//    	for(Integer productId : productIDs) {
//
//    		Products product = viewProductById(productId);
// 
//    		int optInTrueCount = (int) product.getProductSubscriptionList()
//
//    				.stream()
//
//    				.filter(ProductSubscription :: isOptIn)
//
//    				.count();
//
//    		productSubscriptionList.add(new ProductRatingSubscriptionDTO(ProductMapper.toDTO(product),0.0,optInTrueCount));
//
//    	}  	
//
//    	return productSubscriptionList;
//
//    }
//
//    @Override
//
//    public List<ProductRatingSubscriptionDTO> findTopRatedProducts() {
//
//        List<Object[]> results = reviewAndRatingRepository.findTopRatedProducts();
//
//        List<ProductRatingSubscriptionDTO> topRatedProducts = new ArrayList<>();
// 
//        for (Object[] result : results) {
//
//            Integer productId = (Integer) result[0];
//
//            Double topRating = (Double) result[1];
//
//            Products product = productRepository.findById(productId).orElse(null);
//
//            if (product != null) {
//
//                topRatedProducts.add(new ProductRatingSubscriptionDTO(ProductMapper.toDTO(product), topRating,0));
//
//            }
//
//        }
// 
//        return topRatedProducts;
//
//    }
//
//    }
// 

package com.cts.service;

//import com.cts.dto.ProductRatingSubscriptionDTO;

import com.cts.dto.ProductViewDTO;
 
import java.io.IOException;
 
import java.time.LocalDateTime;
 
import java.util.ArrayList;
 
import java.util.List;
 
import java.util.Optional;
 
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
 
import org.springframework.stereotype.Service;
 
import org.springframework.web.multipart.MultipartFile;

//import com.cts.dto.ProductDTO;
 
import com.cts.entity.ProductSubscription;
 
import com.cts.entity.Products;
 
import com.cts.entity.ReviewsAndRatings;
 
import com.cts.entity.User;
 
import com.cts.repository.ProductRepository;

import com.cts.repository.ProductViewRepository;

import com.cts.repository.ReviewAndRatingRepository;

import com.cts.repository.ReviewAndRatingRepository;

import com.cts.repository.UserRepository;
 
import com.cts.exception.*;
 
//import com.cts.mapper.ProductMapper;

@Service
 
public class ProductServiceImpl implements ProductService {
 
    @Autowired
 
    ProductRepository productRepository;

    @Autowired

    ProductViewRepository productViewRepo;

    @Autowired
 
    UserRepository userRepository;
 
    @Autowired
 
    ReviewAndRatingRepository reviewAndRatingRepository;
 
 
    //view all the products
 
    public List<ProductViewDTO> viewAllProducts() {
 
        return productViewRepo.findAll();
 
//        List<ProductDTO> productDTOs = new ArrayList<>();

//

//        for (Products product : products) {

//

//            productDTOs.add(new ProductDTO(product));

//

//        }

//

//        return productDTOs;
 
    }

    //view product by id
 
    @Override
 
    public ProductViewDTO viewProductById(int id) {
 
        return productViewRepo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
 
    }


    @Override

    public Products addProduct(String name, String description, MultipartFile imageFile) throws IOException {
 
        Products product = new Products();
 
        product.setName(name);
 
        product.setDescription(description);
 
        product.setImages(imageFile.getBytes());
 
        return productRepository.save(product);
 
    }
 
    public Products updateProduct(String name, String upName, String description, MultipartFile imageFile) throws  InvalidInputException, IOException {
        Products product = productRepository.findByName(name).orElseThrow(() -> new InvalidInputException("Product not found with Name: " + name));
        if (upName != null)product.setName(upName);
        if (description != null)product.setDescription(description);
        if (imageFile != null)product.setImages(imageFile.getBytes());
       
 
        return productRepository.save(product);
       
 
    }

    @Override
 
    public byte[] getProductImage(int id) {
 
        Products product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
 
        return product.getImages();
 
    }
    @Override
    
    public byte[] getProductImageByName(String name) {
 
        Products product = productRepository.findByName(name).orElseThrow(() -> new RuntimeException("Product not found"));
 
        return product.getImages();
 
    }

    @Override
 
    public Products addSubscription(int userId, int productId) {
 
        Products product = productRepository.findById(productId).orElseThrow(() -> new InvalidProductException("Product not found"));
 
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
 
        if (!product.getIsActive()) {
 
            throw new InvalidProductException("Product is not active");
 
        }
 
        Optional<ProductSubscription> existingSubscription = product.getProductSubscriptionList().stream()
 
            .filter(subscription -> subscription.getUser().getUserID() == userId)
 
            .findFirst();
 
        if (existingSubscription.isPresent()) {
 
             ProductSubscription subscription = existingSubscription.get();
 
        if (!subscription.isOptIn()) {
 
            // Reactivate the subscription
 
            subscription.setOptIn(true);
 
            subscription.setUpdatedOn(LocalDateTime.now());
 
            productRepository.save(product);
 
            return product;
 
        } else {
 
            throw new InvalidProductException("Already subscribed to this product");
 
        }
 
        }
 
        ProductSubscription subscription = new ProductSubscription();
 
        subscription.setUser(user);
 
        subscription.setProducts(product);
 
        product.getProductSubscriptionList().add(subscription);
 
        return productRepository.save(product);
 
    }
 
    @Override
 
    public Products removeSubscription(int userId, int productId) {
 
        Products product = productRepository.findById(productId).orElseThrow(() -> new InvalidProductException("Product not found"));
 
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
 
        if (!product.getIsActive()) {
 
            throw new InvalidProductException("Product is not active");
 
        }
 
        Optional<ProductSubscription> existingSubscription = product.getProductSubscriptionList().stream()
 
            .filter(subscription -> subscription.getUser().getUserID() == userId)
 
            .findFirst();
 
        if (!existingSubscription.isPresent() || !existingSubscription.get().isOptIn()) {
 
            throw new InvalidProductException("User has not subscribed to this product");
 
        }
 
        // Set optIn to false instead of removing the subscription
 
        existingSubscription.get().setOptIn(false);
 
        existingSubscription.get().setUpdatedOn(LocalDateTime.now());
 
        productRepository.save(product);
 
        return product;
 
}
 
    @Override
 
    public List<ProductSubscription> getSubscriptionList(int productId) {
 
        Products product = productRepository.findById(productId)
 
        .orElseThrow(() -> new InvalidProductException("Product not found"));
 
    return product.getProductSubscriptionList().stream()
 
        .filter(ProductSubscription::isOptIn)
 
        .collect(Collectors.toList());
 
    }
 
    @Override
 
    public List<ProductViewDTO> getProductSubscriptionList(int userId){
 
        
    	return productViewRepo.getSubscribedListByUser(userId);
        
//        return user.getProductSubscriptionList().stream()
// 
//                   .filter(subscription -> subscription.isOptIn())
// 
//                   .map(subscription -> new ProductViewDTO())
// 
//                   .collect(Collectors.toList());
 
        	
    }
 
    @Override
 
    public List<ProductViewDTO> findTopSubscribedProduct() {
 
//    	List<ProductRatingSubscriptionDTO> productSubscriptionList = new ArrayList<>();

//

//    	List<Integer> productIDs = productRepository.findTopSubscribedProduct();

//

//    	for(Integer productId : productIDs) {

//

//    		Products product = viewProductById(productId);

// 

//    		int optInTrueCount = (int) product.getProductSubscriptionList()

//

//    				.stream()

//

//    				.filter(ProductSubscription :: isOptIn)

//

//    				.count();

//

//    		productSubscriptionList.add(new ProductRatingSubscriptionDTO(ProductMapper.toDTO(product),0.0,optInTrueCount));
 
//    	}  	
 
    	return productViewRepo.findTopSubscribedProduct();
 
    }
 
    @Override
 
    public List<ProductViewDTO> findTopRatedProducts() {
 
//        List<Object[]> results = reviewAndRatingRepository.findTopRatedProducts();

//

//        List<ProductRatingSubscriptionDTO> topRatedProducts = new ArrayList<>();

// 

//        for (Object[] result : results) {

//

//            Integer productId = (Integer) result[0];

//

//            Double topRating = (Double) result[1];

//

//            Products product = productRepository.findById(productId).orElse(null);

//

//            if (product != null) {

//

//                topRatedProducts.add(new ProductRatingSubscriptionDTO(ProductMapper.toDTO(product), topRating,0));

//

//            }

//

//        }

// 

        return productViewRepo.findTopRatedProducts();
 
    }
 
    }

 