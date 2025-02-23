package com.nineteen.omp.user.controller.dto;

import org.springframework.data.domain.Page;

public record GetUserInfoPageResponseDto(
    Page<GetUserInfoResponseDto> getUserInfoResponseDtoPage
) {

}
