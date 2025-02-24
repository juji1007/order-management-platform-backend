package com.nineteen.omp.store.controller;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.global.utils.PageableUtils;
import com.nineteen.omp.store.controller.dto.ServiceAreaRequestDto;
import com.nineteen.omp.store.controller.dto.ServiceAreaResponseDto;
import com.nineteen.omp.store.service.ServiceAreaService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/service-areas")
public class ServiceAreaController {

  private final ServiceAreaService serviceAreaService;

  @PreAuthorize("hasAnyRole('MASTER','OWNER')")
  @PostMapping
  public ResponseEntity<ResponseDto<ServiceAreaResponseDto>> createServiceArea(
      @Valid @RequestBody ServiceAreaRequestDto serviceAreaRequestDto) {

    ServiceAreaResponseDto serviceAreaResponseDto = serviceAreaService.createServiceArea(
        serviceAreaRequestDto);

    return ResponseEntity.ok(ResponseDto.success(serviceAreaResponseDto));
  }

  // 가게 ID로 배달 가능 지역 조회
  @GetMapping("/stores/{storeId}/areas")
  public ResponseEntity<ResponseDto<List<ServiceAreaResponseDto>>> getAreasByStoreId(
      @PathVariable UUID storeId) {

    List<ServiceAreaResponseDto> serviceAreaResponseDtos = serviceAreaService.getAreasByStoreId(
        storeId);
    return ResponseEntity.ok(ResponseDto.success(serviceAreaResponseDtos));
  }

  // 지역 ID로 해당 지역의 가게 조회 -> 페이징 처리 필요
  @GetMapping("/areas/{areaId}/stores")
  public ResponseEntity<ResponseDto<Page<ServiceAreaResponseDto>>> getStoresByAreaId(
      @PathVariable UUID areaId,
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable) {

    Pageable validatedPageable = PageableUtils.validatePageable(pageable);

    Page<ServiceAreaResponseDto> serviceAreaResponseDto = serviceAreaService.getStoresByAreaId(
        areaId, validatedPageable);
    return ResponseEntity.ok(ResponseDto.success(serviceAreaResponseDto));
  }

  // 서비스 지역 삭제 -> 서비스 지역 id는 관계 이므로 서비스 지역id 찾고 삭제 수행
  @PreAuthorize("hasAnyRole('MASTER','OWNER')")
  @DeleteMapping("/{storeId}/{areaId}")
  public ResponseEntity<ResponseDto<?>> deleteServiceArea(
      @PathVariable UUID storeId,
      @PathVariable UUID areaId) {

    serviceAreaService.deleteServiceArea(areaId, storeId);
    return ResponseEntity.ok(ResponseDto.success());
  }

}