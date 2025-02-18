package com.nineteen.omp.store.controller;

import com.nineteen.omp.global.dto.ResponseDto;
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

  @PostMapping
  public ResponseEntity<ResponseDto<StoreResponseDto>> createStore(
      @RequestBody StoreRequestDto storeRequestDto) {
    String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        .toString();
    User user = userService.findById(userId);

    StoreServiceRequestDto storeServiceRequestDto = toStoreServiceRequestDto(storeRequestDto, user);
    StoreResponseDto storeResponseDto = storeService.createStore(storeServiceRequestDto);

    return ResponseEntity.ok(ResponseDto.success(storeResponseDto));
  }

  private StoreServiceRequestDto toStoreServiceRequestDto(StoreRequestDto storeRequestDto,
      User user) {
    return new StoreServiceRequestDto(
        user,
        storeRequestDto.category(),
        storeRequestDto.name(),
        storeRequestDto.address(),
        storeRequestDto.phone(),
        storeRequestDto.openHours(),
        storeRequestDto.closeHours(),
        storeRequestDto.closedDays()
    );
  }
}
