package com.nineteen.omp.store.controller;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.global.exception.CommonExceptionCode;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.store.controller.dto.AreaRequestDto;
import com.nineteen.omp.store.service.AreaService;
import com.nineteen.omp.store.service.dto.AreaResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/areas")
public class AreaController {

  private final AreaService areaService;

  @PostMapping //admin만 가능하게 -> security에서 처리
  public ResponseEntity<ResponseDto<AreaResponseDto>> createArea(
      @RequestBody AreaRequestDto areaRequestDto) {

    AreaResponseDto areaResponseDto = areaService.createArea(areaRequestDto);

    return ResponseEntity.ok(ResponseDto.success(areaResponseDto));
  }

  //키워드 -> 시,동,구 하나라도 있으면 검색
  @GetMapping
  public ResponseEntity<ResponseDto<Page<AreaResponseDto>>> searchArea(
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "name") String sortBy,
      @RequestParam(defaultValue = "true") boolean isAsc
  ) {
    if (page < 1 || size < 1) {
      throw new CustomException(CommonExceptionCode.INVALID_PARAMETER);
    }

    Sort sort = isAsc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page - 1, size, sort);
    Page<AreaResponseDto> searchedAreas = areaService.searchAreas(keyword, pageable);

    return ResponseEntity.ok(ResponseDto.success(searchedAreas));
  }

  //관리자만
  @GetMapping("{areaId}")
  public ResponseEntity<ResponseDto<AreaResponseDto>> getArea(@PathVariable UUID areaId) {
    AreaResponseDto areaResponseDto = areaService.getArea(areaId);
    return ResponseEntity.ok(ResponseDto.success(areaResponseDto));
  }

  //관리자만
  @PatchMapping("{areaId}")
  public ResponseEntity<ResponseDto<AreaResponseDto>> updateArea(
      @PathVariable UUID areaId,
      @RequestBody AreaRequestDto areaRequestDto) {
    AreaResponseDto areaResponseDto = areaService.updateArea(areaId, areaRequestDto);
    return ResponseEntity.ok(ResponseDto.success(areaResponseDto));
  }

  //관리자만
  @DeleteMapping("{areaId}")
  public ResponseEntity<?> deleteArea(@PathVariable UUID areaId) {
    areaService.deleteArea(areaId);
    return ResponseEntity.ok(ResponseDto.success());
  }
}
