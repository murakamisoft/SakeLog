package com.nori.repository;

import com.nori.domain.DrinkLog;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DrinkLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DrinkLogRepository extends JpaRepository<DrinkLog, Long> {
    @Query("select drinkLog from DrinkLog drinkLog where drinkLog.user.login = ?#{authentication.name}")
    List<DrinkLog> findByUserIsCurrentUser();
}
