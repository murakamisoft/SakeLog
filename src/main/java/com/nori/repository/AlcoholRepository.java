package com.nori.repository;

import com.nori.domain.Alcohol;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Alcohol entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlcoholRepository extends JpaRepository<Alcohol, Long> {}
