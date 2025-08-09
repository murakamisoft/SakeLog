package com.nori.sakelog.service;

import com.nori.sakelog.dto.request.DrinkLogRequest;
import com.nori.sakelog.dto.response.DrinkLogResponse;
import com.nori.sakelog.entity.DrinkLog;
import com.nori.sakelog.repository.DrinkLogRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class DrinkLogService {

  private final DrinkLogRepository repository;

  public DrinkLogResponse createDrinkLog(DrinkLogRequest request) {
    // リクエスト DTO → Entity 変換
    DrinkLog entity = new DrinkLog();
    entity.setDrinkId(request.getDrinkId());
    entity.setDrinkDate(request.getDrinkDate());
    entity.setAmountMl(request.getAmountMl());
    entity.setRating(request.getRating());
    entity.setComment(request.getComment());

    // 保存
    DrinkLog saved = repository.save(entity);

    // Entity → レスポンス DTO 変換
    DrinkLogResponse response = new DrinkLogResponse();
    response.setTDrinkLogId(saved.getTDrinkLogId());
    response.setCreatedAt(LocalDateTime.now());

    return response;
  }
}
