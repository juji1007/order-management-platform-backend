package com.nineteen.omp.store.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;

public record StoreRequestDto(
    @NotBlank(message = "스토어 카테고리는 필수 입력값입니다.")
    @Size(max = 20, message = "스토어 카테고리는 최대 20자까지 입력 가능합니다.")
    String storeCategoryName,

    @NotBlank(message = "스토어 이름은 필수 입력값입니다.")
    @Size(max = 50, message = "스토어 이름은 최대 50자까지 입력 가능합니다.")
    String name,

    @NotBlank(message = "스토어 주소는 필수 입력값입니다.")
    @Size(max = 50, message = "스토어 주소는 최대 50자까지 입력 가능합니다.")
    String address,

    @NotBlank(message = "스토어 전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    @Size(max = 15, message = "스토어 전화번호는 최대 15자까지 입력 가능합니다. (하이픈 포함)")
    String phone,

    LocalTime openHours,
    LocalTime closeHours,

    String closedDays
) {

}
