package com.cts.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cts.dto.ProductDTO;
import com.cts.entity.ProductSubscription;
import com.cts.entity.Products;
import com.cts.entity.ReviewsAndRatings;
import com.cts.entity.User;
import com.cts.repository.ProductRepository;
import com.cts.repository.UserRepository;
import com.cts.exception.*;

@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;
    
    //view all the products
    public List<ProductDTO> viewAllProducts() {
        List<Products> products = productRepository.findAll();
        List<ProductDTO> productDTOs = new ArrayList<>();
        
        for (Products product : products) {
            productDTOs.add(new ProductDTO(product));
        }
        return productDTOs;
    }

    //view product by id
    @Override
    public Products viewProductById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public void removeProduct(int id) {
        productRepository.deleteById(id);
    }

    @Override
    public Products addProduct(String name, String description, MultipartFile imageFile) throws IOException {
        Products product = new Products();
        product.setName(name);
        product.setDescription(description);
        product.setImages(imageFile.getBytes());
        return productRepository.save(product);
    }
   
    
    @Override
    public Products updateProduct(String name, String description, MultipartFile productImage) throws  InvalidProductException, IOException {
        Products product = productRepository.findByName(name).orElseThrow(() -> new InvalidProductException("Product not found with Name: " + name));
        if (description != null)product.setDescription(description);
        if (productImage != null)product.setImages(productImage.getBytes());
 
        return productRepository.save(product);

 
    }

    @Override
    public byte[] getProductImage(int id) {
        Products product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
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
    public List<ProductDTO> getProductSubscriptionList(int userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getProductSubscriptionList().stream()
                   .filter(subscription -> subscription.isOptIn())
                   .map(subscription -> new ProductDTO(subscription.getProducts()))
                   .collect(Collectors.toList());
    }
   

}