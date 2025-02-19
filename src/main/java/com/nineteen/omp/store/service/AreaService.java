package com.nineteen.omp.store.service;

import com.nineteen.omp.global.exception.CommonExceptionCode;
import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.store.controller.dto.AreaRequestDto;
import com.nineteen.omp.store.domain.Area;
import com.nineteen.omp.store.exception.AreaException;
import com.nineteen.omp.store.exception.AreaExceptionCode;
import com.nineteen.omp.store.repository.AreaRepository;
import com.nineteen.omp.store.service.dto.AreaResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AreaService {

  private final AreaRepository areaRepository;

  @Transactional
  public AreaResponseDto createArea(AreaRequestDto areaRequestDto) {
    boolean isAreaExists = areaRepository.existsBySiAndGuAndDong(areaRequestDto.si(),
        areaRequestDto.gu(), areaRequestDto.dong());
    if (isAreaExists) {
      throw new AreaException(AreaExceptionCode.AREA_IS_DUPLICATED);
    }

    Area area = Area.builder()
        .si(areaRequestDto.si())
        .gu(areaRequestDto.gu())
        .dong(areaRequestDto.dong())
        .build();
    Area savedArea = areaRepository.save(area);
    return toResponseDto(savedArea);
  }

  //Search
  public Page<AreaResponseDto> searchAreas(
      String keyword,
      Pageable pageable) {
    // Pageable 검증
    if (pageable.getPageNumber() < 0) {
      throw new CustomException(CommonExceptionCode.INVALID_PARAMETER);
    }

    return areaRepository.searchAreas(keyword, pageable);
  }

  //Read
  @Transactional(readOnly = true)
  public AreaResponseDto getArea(UUID areaId) {
    Area area = areaRepository.findById(areaId)
        .filter(a -> a.getIsDeleted() == false)
        .orElseThrow(() -> new AreaException(AreaExceptionCode.AREA_NOT_FOUND));
    return toResponseDto(area);
  }

  //update
  @Transactional
  public AreaResponseDto updateArea(UUID areaId, AreaRequestDto requestDto) {
    Area area = areaRepository.findById(areaId)
        .filter(a -> a.getIsDeleted() == false)
        .orElseThrow(() -> new AreaException(AreaExceptionCode.AREA_NOT_FOUND));

    //setter랑 같은 것 같음?
    if (requestDto.si() != null) {
      area.changeNameSi(requestDto.si());
    }
    if (requestDto.gu() != null) {
      area.changeNameGu(requestDto.gu());
    }
    if (requestDto.dong() != null) {
      area.changeNameDong(requestDto.dong());
    }

    Area savedArea = areaRepository.save(area);
    return toResponseDto(savedArea);
  }

  //softDelete
  @Transactional
  public Boolean deleteArea(UUID areaId) {
    Area area = areaRepository.findById(areaId)
        .filter(a -> a.getIsDeleted() == false)
        .orElseThrow(() -> new AreaException(AreaExceptionCode.AREA_NOT_FOUND));

    //@PreRemove
    areaRepository.deleteById(areaId);

    return true;
  }

  private AreaResponseDto toResponseDto(Area area) {
    return new AreaResponseDto(
        area.getId(),
        area.getSi(),
        area.getGu(),
        area.getDong()
    );
  }
}
