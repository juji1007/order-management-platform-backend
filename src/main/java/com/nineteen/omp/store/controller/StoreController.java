package com.nineteen.omp.store.controller;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.global.utils.PageableUtils;
import com.nineteen.omp.store.controller.dto.StoreRequestDto;
import com.nineteen.omp.store.controller.dto.StoreResponseDto;
import com.nineteen.omp.store.service.StoreService;
import com.nineteen.omp.store.service.dto.StoreCommand;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PostMapping
  public ResponseEntity<ResponseDto<StoreResponseDto>> createStore(
      @Valid @RequestBody StoreRequestDto storeRequestDto) {

    Long userId = 123L;

    StoreResponseDto storeResponseDto = storeService.createStore(
        new StoreCommand(userId, storeRequestDto));

    return ResponseEntity.ok(ResponseDto.success(storeResponseDto));
  }

  //검색 -> @ModelAttribute말고 전부 RequestParam 처리?, storeSearchDto(요청dto)예외처리 안함(null 일 때 전체검색)
  //검색 -> 이름, 카테고리, 주소 -> 정렬조건은 다만들기
  @GetMapping
  public ResponseEntity<ResponseDto<Page<StoreResponseDto>>> searchStore(
      @RequestParam(
          name = "name",
          defaultValue = "",
          required = false
      ) String name,
      @RequestParam(
          name = "categoryName",
          defaultValue = "",
          required = false
      ) String categoryName,
      @RequestParam(
          name = "address",
          defaultValue = "",
          required = false
      ) String address,
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable) {
    Pageable validatedPageable = PageableUtils.validatePageable(pageable);

    Page<StoreResponseDto> searchedStores = storeService.searchStores(
        name,
        categoryName,
        address,
        validatedPageable);

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
      @Valid @RequestBody StoreRequestDto storeRequestDto
  ) {

    Long userId = 123L;

    StoreResponseDto storeResponseDto = storeService.updateStore(storeId,
        new StoreCommand(userId, storeRequestDto));
    return ResponseEntity.ok(ResponseDto.success(storeResponseDto));
  }

  @DeleteMapping("/{storeId}")
  public ResponseEntity<?> deleteStore(@PathVariable UUID storeId) {
    storeService.deleteStore(storeId);
    return ResponseEntity.ok(ResponseDto.success());
  }

}


