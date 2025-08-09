package com.nori.sakelog.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DrinkLogResponse {
  public Long tDrinkLogId;
  public LocalDateTime createdAt;
}
