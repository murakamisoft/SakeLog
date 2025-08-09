package com.nori.sakelog.dto.request;

import java.time.LocalDate;

import jakarta.annotation.Nonnull;
import lombok.Data;

@Data
public class DrinkLogRequest {

  @Nonnull
  public Long drinkId;

  @Nonnull
  public LocalDate drinkDate;

  @Nonnull
  public Integer amountMl;

  public Integer rating;
  public String comment;
}
