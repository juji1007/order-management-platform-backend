package com.nineteen.omp.product.service;

import com.nineteen.omp.global.exception.CommonExceptionCode;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.product.controller.dto.ProductRequestDto;
import com.nineteen.omp.product.domain.Product;
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
  public Product addProduct(ProductRequestDto requestDto) {
    // TODO: 실제 store의 UUID 정보를 동적으로 조회하도록 변경 필요
    Store store = getStoreById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    Product product = createProduct(requestDto, store);
    return saveProduct(product);
  }

  private Store getStoreById(UUID storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(ProductExceptionCode.STORE_NOT_FOUND));
  }

  private Product createProduct(ProductRequestDto requestDto, Store store) {
    return Product.builder()
        .store(store)
        .name(requestDto.name())
        .price(requestDto.price())
        .image(requestDto.image())
        .description(requestDto.description())
        .build();
  }

  private Product saveProduct(Product product) {
    try {
      return productRepository.save(product);
    } catch (DataIntegrityViolationException e) {
      throw new CustomException(ProductExceptionCode.PRODUCT_SAVE_FAILED);
    } catch (Exception e) {
      throw new CustomException(CommonExceptionCode.INTERNAL_SERVER_ERROR);
    }
  }


}
