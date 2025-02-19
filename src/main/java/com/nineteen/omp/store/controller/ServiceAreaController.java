package com.nineteen.omp.store.controller;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.store.controller.dto.ServiceAreaRequestDto;
import com.nineteen.omp.store.controller.dto.ServiceAreaResponseDto;
import com.nineteen.omp.store.domain.Area;
import com.nineteen.omp.store.domain.Store;
import com.nineteen.omp.store.service.AreaService;
import com.nineteen.omp.store.service.ServiceAreaService;
import com.nineteen.omp.store.service.StoreService;
import com.nineteen.omp.store.service.dto.ServiceAreaCommand;
import com.nineteen.omp.store.util.PageableUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/service-areas")
public class ServiceAreaController {

  private final ServiceAreaService serviceAreaService;
  private final AreaService areaService;
  private final StoreService storeService;

  @PostMapping
  public ResponseEntity<ResponseDto<ServiceAreaResponseDto>> createServiceArea(
      @RequestBody ServiceAreaRequestDto serviceAreaRequestDto) {

    Area area = areaService.getArea(serviceAreaRequestDto.areaId());
    Store store = storeService.getStore(serviceAreaRequestDto.storeId());
    System.out.println("확인" + area + ",   " + store);
    //command 이용
    ServiceAreaResponseDto serviceAreaResponseDto = serviceAreaService.createServiceArea(
        new ServiceAreaCommand(serviceAreaRequestDto, area, store));

    return ResponseEntity.ok(ResponseDto.success(serviceAreaResponseDto));
  }

  // 가게 ID로 배달 가능 지역 조회
  @GetMapping("/areas/{storeId}")
  public ResponseEntity<ResponseDto<List<ServiceAreaResponseDto>>> getAreasByStoreId(
      @PathVariable UUID storeId) {

    List<ServiceAreaResponseDto> serviceAreaResponseDtos = serviceAreaService.getAreasByStoreId(
        storeId);
    return ResponseEntity.ok(ResponseDto.success(serviceAreaResponseDtos));
  }

  // 지역 ID로 해당 지역의 가게 조회 -> 페이징 처리 필요
  @GetMapping("/stores/{areaId}")
  public ResponseEntity<ResponseDto<Page<ServiceAreaResponseDto>>> getStoresByAreaId(
      @PathVariable UUID areaId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "true") boolean isAsc) {

    Pageable pageable = PageableUtils.createPageable(page, size, sortBy, isAsc);

    Page<ServiceAreaResponseDto> serviceAreaResponseDto = serviceAreaService.getStoresByAreaId(
        areaId, pageable);
    return ResponseEntity.ok(ResponseDto.success(serviceAreaResponseDto));
  }

  // 서비스 지역 삭제 -> 서비스 지역 id는 관계 이므로 서비스 지역id 찾고 삭제 수행
  @DeleteMapping("/{storeId}/{areaId}")
  public ResponseEntity<ResponseDto<?>> deleteServiceArea(
      @PathVariable UUID storeId,
      @PathVariable UUID areaId) {

    serviceAreaService.deleteServiceArea(areaId, storeId);
    return ResponseEntity.ok(ResponseDto.success());
  }

}
