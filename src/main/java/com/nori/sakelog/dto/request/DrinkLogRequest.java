package com.nori.sakelog.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DrinkLogRequest {
  public Long drinkId;
  public LocalDate drinkDate;
  public Integer amountMl;
  public Integer rating;
  public String comment;
}
