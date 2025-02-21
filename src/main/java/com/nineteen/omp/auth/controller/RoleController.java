package com.nineteen.omp.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

  @PreAuthorize("hasRole('MASTER')")
  @PostMapping("/masters")
  public ResponseEntity<String> getMasters() {
    log.info("Access granted to Master.");
    return ResponseEntity.ok("Masters endpoint");
  }

  @PreAuthorize("hasAnyRole('MASTER','OWNER')")
  @PostMapping("/owners")
  public ResponseEntity<String> getOwner() {
    log.info("Access granted to Owners.");
    log.info(getMasters().toString());
    return ResponseEntity.ok("Owners endpoint");
  }

  @PreAuthorize("hasAnyRole('MASTER','OWNER','USER')")
  @PostMapping("/users")
  public ResponseEntity<String> getUsers() {
    log.info("Access granted to Users.");
    return ResponseEntity.ok("Users endpoint");
  }
}
