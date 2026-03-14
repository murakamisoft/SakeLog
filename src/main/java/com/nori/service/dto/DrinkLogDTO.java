package com.nori.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nori.domain.DrinkLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DrinkLogDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant drinkDate;

    private Integer quantity;

    private Integer rating;

    @Size(max = 1000)
    private String memo;

    private Instant createdAt;

    private UserDTO user;

    private AlcoholDTO alcohol;

    private PlaceDTO place;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDrinkDate() {
        return drinkDate;
    }

    public void setDrinkDate(Instant drinkDate) {
        this.drinkDate = drinkDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AlcoholDTO getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(AlcoholDTO alcohol) {
        this.alcohol = alcohol;
    }

    public PlaceDTO getPlace() {
        return place;
    }

    public void setPlace(PlaceDTO place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DrinkLogDTO)) {
            return false;
        }

        DrinkLogDTO drinkLogDTO = (DrinkLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, drinkLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DrinkLogDTO{" +
            "id=" + getId() +
            ", drinkDate='" + getDrinkDate() + "'" +
            ", quantity=" + getQuantity() +
            ", rating=" + getRating() +
            ", memo='" + getMemo() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", user=" + getUser() +
            ", alcohol=" + getAlcohol() +
            ", place=" + getPlace() +
            "}";
    }
}
