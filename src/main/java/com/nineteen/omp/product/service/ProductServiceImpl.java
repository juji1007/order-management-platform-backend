package com.nineteen.omp.product.service;

import com.nineteen.omp.global.exception.CommonExceptionCode;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.product.controller.dto.ProductRequestDto;
import com.nineteen.omp.product.controller.dto.ProductResponseDto;
import com.nineteen.omp.product.domain.StoreProduct;
import com.nineteen.omp.product.exception.ProductExceptionCode;
import com.nineteen.omp.product.repository.ProductRepository;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.repository.StoreRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final StoreRepository storeRepository;

  @Override
  public StoreProduct addProduct(ProductRequestDto requestDto) {
    // TODO: 실제 store의 UUID 정보를 동적으로 조회하도록 변경 필요
    Store store = getStoreById(UUID.fromString("00000000-0000-0000-0000-000000000001"));

    StoreProduct storeProduct = createProduct(requestDto, store);
    return saveProduct(storeProduct);
  }

  private StoreProduct createProduct(ProductRequestDto requestDto, Store store) {
    return StoreProduct.builder()
        .store(store)
        .name(requestDto.name())
        .price(requestDto.price())
        .image(requestDto.image())
        .description(requestDto.description())
        .build();
  }

  private StoreProduct saveProduct(StoreProduct storeProduct) {
    try {
      return productRepository.save(storeProduct);
    } catch (DataIntegrityViolationException e) {
      throw new CustomException(ProductExceptionCode.PRODUCT_SAVE_FAILED);
    } catch (Exception e) {
      throw new CustomException(CommonExceptionCode.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ProductResponseDto getProduct(UUID productId) {
    StoreProduct storeProduct = getProductById(productId);
    return new ProductResponseDto(storeProduct);
  }

  @Override
  public ProductResponseDto updateProduct(ProductRequestDto requestDto, UUID productId) {

    StoreProduct storeProduct = getProductById(productId);
    UUID storeId = storeProduct.getStore().getId();
    Store store = getStoreById(storeId);

    // 필드값 수정
    StoreProduct updatedStoreProduct = updateProductFromDto(requestDto, storeProduct, store);

    // DB 저장
    productRepository.save(updatedStoreProduct);

    // 수정된 데이터를 ResponseDto로 반환
    return new ProductResponseDto(updatedStoreProduct);
  }

  private StoreProduct getProductById(UUID productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ProductExceptionCode.PRODUCT_NOT_FOUND));
  }

  private Store getStoreById(UUID storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(ProductExceptionCode.STORE_NOT_FOUND));
  }

  private StoreProduct updateProductFromDto(ProductRequestDto requestDto, StoreProduct storeProduct,
      Store store) {
    return storeProduct.toBuilder()
        .store(store)
        .name(requestDto.name())
        .price(requestDto.price())
        .image(requestDto.image())
        .description(requestDto.description())
        .build();
  }

  @Override
  public void deleteProduct(UUID productId) {
    StoreProduct storeProduct = getProductById(productId);
    productRepository.delete(storeProduct);
  }

  @Override
  public ProductResponseDto softDeleteProduct(UUID productId) {
    StoreProduct storeProduct = getProductById(productId);
    storeProduct.softDelete();
    productRepository.save(storeProduct);
    return new ProductResponseDto(storeProduct);
  }

}
