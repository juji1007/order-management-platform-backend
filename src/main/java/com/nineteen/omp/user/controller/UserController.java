package com.nineteen.omp.user.controller;

import com.nineteen.omp.auth.dto.UserDetailsImpl;
import com.nineteen.omp.global.dto.ResponseDto;
import com.nineteen.omp.global.utils.PageableUtils;
import com.nineteen.omp.user.controller.dto.GetUserInfoPageResponseDto;
import com.nineteen.omp.user.controller.dto.GetUserInfoResponseDto;
import com.nineteen.omp.user.controller.dto.SignupRequestDto;
import com.nineteen.omp.user.controller.dto.UpdateUserRequestDto;
import com.nineteen.omp.user.service.UserService;
import com.nineteen.omp.user.service.dto.GetUserInfoPageResponseCommand;
import com.nineteen.omp.user.service.dto.UpdateUserRequestCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<ResponseDto<?>> signup(
      @RequestBody @Valid SignupRequestDto requestDto
  ) {
    userService.signup(requestDto);
    return ResponseEntity.ok(ResponseDto.success(requestDto.username()));
  }

  @PreAuthorize("permitAll()")
  @GetMapping("/my")
  public ResponseEntity<ResponseDto<?>> getUserInfo(
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    var responseCommand = userService.getUserInfo(userDetails.getUserId());
    var response = new GetUserInfoResponseDto(responseCommand);
    return ResponseEntity.ok(ResponseDto.success(response));
  }

  @PreAuthorize("hasRole('MASTER')")
  @GetMapping("/all")
  public ResponseEntity<ResponseDto<?>> getUsers(
      @PageableDefault(
          size = 10,
          page = 0,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable
  ) {
    pageable = PageableUtils.validatePageable(pageable);
    var responseCommand = userService.getUsers(pageable);
    var responseDto = convertCommandToDto(pageable, responseCommand);
    return ResponseEntity.ok(ResponseDto.success(responseDto));
  }

  @PatchMapping("/my")
  public ResponseEntity<ResponseDto<?>> updateUser(
      @RequestBody @Valid UpdateUserRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    UpdateUserRequestCommand requestCommand = new UpdateUserRequestCommand(requestDto);
    userService.updateUser(userDetails.getUserId(), requestCommand);
    return ResponseEntity.ok(ResponseDto.success());
  }

  @PatchMapping("/withdraw ")
  public ResponseEntity<ResponseDto<?>> deleteUser(
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    userService.deleteUser(userDetails.getUserId());
    return ResponseEntity.ok(ResponseDto.success());
  }
  
  @PreAuthorize("hasRole('MASTER')")
  @GetMapping("/search")
  public ResponseEntity<ResponseDto<?>> searchUser(
      @RequestParam(
          name = "nickname",
          defaultValue = ""
      ) String nickname,
      @PageableDefault(
          size = 10,
          page = 0,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable
  ) {
    pageable = PageableUtils.validatePageable(pageable);
    var responseCommand = userService.searchUser(nickname, pageable);
    var responseDto = convertCommandToDto(pageable, responseCommand);
    return ResponseEntity.ok(ResponseDto.success(responseDto));
  }

  private static GetUserInfoPageResponseDto convertCommandToDto(
      Pageable pageable,
      GetUserInfoPageResponseCommand responseCommand
  ) {
    var responseDtoList = responseCommand.getUserPageResponseCommandPage().stream()
        .map(GetUserInfoResponseDto::new)
        .toList();
    var responseDtoPage = new PageImpl<>(
        responseDtoList,
        pageable,
        responseCommand.getUserPageResponseCommandPage().getTotalElements()
    );
    return new GetUserInfoPageResponseDto(responseDtoPage);
  }

}
