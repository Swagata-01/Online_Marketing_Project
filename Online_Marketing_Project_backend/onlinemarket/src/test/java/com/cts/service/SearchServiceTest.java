package com.cts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.dto.ProductDTO;
import com.cts.entity.Products;
import com.cts.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {
	@Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SearchServiceImpl searchService;

    private Products product1;
    private Products product2;
    private Products product3;
    
    @BeforeEach
    void setUp() {
        product1 = new Products();
        product1.setName("Nestle");
        
        product2 = new Products();
        product2.setName("Nestle 2.O");
        
        product3 = new Products();
        product3.setName("DiaryMilk");
    }

    @Test
    void testSearchProductByName() {
        when(productRepository.findByNameContainingIgnoreCase("est"))
                .thenReturn(Arrays.asList(product1, product2));

        List<ProductDTO> result = searchService.searchProductByName("est");

//        assertEquals(2, result.size());
        assertEquals("Nestle", result.get(0).getName());
        assertEquals("Nestle 2.O", result.get(1).getName());
    }
}
