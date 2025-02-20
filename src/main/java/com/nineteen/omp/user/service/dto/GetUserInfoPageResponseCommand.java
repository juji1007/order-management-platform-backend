package com.nineteen.omp.user.service.dto;

import org.springframework.data.domain.Page;

public record GetUserInfoPageResponseCommand(
    Page<GetUserInfoResponseCommand> getUserPageResponseCommandPage
) {

}
