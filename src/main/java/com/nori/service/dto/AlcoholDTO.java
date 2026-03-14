package com.nori.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.nori.domain.Alcohol} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlcoholDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String alcoholName;

    @Size(max = 50)
    private String alcoholType;

    @Size(max = 200)
    private String brandName;

    private BigDecimal abv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlcoholName() {
        return alcoholName;
    }

    public void setAlcoholName(String alcoholName) {
        this.alcoholName = alcoholName;
    }

    public String getAlcoholType() {
        return alcoholType;
    }

    public void setAlcoholType(String alcoholType) {
        this.alcoholType = alcoholType;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public BigDecimal getAbv() {
        return abv;
    }

    public void setAbv(BigDecimal abv) {
        this.abv = abv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlcoholDTO)) {
            return false;
        }

        AlcoholDTO alcoholDTO = (AlcoholDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alcoholDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlcoholDTO{" +
            "id=" + getId() +
            ", alcoholName='" + getAlcoholName() + "'" +
            ", alcoholType='" + getAlcoholType() + "'" +
            ", brandName='" + getBrandName() + "'" +
            ", abv=" + getAbv() +
            "}";
    }
}
