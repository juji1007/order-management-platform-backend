package com.nineteen.omp.store.service;

import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.domain.StoreCategory;
import com.nineteen.omp.store.exception.StoreException;
import com.nineteen.omp.store.exception.StoreExceptionCode;
import com.nineteen.omp.store.repository.StoreRepository;
import com.nineteen.omp.store.service.dto.StoreResponseDto;
import com.nineteen.omp.store.service.dto.StoreServiceRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;

  //Create, 가게 중복 체크, 오픈시간 > 닫는시간 인 경우 -> 서버에서 처리/ 프론트에서 처리?
  @Transactional
  public StoreResponseDto createStore(StoreServiceRequestDto requestDto) {
    boolean isStoreExsits = existsByNameAndAddress(requestDto.name(), requestDto.address());
    if (isStoreExsits) {
      throw new StoreException(StoreExceptionCode.STORE_IS_DUPLICATED);
    }
    //enum에서 값 찾기
    StoreCategory category = StoreCategory.selectName(requestDto.name());

    Store store = Store.builder()
        .user(requestDto.user())
        .storeCategory(category)
        .name(requestDto.name())
        .address(requestDto.address())
        .phone(requestDto.phone())
        .openHours(requestDto.openHours())
        .closeHours(requestDto.closeHours())
        .closedDays(requestDto.closedDays())
        .build();
    Store savedStore = storeRepository.save(store);
    return toResponseDto(savedStore);
  }

  public boolean existsByNameAndAddress(String name, String address) {
    return storeRepository.existsByNameAndAddress(name, address);
  }

  //toResponse
  private StoreResponseDto toResponseDto(Store store) {
    return new StoreResponseDto(
        store.getId(),
        store.getName(),
        store.getAddress(),
        store.getPhone(),
        store.getOpenHours(),
        store.getCloseHours(),
        store.getClosedDays()
    );
  }
}
