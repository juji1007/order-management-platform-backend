package com.nineteen.omp.product.controller;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.global.utils.PageableUtils;
import com.nineteen.omp.product.controller.dto.ProductRequestDto;
import com.nineteen.omp.product.controller.dto.ProductResponseDto;
import com.nineteen.omp.product.service.ProductService;
import com.nineteen.omp.product.service.dto.ProductCommand;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PreAuthorize("hasRole('OWNER')")
  @PostMapping()
  public ResponseEntity<ResponseDto<?>> addProduct(
      @RequestBody @Valid ProductRequestDto requestDto) {
    ProductCommand productCommand = ProductCommand.fromProductRequestDto(requestDto);
    ProductResponseDto productResponseDto = productService.addProduct(productCommand);
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.success(productResponseDto));
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ResponseDto<ProductResponseDto>> getProduct(@PathVariable UUID productId) {
    ProductResponseDto productResponseDto = productService.getProduct(productId);
    return ResponseEntity.ok().body(ResponseDto.success(productResponseDto));
  }


  @GetMapping()
  public ResponseEntity<ResponseDto<?>> getAllProducts(
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable) {

    pageable = PageableUtils.validatePageable(pageable);  // 유효성 검사
    Page<ProductResponseDto> productPage = productService.getAllProducts(pageable);

    return ResponseEntity.ok().body(ResponseDto.success(productPage.getContent()));
  }

  // TODO : @AuthenticationPrinciapal 로 UserDetails 에서 productId를 가져와야 함.
  @PreAuthorize("hasAnyRole('MASTER','OWNER')")
  @PatchMapping("/{productId}")
  public ResponseEntity<ResponseDto<?>> updateProduct(
      @RequestBody @Valid ProductRequestDto requestDto,
      @PathVariable UUID productId
  ) {
    ProductCommand command = ProductCommand.fromProductRequestDto(requestDto);
    ProductResponseDto updateProduct = productService.updateProduct(command, productId);
    return ResponseEntity.ok().body(ResponseDto.success(updateProduct));
  }

  @PreAuthorize("hasAnyRole('MASTER','OWNER')")
  @DeleteMapping("/{productId}")
  public ResponseEntity<ResponseDto<?>> deleteProduct(@PathVariable UUID productId) {
    productService.deleteProduct(productId);
    return ResponseEntity.ok().body(ResponseDto.success());
  }

  @GetMapping("/search")
  public ResponseEntity<ResponseDto<?>> searchProducts(
      @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
      @RequestParam(name = "category", required = false, defaultValue = "") String category,
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable) {

    pageable = PageableUtils.validatePageable(pageable);
    Page<ProductResponseDto> productPage = productService.searchProducts(keyword, category,
        pageable);

    return ResponseEntity.ok().body(ResponseDto.success(productPage.getContent()));
  }

}