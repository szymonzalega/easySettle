package com.easysettle.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Transfers.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transfers")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transfers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @ManyToOne
    private Members payer;

    @ManyToOne
    private Members loaner;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "payments_id", nullable = false)
    private Payments payments;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public Transfers amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Members getPayer() {
        return payer;
    }

    public Transfers payer(Members members) {
        this.payer = members;
        return this;
    }

    public void setPayer(Members members) {
        this.payer = members;
    }

    public Members getLoaner() {
        return loaner;
    }

    public Transfers loaner(Members members) {
        this.loaner = members;
        return this;
    }

    public void setLoaner(Members members) {
        this.loaner = members;
    }

    public Payments getPayments() {
        return payments;
    }

    public Transfers payments(Payments payments) {
        this.payments = payments;
        return this;
    }

    public void setPayments(Payments payments) {
        this.payments = payments;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transfers transfers = (Transfers) o;
        if (transfers.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transfers.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Transfers{" +
            "id=" + id +
            ", amount=" + amount +
            ", payer=" + payer +
            ", loaner=" + loaner +
            '}';
    }
}
