package com.cts.controller;

import com.cts.dto.MultipleProductDTO;
import com.cts.entity.MultipleProduct;
import com.cts.service.MultipleProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/OMP")
public class MultipleProductController {
    @Autowired
    private MultipleProductService multipleProductService;

    @PostMapping("/admin/uploadMultipleRecords")
    public ResponseEntity<List<MultipleProduct>> uploadProducts(@RequestParam("file") MultipartFile file) throws IOException {
        List<MultipleProductDTO> productDTOs = multipleProductService.readProductsFromXlsx(file);
        List<MultipleProduct> products = multipleProductService.saveProducts(productDTOs);
        return new ResponseEntity<>(products, HttpStatus.CREATED);
    }
}
