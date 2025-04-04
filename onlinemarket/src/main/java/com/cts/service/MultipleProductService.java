package com.cts.service;

import com.cts.dto.MultipleProductDTO;
import com.cts.entity.MultipleProduct;
import com.cts.repository.MultipleProductRepository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MultipleProductService {
    @Autowired
    private MultipleProductRepository multipleProductRepository;

    public List<MultipleProduct> saveProducts(List<MultipleProductDTO> productDTOs) {
        List<MultipleProduct> products = productDTOs.stream().map(dto -> {
            MultipleProduct product = new MultipleProduct();
            product.setName(dto.getName());
            product.setDescription(dto.getDescription());
            product.setImageUrl(dto.getImageUrl());
            return product;
        }).collect(Collectors.toList());
        return multipleProductRepository.saveAll(products);
    }

    public List<MultipleProductDTO> readProductsFromXlsx(MultipartFile file) throws IOException {
        List<MultipleProductDTO> productDTOs = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                MultipleProductDTO productDTO = new MultipleProductDTO();
                productDTO.setName(row.getCell(0).getStringCellValue());
                productDTO.setDescription(row.getCell(1).getStringCellValue());
                productDTO.setImageUrl(row.getCell(2).getStringCellValue());
                productDTOs.add(productDTO);
            }
        }
        return productDTOs;
    }
}
