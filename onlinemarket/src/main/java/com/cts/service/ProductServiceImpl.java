package com.cts.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cts.dto.ProductDTO;
import com.cts.entity.Products;
import com.cts.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductRepository productRepository;
	
	@Override
	public List<ProductDTO> viewAllProducts() {
		List<Products> products =  productRepository.findAll();
		List<ProductDTO> productDTOs = new ArrayList<>();
	    
	    for (Products product : products) {
	        productDTOs.add(new ProductDTO(product));
	    }
	    return productDTOs;
	}

	@Override
	public Products viewProductById(int id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id).get();
	}

	@Override
	public void removeProduct(int id) {
		// TODO Auto-generated method stub
		productRepository.deleteById(id);
	}

	@Override
	public Products addProduct(String name, String description, MultipartFile imageFile) throws IOException {
		// TODO Auto-generated method stub
		Products product = new Products();
		product.setName(name);
		product.setDescription(description);
		product.setImages(imageFile.getBytes());
		return productRepository.save(product);
	}

	@Override
	public byte[] getProductImage(int id) {
		// TODO Auto-generated method stub
		Products product = productRepository.findById(id).orElseThrow(()->
									new RuntimeException("Product not Found"));
		return product.getImages();
	}


}
