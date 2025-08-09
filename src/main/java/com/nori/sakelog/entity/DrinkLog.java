package com.nori.sakelog.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "t_drinklog")
public class DrinkLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "t_drink_log_id")
  private Long tDrinkLogId;

  @Column(nullable = false)
  private Long drinkId;

  @Column(nullable = false)
  private LocalDate drinkDate;

  @Column(nullable = false)
  private Integer amountMl;

  private Integer rating;

  @Column(columnDefinition = "TEXT")
  private String comment;
}