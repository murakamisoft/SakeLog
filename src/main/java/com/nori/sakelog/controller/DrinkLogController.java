package com.nori.sakelog.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nori.sakelog.dto.request.DrinkLogRequest;
import com.nori.sakelog.dto.response.DrinkLogResponse;
import com.nori.sakelog.service.DrinkLogService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/drinklogs")
public class DrinkLogController {

  private final DrinkLogService drinkLogService;

  @PostMapping
  public ResponseEntity<DrinkLogResponse> createDrinkLog(@Validated @RequestBody DrinkLogRequest request) {
    DrinkLogResponse response = drinkLogService.createDrinkLog(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}