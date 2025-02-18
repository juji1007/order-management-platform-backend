package com.nineteen.omp.store.controller;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.global.exception.CommonExceptionCode;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.store.controller.dto.StoreRequestDto;
import com.nineteen.omp.store.repository.dto.StoreSearchDto;
import com.nineteen.omp.store.service.StoreService;
import com.nineteen.omp.store.service.dto.StoreResponseDto;
import com.nineteen.omp.store.service.dto.StoreServiceRequestDto;
import com.nineteen.omp.user.domain.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  //검색 -> @ModelAttribute말고 전부 RequestParam 처리?
  @GetMapping
  public ResponseEntity<ResponseDto<Page<StoreResponseDto>>> searchStore(
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "name") String sortBy,
      @RequestParam(defaultValue = "true") boolean isAsc,
      @ModelAttribute StoreSearchDto storeSearchDto
  ) {
    if (page < 1 || size < 1) {
      throw new CustomException(CommonExceptionCode.INVALID_PARAMETER);
    }

    Sort sort = isAsc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page - 1, size, sort);
    Page<StoreResponseDto> searchedStores = storeService.searchStores(keyword, storeSearchDto,
        pageable);

    return ResponseEntity.ok(ResponseDto.success(searchedStores));
  }

  @GetMapping("/{storeId}")
  public ResponseEntity<ResponseDto<StoreResponseDto>> getStore(@PathVariable UUID storeId) {
    StoreResponseDto storeResponseDto = storeService.getStore(storeId);
    return ResponseEntity.ok(ResponseDto.success(storeResponseDto));
  }

  @PatchMapping("/{storeId}")
  public ResponseEntity<ResponseDto<StoreResponseDto>> updateStore(
      @PathVariable UUID storeId,
      @RequestBody StoreRequestDto storeRequestDto
  ) {
    //user -> 스프링 시큐리티 이용
    String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        .toString();
    User user = userService.findById(userId);

    StoreServiceRequestDto storeServiceRequestDto = toStoreServiceRequestDto(storeRequestDto, user);
    StoreResponseDto storeResponseDto = storeService.updateStore(storeId, storeServiceRequestDto);
    return ResponseEntity.ok(ResponseDto.success(storeResponseDto));
  }

  @PatchMapping("/{storeId}/soft-delete")
  public ResponseEntity<?> softDeleteStore(@PathVariable UUID storeId) {
    storeService.softDeleteStore(storeId);
    return ResponseEntity.ok(ResponseDto.success());
  }

  @DeleteMapping("/{storeId}")
  public ResponseEntity<?> deleteStore(@PathVariable UUID storeId) {
    storeService.hardDeleteStore(storeId);
    return ResponseEntity.ok(ResponseDto.success());
  }

  private StoreServiceRequestDto toStoreServiceRequestDto(StoreRequestDto storeRequestDto,
      User user) {
    return new StoreServiceRequestDto(
        user,
        storeRequestDto.categoryCode(),
        storeRequestDto.name(),
        storeRequestDto.address(),
        storeRequestDto.phone(),
        storeRequestDto.openHours(),
        storeRequestDto.closeHours(),
        storeRequestDto.closedDays()
    );
  }
}


