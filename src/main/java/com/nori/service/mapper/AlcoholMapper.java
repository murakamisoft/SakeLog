package com.nori.service.mapper;

import com.nori.domain.Alcohol;
import com.nori.service.dto.AlcoholDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Alcohol} and its DTO {@link AlcoholDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlcoholMapper extends EntityMapper<AlcoholDTO, Alcohol> {}
