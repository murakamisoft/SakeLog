package com.nori.sakelog.repository;

import com.nori.sakelog.entity.DrinkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrinkLogRepository extends JpaRepository<DrinkLog, Long> {
}
