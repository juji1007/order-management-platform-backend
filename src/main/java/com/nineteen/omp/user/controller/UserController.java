package com.nineteen.omp.user.controller;

import com.nineteen.omp.global.dto.ResponseDto;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/users/signup")
  public ResponseEntity<ResponseDto<?>> signup(
      @RequestBody @Valid SignupRequestDto requestDto
  ) {
    userService.signup(requestDto);
    return ResponseEntity.ok(ResponseDto.success(requestDto.username()));
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<ResponseDto<?>> getUserInfo(
      @PathVariable(name = "userId") Long userId
  ) {
    var responseCommand = userService.getUserInfo(userId);
    var response = new GetUserInfoResponseDto(responseCommand);
    return ResponseEntity.ok(ResponseDto.success(response));
  }

  @GetMapping("/users/all")
  public ResponseEntity<ResponseDto<?>> getUsers(
      @PageableDefault(
          size = 10,
          page = 0,
          sort = {"createdAt", "updatedAt"},
          direction = Direction.ASC
      ) Pageable pageable
  ) {
    // Master 만 조회 가능
//    pageable = PageableUtils.validatePageable(pageable);
    var responseCommand = userService.getUsers(pageable);
    var responseDto = convertCommandToDto(pageable, responseCommand);
    return ResponseEntity.ok(ResponseDto.success(responseDto));
  }

  @PatchMapping("/users/{userId}")
  public ResponseEntity<ResponseDto<?>> updateUser(
      @PathVariable(name = "userId") Long userId,
      @RequestBody @Valid UpdateUserRequestDto requestDto
  ) {
    UpdateUserRequestCommand requestCommand = new UpdateUserRequestCommand(requestDto);
    userService.updateUser(userId, requestCommand);
    return ResponseEntity.ok(ResponseDto.success());
  }

  @PatchMapping("/users/{userId}/withdraw ")
  public ResponseEntity<ResponseDto<?>> deleteUser(
      @PathVariable(name = "userId") Long userId
  ) {
    userService.deleteUser(userId);
    return ResponseEntity.ok(ResponseDto.success());
  }

  @GetMapping("/users")
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
    // Master 만 조회 가능
//    pageable = PageableUtils.validatePageable(pageable);
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
