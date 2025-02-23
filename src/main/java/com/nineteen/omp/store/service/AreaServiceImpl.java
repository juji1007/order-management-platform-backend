package com.nineteen.omp.store.service;

import com.nineteen.omp.store.controller.dto.AreaRequestDto;
import com.nineteen.omp.store.controller.dto.AreaResponseDto;
import com.nineteen.omp.store.domain.Area;
import com.nineteen.omp.store.exception.AreaException;
import com.nineteen.omp.store.exception.AreaExceptionCode;
import com.nineteen.omp.store.repository.AreaRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {

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
  public Page<AreaResponseDto> searchAreas(String keyword, Pageable pageable) {
    return areaRepository.searchAreas(keyword, pageable);
  }

  //Read
  @Transactional(readOnly = true)
  public AreaResponseDto getArea(UUID areaId) {
    return toResponseDto(findByIdOrElseThrow(areaId));
  }

  //update
  @Transactional
  public AreaResponseDto updateArea(UUID areaId, AreaRequestDto requestDto) {
    Area area = findByIdOrElseThrow(areaId);

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

  @Transactional
  public void deleteArea(UUID areaId) {
    areaRepository.deleteById(findByIdOrElseThrow(areaId).getId());
  }

  private Area findByIdOrElseThrow(UUID areaId) {
    return areaRepository.findById(areaId)
        .orElseThrow(() -> new AreaException(AreaExceptionCode.AREA_NOT_FOUND));
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
