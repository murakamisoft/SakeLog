package com.nori.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DrinkLog.
 */
@Entity
@Table(name = "drink_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DrinkLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "drink_date", nullable = false)
    private Instant drinkDate;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "rating")
    private Integer rating;

    @Size(max = 1000)
    @Column(name = "memo", length = 1000)
    private String memo;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Alcohol alcohol;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DrinkLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDrinkDate() {
        return this.drinkDate;
    }

    public DrinkLog drinkDate(Instant drinkDate) {
        this.setDrinkDate(drinkDate);
        return this;
    }

    public void setDrinkDate(Instant drinkDate) {
        this.drinkDate = drinkDate;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public DrinkLog quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getRating() {
        return this.rating;
    }

    public DrinkLog rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getMemo() {
        return this.memo;
    }

    public DrinkLog memo(String memo) {
        this.setMemo(memo);
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public DrinkLog createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DrinkLog user(User user) {
        this.setUser(user);
        return this;
    }

    public Alcohol getAlcohol() {
        return this.alcohol;
    }

    public void setAlcohol(Alcohol alcohol) {
        this.alcohol = alcohol;
    }

    public DrinkLog alcohol(Alcohol alcohol) {
        this.setAlcohol(alcohol);
        return this;
    }

    public Place getPlace() {
        return this.place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public DrinkLog place(Place place) {
        this.setPlace(place);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DrinkLog)) {
            return false;
        }
        return getId() != null && getId().equals(((DrinkLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DrinkLog{" +
            "id=" + getId() +
            ", drinkDate='" + getDrinkDate() + "'" +
            ", quantity=" + getQuantity() +
            ", rating=" + getRating() +
            ", memo='" + getMemo() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
