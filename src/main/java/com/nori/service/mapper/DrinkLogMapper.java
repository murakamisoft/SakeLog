package com.nori.service.mapper;

import com.nori.domain.Alcohol;
import com.nori.domain.DrinkLog;
import com.nori.domain.Place;
import com.nori.domain.User;
import com.nori.service.dto.AlcoholDTO;
import com.nori.service.dto.DrinkLogDTO;
import com.nori.service.dto.PlaceDTO;
import com.nori.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DrinkLog} and its DTO {@link DrinkLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface DrinkLogMapper extends EntityMapper<DrinkLogDTO, DrinkLog> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "alcohol", source = "alcohol", qualifiedByName = "alcoholId")
    @Mapping(target = "place", source = "place", qualifiedByName = "placeId")
    DrinkLogDTO toDto(DrinkLog s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("alcoholId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlcoholDTO toDtoAlcoholId(Alcohol alcohol);

    @Named("placeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlaceDTO toDtoPlaceId(Place place);
}
