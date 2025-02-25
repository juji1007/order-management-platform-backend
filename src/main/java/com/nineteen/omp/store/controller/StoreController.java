package com.nineteen.omp.store.controller;

import com.nineteen.omp.auth.dto.UserDetailsImpl;
import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.global.utils.PageableUtils;
import com.nineteen.omp.store.controller.dto.SearchStoreResponseDto;
import com.nineteen.omp.store.controller.dto.StoreRequestDto;
import com.nineteen.omp.store.controller.dto.StoreResponseDto;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.repository.StoreRepository;
import com.nineteen.omp.store.service.StoreService;
import com.nineteen.omp.store.service.dto.StoreCommand;
import com.nineteen.omp.user.service.UserService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;
  private final UserService userService;
  private final StoreRepository storeRepository;

  @PreAuthorize("hasAnyRole('USER','MASTER','OWNER')")
  @PostMapping
  public ResponseEntity<ResponseDto<StoreResponseDto>> createStore(
      @Valid @RequestBody StoreRequestDto storeRequestDto,
      @AuthenticationPrincipal UserDetails userDetails
  ) {

    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
    Long userId = userDetailsImpl.getUserId();

    StoreResponseDto storeResponseDto = storeService.createStore(
        new StoreCommand(userId, storeRequestDto));

    return ResponseEntity.ok(ResponseDto.success(storeResponseDto));
  }


  //심화 검색
  @GetMapping("/search")
  public ResponseEntity<ResponseDto<Page<SearchStoreResponseDto>>> searchAdvancedStore(
      @RequestParam(
          name = "productName",
          defaultValue = "",
          required = false
      ) String productName,
      @RequestParam(
          name = "storeName",
          defaultValue = "",
          required = false
      ) String storeName,
      @RequestParam(
          name = "categoryName",
          defaultValue = "",
          required = false
      ) String categoryName,
      @RequestParam(
          name = "averageRating",
          defaultValue = "0",
          required = false
      ) int averageRating,
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable) {
    Pageable validatedPageable = PageableUtils.validatePageable(pageable);

    Page<SearchStoreResponseDto> searchedAdvanced = storeService.searchAdvacnedStore(
        productName,
        storeName,
        categoryName,
        averageRating,
        validatedPageable
    );

    return ResponseEntity.ok(ResponseDto.success(searchedAdvanced));
  }


  @PreAuthorize("hasAnyRole('MASTER')")
  @PostMapping("/approve/{storeId}")
  public ResponseEntity<ResponseDto<?>> approveStore(
      @PathVariable UUID storeId
  ) {
    log.info("approveStore StoreId : {}", storeId);
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다."));

    // 가게 status -> open으로 변경
    storeService.approveStore(storeId);
    // 유저 role -> 변경
    userService.updateUserRole(store.getUser().getId());

    return ResponseEntity.ok(ResponseDto.success());
  }

  //검색 -> 이름, 카테고리, 주소 -> 정렬조건은 다만들
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

  @PreAuthorize("hasAnyRole('MASTER','OWNER')")
  @PatchMapping("/{storeId}")
  public ResponseEntity<ResponseDto<StoreResponseDto>> updateStore(
      @PathVariable UUID storeId,
      @Valid @RequestBody StoreRequestDto storeRequestDto,
      @AuthenticationPrincipal UserDetails userDetails) {

    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
    Long userId = userDetailsImpl.getUserId();

    StoreResponseDto storeResponseDto = storeService.updateStore(storeId,
        new StoreCommand(userId, storeRequestDto));
    return ResponseEntity.ok(ResponseDto.success(storeResponseDto));
  }

  @PreAuthorize("hasAnyRole('MASTER','OWNER')")
  @DeleteMapping("/{storeId}")
  public ResponseEntity<?> deleteStore(@PathVariable UUID storeId) {
    storeService.deleteStore(storeId);
    return ResponseEntity.ok(ResponseDto.success());
  }

}


