package com.nineteen.omp.product.service;

import com.nineteen.omp.product.controller.dto.ProductRequestDto;
import com.nineteen.omp.product.domain.Product;


public interface ProductService {

  Product addProduct(ProductRequestDto requestDto);

}