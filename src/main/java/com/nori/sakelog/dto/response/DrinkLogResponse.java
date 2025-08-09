package com.nori.sakelog.dto.response;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
public class DrinkLogResponse {
  public Long tDrinkLogId;
  @CreationTimestamp
  public LocalDateTime createdAt;
}
