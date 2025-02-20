package com.nineteen.omp.product.service;

import com.nineteen.omp.global.exception.CommonExceptionCode;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.product.controller.dto.ProductResponseDto;
import com.nineteen.omp.product.domain.StoreProduct;
import com.nineteen.omp.product.exception.ProductException;
import com.nineteen.omp.product.exception.ProductExceptionCode;
import com.nineteen.omp.product.repository.ProductRepository;
import com.nineteen.omp.product.service.dto.ProductCommand;
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
  public ProductResponseDto addProduct(ProductCommand command) {
    Store store = getStoreById(command.storeId());
    StoreProduct storeProduct = createProduct(command, store);

    storeProduct = saveProduct(storeProduct);
    return new ProductResponseDto(storeProduct);
  }

  private StoreProduct createProduct(ProductCommand command, Store store) {
    return StoreProduct.builder()
        .store(store)
        .name(command.name())
        .price(command.price())
        .image(command.image())
        .description(command.description())
        .build();
  }

  private StoreProduct saveProduct(StoreProduct storeProduct) {
    try {
      return productRepository.save(storeProduct);
    } catch (DataIntegrityViolationException e) {
      throw new ProductException(ProductExceptionCode.PRODUCT_SAVE_FAILED);
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
  public ProductResponseDto updateProduct(ProductCommand command, UUID productId) {

    StoreProduct storeProduct = getProductById(productId);
    UUID storeId = storeProduct.getStore().getId();
    Store store = getStoreById(storeId);

    StoreProduct updatedStoreProduct = updateProductFromCommand(command, storeProduct, store);
    productRepository.save(updatedStoreProduct);
    return new ProductResponseDto(updatedStoreProduct);
  }

  private StoreProduct updateProductFromCommand(ProductCommand command, StoreProduct storeProduct,
      Store store) {
    return storeProduct.toBuilder()
        .store(store)
        .name(command.name())
        .price(command.price())
        .image(command.image())
        .description(command.description())
        .build();
  }

  public StoreProduct getProductById(UUID productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new ProductException(ProductExceptionCode.PRODUCT_NOT_FOUND));
  }

  private Store getStoreById(UUID storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new ProductException(ProductExceptionCode.STORE_NOT_FOUND));
  }


  @Override
  public void deleteProduct(UUID productId) {
    StoreProduct storeProduct = getProductById(productId);
    productRepository.delete(storeProduct);
  }

}
