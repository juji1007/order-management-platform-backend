package com.nineteen.omp.store.service;

import static com.nineteen.omp.store.controller.dto.StoreResponseDto.toResponseDto;

import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.store.controller.dto.SearchStoreResponseDto;
import com.nineteen.omp.store.controller.dto.StoreResponseDto;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.domain.StoreCategory;
import com.nineteen.omp.store.exception.StoreException;
import com.nineteen.omp.store.exception.StoreExceptionCode;
import com.nineteen.omp.store.repository.StoreRepository;
import com.nineteen.omp.store.repository.dto.StoreData;
import com.nineteen.omp.store.service.dto.StoreCommand;
import com.nineteen.omp.user.domain.User;
import com.nineteen.omp.user.exception.UserExceptionCode;
import com.nineteen.omp.user.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

  private final StoreRepository storeRepository;
  private final UserRepository userRepository;

  //Create, 가게 중복 체크
  @Override
  @Transactional
  public StoreResponseDto createStore(StoreCommand storeCommand) {

    User user = userRepository.findById(storeCommand.userId())
        .orElseThrow(() -> new CustomException(UserExceptionCode.USER_NOT_FOUND));

    boolean isStoreExsits = storeRepository.existsByNameAndAddress(
        storeCommand.storeRequestDto().name(),
        storeCommand.storeRequestDto().address());
    if (isStoreExsits) {
      throw new StoreException(StoreExceptionCode.STORE_IS_DUPLICATED);
    }
    //enum에서 값 찾기
    StoreCategory storeCategory = StoreCategory.fromName(
        storeCommand.storeRequestDto().storeCategoryName());

    StoreData storeData = new StoreData(storeCommand, storeCategory, user);

    Store savedStore = storeRepository.save(storeData.toEntity());
    return toResponseDto(savedStore);
  }

  //심화 검색
  @Override
  public Page<SearchStoreResponseDto> searchAdvacnedStore(String productName, String storeName,
      String categoryName, int averageRating, Pageable pageable) {

    //검증
    if (categoryName != null && !categoryName.isEmpty()) {
      StoreCategory.fromName(categoryName);
    }

    return storeRepository.searchAdvancedStore(productName, storeName, categoryName, averageRating,
        pageable);
  }

  //Search
  @Override
  public Page<StoreResponseDto> searchStores(
      String name,
      String categoryName,
      String address,
      Pageable pageable) {

    //검증
    if (categoryName != null && !categoryName.isEmpty()) {
      StoreCategory.fromName(categoryName);
    }

    return storeRepository.searchStores(name, categoryName, address, pageable);
  }

  //Read
  @Override
  public StoreResponseDto getStore(UUID storeId) {
    return toResponseDto(findByIdOrElseThrow(storeId));
  }

  //update
  @Override
  @Transactional
  public StoreResponseDto updateStore(UUID storeId, StoreCommand storeCommand) {
    //아이디 존재 확인
    Store store = findByIdOrElseThrow(storeId);

    if (storeCommand.storeRequestDto().storeCategoryName() != null) {
      store.changeStoreCategory(storeCommand.storeRequestDto().storeCategoryName());
    }
    if (storeCommand.storeRequestDto().name() != null) {
      store.changeStoreName(storeCommand.storeRequestDto().name());
    }
    if (storeCommand.storeRequestDto().address() != null) {
      store.changeStoreAddress(storeCommand.storeRequestDto().address());
    }
    if (storeCommand.storeRequestDto().openHours() != null) {
      store.changeStoreOpenHours(storeCommand.storeRequestDto().openHours());
    }
    if (storeCommand.storeRequestDto().closeHours() != null) {
      store.changeStoreCloseHours(storeCommand.storeRequestDto().closeHours());
    }
    if (storeCommand.storeRequestDto().closedDays() != null) {
      store.changeStoreClosedDays(storeCommand.storeRequestDto().closedDays());
    }

    Store savedStore = storeRepository.save(store);
    return toResponseDto(savedStore);
  }

  @Override
  @Transactional
  public void deleteStore(UUID storeId) {
    storeRepository.deleteById(findByIdOrElseThrow(storeId).getId());
  }


  private Store findByIdOrElseThrow(UUID storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new StoreException(StoreExceptionCode.STORE_NOT_FOUND));
  }

}
