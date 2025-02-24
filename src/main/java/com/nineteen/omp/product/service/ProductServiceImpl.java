package com.nineteen.omp.product.service;


import com.nineteen.omp.ai.service.AiService;
import com.nineteen.omp.product.controller.dto.ProductResponseDto;
import com.nineteen.omp.product.domain.StoreProduct;
import com.nineteen.omp.product.exception.ProductException;
import com.nineteen.omp.product.exception.ProductExceptionCode;
import com.nineteen.omp.product.repository.ProductRepository;
import com.nineteen.omp.product.service.dto.ProductCommand;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.repository.StoreRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final StoreRepository storeRepository;
  private final AiService aiService;

  @Override
  @Transactional
  public ProductResponseDto addProduct(ProductCommand command) {

    Store store = findStoreById(command.storeId());
    String description = getProductDescription(command);

    StoreProduct storeProduct = createProduct(command, store, description);

    storeProduct = saveProduct(storeProduct);
    return new ProductResponseDto(storeProduct);
  }

  private String getProductDescription(ProductCommand command) {
    String description = command.description();
    if (description == null || description.isEmpty()) {
      description = aiService.getAiResponse(command.name());
    }
    return description;
  }

  private StoreProduct createProduct(ProductCommand command, Store store, String description) {
    return StoreProduct.builder()
        .store(store)
        .name(command.name())
        .price(command.price())
        .image(command.image())
        .description(description)
        .build();
  }

  private StoreProduct saveProduct(StoreProduct storeProduct) {
    try {
      return productRepository.save(storeProduct);
    } catch (Exception e) {
      throw new ProductException(ProductExceptionCode.PRODUCT_SAVE_FAILED);
    }
  }

  @Override
  public ProductResponseDto getProduct(UUID productId) {
    StoreProduct storeProduct = findProductById(productId)
        .orElseThrow(() -> new ProductException(ProductExceptionCode.PRODUCT_NOT_FOUND));
    return new ProductResponseDto(storeProduct);
  }

  @Override
  @Transactional
  public ProductResponseDto updateProduct(ProductCommand command, UUID productId) {

    StoreProduct storeProduct = findProductById(productId)
        .orElseThrow(() -> new ProductException(ProductExceptionCode.PRODUCT_NOT_FOUND));

    StoreProduct updatedStoreProduct = storeProduct.toBuilder()
        .name(command.name())
        .price(command.price())
        .image(command.image())
        .description(command.description())
        .build();

    return new ProductResponseDto(updatedStoreProduct);
  }

  public Optional<StoreProduct> findProductById(UUID productId) {
    return productRepository.findById(productId);
  }

  private Store findStoreById(UUID storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new ProductException(ProductExceptionCode.STORE_NOT_FOUND));
  }

  @Override
  public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
    return productRepository.findAll(pageable)
        .map(ProductResponseDto::new);
  }

  @Override
  @Transactional
  public void deleteProduct(UUID productId) {
    if (productRepository.existsById(productId)) {
      productRepository.deleteById(productId);
    } else {
      throw new ProductException(ProductExceptionCode.PRODUCT_ALREADY_DELETED);
    }
  }

  @Override
  public Page<ProductResponseDto> searchProducts(String keyword, String category,
      Pageable pageable) {
    return productRepository.searchProducts(keyword, category, pageable);
  }

}
