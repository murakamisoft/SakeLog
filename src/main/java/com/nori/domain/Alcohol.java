package com.nori.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Alcohol.
 */
@Entity
@Table(name = "alcohol")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Alcohol implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "alcohol_name", length = 200, nullable = false)
    private String alcoholName;

    @Size(max = 50)
    @Column(name = "alcohol_type", length = 50)
    private String alcoholType;

    @Size(max = 200)
    @Column(name = "brand_name", length = 200)
    private String brandName;

    @Column(name = "abv", precision = 21, scale = 2)
    private BigDecimal abv;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Alcohol id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlcoholName() {
        return this.alcoholName;
    }

    public Alcohol alcoholName(String alcoholName) {
        this.setAlcoholName(alcoholName);
        return this;
    }

    public void setAlcoholName(String alcoholName) {
        this.alcoholName = alcoholName;
    }

    public String getAlcoholType() {
        return this.alcoholType;
    }

    public Alcohol alcoholType(String alcoholType) {
        this.setAlcoholType(alcoholType);
        return this;
    }

    public void setAlcoholType(String alcoholType) {
        this.alcoholType = alcoholType;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public Alcohol brandName(String brandName) {
        this.setBrandName(brandName);
        return this;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public BigDecimal getAbv() {
        return this.abv;
    }

    public Alcohol abv(BigDecimal abv) {
        this.setAbv(abv);
        return this;
    }

    public void setAbv(BigDecimal abv) {
        this.abv = abv;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alcohol)) {
            return false;
        }
        return getId() != null && getId().equals(((Alcohol) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alcohol{" +
            "id=" + getId() +
            ", alcoholName='" + getAlcoholName() + "'" +
            ", alcoholType='" + getAlcoholType() + "'" +
            ", brandName='" + getBrandName() + "'" +
            ", abv=" + getAbv() +
            "}";
    }
}
