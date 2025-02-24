package com.nineteen.omp.store.controller;

import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.global.utils.PageableUtils;
import com.nineteen.omp.store.controller.dto.AreaRequestDto;
import com.nineteen.omp.store.controller.dto.AreaResponseDto;
import com.nineteen.omp.store.service.AreaService;
import jakarta.validation.Valid;
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

  @PreAuthorize("hasRole('MASTER')")
  @PostMapping //admin만 가능하게 -> security에서 처리
  public ResponseEntity<ResponseDto<AreaResponseDto>> createArea(
      @Valid @RequestBody AreaRequestDto areaRequestDto) {

    AreaResponseDto areaResponseDto = areaService.createArea(areaRequestDto);

    return ResponseEntity.ok(ResponseDto.success(areaResponseDto));
  }

  //키워드 -> 시,동,구 하나라도 있으면 검색
  @GetMapping
  public ResponseEntity<ResponseDto<Page<AreaResponseDto>>> searchArea(
      @RequestParam(
          name = "keyword",
          defaultValue = "",
          required = false
      ) String keyword,
      @PageableDefault(
          size = 10,
          page = 1,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable) {
    Pageable validatedPageable = PageableUtils.validatePageable(pageable);

    Page<AreaResponseDto> searchedAreas = areaService.searchAreas(keyword, validatedPageable);

    return ResponseEntity.ok(ResponseDto.success(searchedAreas));
  }

  @PreAuthorize("hasRole('MASTER')")
  @GetMapping("{areaId}")
  public ResponseEntity<ResponseDto<AreaResponseDto>> getArea(@PathVariable UUID areaId) {
    AreaResponseDto areaResponseDto = areaService.getArea(areaId);
    return ResponseEntity.ok(ResponseDto.success(areaResponseDto));
  }

  @PreAuthorize("hasRole('MASTER')")
  @PatchMapping("{areaId}")
  public ResponseEntity<ResponseDto<AreaResponseDto>> updateArea(
      @PathVariable UUID areaId,
      @Valid @RequestBody AreaRequestDto areaRequestDto) {
    AreaResponseDto areaResponseDto = areaService.updateArea(areaId, areaRequestDto);
    return ResponseEntity.ok(ResponseDto.success(areaResponseDto));
  }

  @PreAuthorize("hasRole('MASTER')")
  @DeleteMapping("{areaId}")
  public ResponseEntity<?> deleteArea(@PathVariable UUID areaId) {
    areaService.deleteArea(areaId);
    return ResponseEntity.ok(ResponseDto.success());
  }
}
