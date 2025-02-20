package com.nineteen.omp.product.controller;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.product.controller.dto.ProductRequestDto;
import com.nineteen.omp.product.controller.dto.ProductResponseDto;
import com.nineteen.omp.product.domain.StoreProduct;
import com.nineteen.omp.product.service.ProductService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    ProductCommand productCommand = ProductCommand.fromProductRequestDto(requestDto);
    ProductResponseDto productResponseDto = productService.addProduct(productCommand);
    return ResponseEntity.ok().body(
        (ResponseDto<?>) ResponseDto.success(productResponseDto));
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ResponseDto<ProductResponseDto>> getProduct(@PathVariable UUID productId) {
    ProductResponseDto productResponseDto = productService.getProduct(productId);
    return ResponseEntity.ok().body(ResponseDto.success(productResponseDto));
  }

  // TODO : @AuthenticationPrinciapal 로 UserDetails 에서 productId를 가져와야 함.
  @PatchMapping("/{productId}")
  public ResponseEntity<ResponseDto<?>> updateProduct(
      @RequestBody ProductRequestDto requestDto,
      @PathVariable UUID productId
  ) {
    ProductCommand command = ProductCommand.fromProductRequestDto(requestDto);
    ProductResponseDto updateProduct = productService.updateProduct(command, productId);
    return ResponseEntity.ok().body(ResponseDto.success(updateProduct));
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<ResponseDto<?>> deleteProduct(@PathVariable UUID productId) {
    productService.deleteProduct(productId);
    return ResponseEntity.ok().body(ResponseDto.success());
  }
}