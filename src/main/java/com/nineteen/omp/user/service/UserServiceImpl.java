package com.nineteen.omp.user.service;

import com.nineteen.omp.global.exception.CustomException;
import com.nineteen.omp.user.controller.dto.SignupRequestDto;
import com.nineteen.omp.user.domain.User;
import com.nineteen.omp.user.exception.UserExceptionCode;
import com.nineteen.omp.user.repository.UserRepository;
import com.nineteen.omp.user.service.dto.GetUserInfoPageResponseCommand;
import com.nineteen.omp.user.service.dto.GetUserInfoResponseCommand;
import com.nineteen.omp.user.service.dto.UpdateUserRequestCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void signup(SignupRequestDto requestDto) {

    String encodedPassword = passwordEncoder.encode(requestDto.password());

    // 회원 중복 확인
    boolean checkUsername = userRepository.existsByUsername(requestDto.username());
    if (checkUsername) {
      throw new CustomException(UserExceptionCode.DUPLICATE_USERNAME);
    }
    // 사용자 등록
    User user = User.builder()
        .username(requestDto.username())
        .password(encodedPassword)
        .nickname(requestDto.nickname())
        .role(requestDto.role())
        .email(requestDto.email())
        .delivery_address(requestDto.delivery_address())
        .build();

    userRepository.save(user);
  }

  @Override
  public GetUserInfoResponseCommand getUserInfo(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserExceptionCode.USER_NOT_FOUND));
    return new GetUserInfoResponseCommand(user);
  }

  @Override
  public GetUserInfoPageResponseCommand getUsers(Pageable pageable) {
    Page<User> userPage = userRepository.findAll(pageable);
    var contents = userPage.stream()
        .map(GetUserInfoResponseCommand::new)
        .toList();
    var responseCommandPage = new PageImpl<>(contents,
        pageable, userPage.getTotalElements());
    return new GetUserInfoPageResponseCommand(responseCommandPage);
  }

  @Override
  @Transactional
  public void updateUser(Long userId, UpdateUserRequestCommand requestCommand) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserExceptionCode.USER_NOT_FOUND));
    user.update(requestCommand);
  }

  @Override
  @Transactional
  public void deleteUser(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new CustomException(UserExceptionCode.USER_NOT_FOUND);
    }
    userRepository.deleteById(userId);
  }

  @Override
  public GetUserInfoPageResponseCommand searchUser(String nickname, Pageable pageable) {
    Page<User> userPage = userRepository.findAllByNicknameContainsIgnoreCase(nickname, pageable);
    var contents = userPage.stream()
        .map(GetUserInfoResponseCommand::new)
        .toList();
    var pageResponseCommand = new PageImpl<>(
        contents,
        pageable,
        userPage.getTotalElements()
    );
    return new GetUserInfoPageResponseCommand(pageResponseCommand);
  }

  @Transactional
  public void updateUserRole(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserExceptionCode.USER_NOT_FOUND));

    user.updateRoleToOwner();
  }

}
