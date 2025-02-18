package com.nineteen.omp.store.controller;

import com.nineteen.omp.category.domain.StoreCategory;
import com.nineteen.omp.global.exception.CommonExceptionCode;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.store.controller.dto.StoreRequestDto;
import com.nineteen.omp.store.service.StoreService;
import com.nineteen.omp.store.service.dto.StoreResponseDto;
import com.nineteen.omp.store.service.dto.StoreServiceRequestDto;
import com.nineteen.omp.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;
  private final UserService userService;
  private final CategoryService categoryService;

  @PostMapping
  public ResponseEntity<StoreResponseDto> createStore(
      @RequestBody StoreRequestDto storeRequestDto) {
    try {
      //user -> 스프링 시큐리티 이용
      String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal()
          .toString();
      User user = userService.findById(userId);

      //category -> 카테고리 서비스 이용, 카테고리(db정보 이미 있음) 선택 후 create 가정
      StoreCategory category = categoryService.findById(storeRequestDto.categoryId());

      //StoreRequestDto -> StoreServiceRequestDto 변환(유저 아이디, 카테고리 아이디 추가)
      StoreServiceRequestDto storeServiceRequestDto = toStoreServiceRequestDto(storeRequestDto,
          user,
          category);

      StoreResponseDto storeResponseDto = storeService.createStore(storeServiceRequestDto);

      return ResponseEntity.ok().body(storeResponseDto);
    } catch (Exception e) {
      throw new CustomException(CommonExceptionCode.INTERNAL_SERVER_ERROR);
    }
  }

  private StoreServiceRequestDto toStoreServiceRequestDto(StoreRequestDto storeRequestDto,
      User user, StoreCategory category) {
    return new StoreServiceRequestDto(
        user,
        category,
        storeRequestDto.name(),
        storeRequestDto.address(),
        storeRequestDto.phone(),
        storeRequestDto.openHours(),
        storeRequestDto.closeHours(),
        storeRequestDto.closedDays()
    );
  }
}
