package com.nineteen.omp.product.controller;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.product.controller.dto.ProductRequestDto;
import com.nineteen.omp.product.controller.dto.ProductResponseDto;
import com.nineteen.omp.product.domain.Product;
import com.nineteen.omp.product.service.ProductService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping()
  public ResponseEntity<ResponseDto<?>> addProduct(@RequestBody ProductRequestDto requestDto) {
    StoreProduct storeProduct = productService.addProduct(requestDto);
    return ResponseEntity.ok().body(ResponseDto.success(storeProduct));
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ResponseDto<ProductResponseDto>> getProduct(@PathVariable UUID productId) {
    ProductResponseDto productResponseDto = productService.getProductById(productId);
    return ResponseEntity.ok().body(ResponseDto.success(productResponseDto));
  }

}